/*
 * This file is part of TrOWL.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.query;

import com.truemesh.squiggle.Column;
import com.truemesh.squiggle.SelectQuery;
import com.truemesh.squiggle.Table;
import com.truemesh.squiggle.criteria.MatchCriteria;
import java.util.*;
import eu.trowl.db.*;
import eu.trowl.hashing.FNV;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ethomas
 */
public abstract class Query {

    /**
     *
     */
    protected int limit = 0;
    /**
     *
     */
    protected int offset = 0;
    /**
     *
     */
    protected boolean distinct = false;
    /**
     *
     */
    protected Map resultVars;
    /**
     *
     */
    protected List<Concept> classes;
    /**
     *
     */
    protected List<Role> properties;
    /**
     *
     */
    protected Bindings varBindings;
    private Set<String> distinguishedVars;
    private Map<String, String> classPaths;
    private Map<String, String> propertyPaths;
    private Map<String, String> propertyTypes;
    private Map<String, String> classPathBindings;
    private Map<String, String> propertyPathBindings;
    private Bindings varClassBindings;
    private Bindings tableBindings;
    private List<String> tables;
    private List<String> cpTables;
    private List<String> ppTables;
    private DB db;
    private static final String TABLE_IDENTIFIER = "t";
    private static final String CPATH_TABLE_IDENTIFIER = "c";
    private static final String PPATH_TABLE_IDENTIFIER = "p";
    /**
     *
     */
    protected static String VARIABLE_PREFIX = "?";
    protected Map<String, String> foundAt;
    protected VarColBinding varColBinding;
    private SelectQuery query;

    /**
     *
     * @param repo
     * @throws Exception
     */
    public Query(String repo) throws Exception {
        init();

        db = DBFactory.construct(repo);

        db.connect();
    }

    /**
     *
     * @throws Exception
     */
    public Query() throws Exception {
        init();

        db = DBFactory.construct();
        db.connect();
    }

    private void init() {
        tables = new ArrayList<String>();
        cpTables = new ArrayList<String>();
        ppTables = new ArrayList<String>();
        varBindings = new Bindings();
        tableBindings = new Bindings();
        varClassBindings = new Bindings();
        classPaths = new HashMap<String, String>();
        propertyPaths = new HashMap<String, String>();

        propertyTypes = new HashMap<String, String>();
        classPathBindings = new HashMap<String, String>();
        propertyPathBindings = new HashMap<String, String>();
        distinguishedVars = new HashSet();
        classes = new ArrayList<Concept>();
        properties = new ArrayList<Role>();
        resultVars = new HashMap();
        foundAt = new HashMap<String, String>();
        varColBinding = new VarColBinding();
        query = new SelectQuery();
    }

    /**
     *
     * @throws QuerySyntaxException
     */
    public abstract void process() throws QuerySyntaxException;

    /**
     *
     * @param repo
     * @return
     * @throws QueryException
     */
    public eu.trowl.query.ResultSet execute(String repo) throws QueryException {

        try {
            db = DBFactory.construct(repo);
            db.connect();
        } catch (Exception e) {
            throw new QueryDatabaseException("Unable to connect to database: " + e.getLocalizedMessage());
        }

        this.process();
        ResultSet res;

        try {
            res = db.execSQL(this.toSQL());
            //System.out.print(this.toSQL());

            return eu.trowl.query.ResultSet.fromSQLResultSet(res);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new QueryDatabaseException();
        }
    }

    public boolean ask(String repo) throws QueryException {
        try {
            db = DBFactory.construct(repo);
            db.connect();
        } catch (Exception e) {
            throw new QueryDatabaseException("Unable to connect to database: " + e.getLocalizedMessage());
        }

        this.process();
        ResultSet res;

        try {
            res = db.execSQL(this.toSQL());
            //System.out.print(this.toSQL());

            return res.next();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new QueryDatabaseException();
        }

    }

    private List<String> getPropertyPathType(String uri) throws SQLException {
        /*List<String> a = new ArrayList<String>();
        a.add("blah");
        a.add("o");
        return a;*/
        return db.queryAtomicList(Queries.GET_PATH_FOR_PROPERTY_URI, uri);
    }

    private String getClassPath(String uri) throws SQLException {
        return db.queryAtomic(Queries.GET_PATH_FOR_CLASS_URI, uri);
    }

    private String addPropertyPath(String uri) throws Exception {
        if (!propertyPaths.containsKey(uri)) {
            List<String> data = getPropertyPathType(uri);
            if (!data.isEmpty()) {
                propertyPaths.put(uri, data.get(0));
                propertyTypes.put(uri, data.get(1));
            } else {
                throw new QueryException("Unknown property in query");
            }
        }
        String tableId;

        if (propertyTypes.get(uri).equals("d")) {
            tableId = addTable("dproperty_instances");

        } else {
            tableId = addTable("oproperty_instances");
        }
        String pTable = addPPTable("propertypaths");
        propertyPathBindings.put(pTable, propertyPaths.get(uri));
        tableBindings.put(appendColumn(pTable, "property_id"), appendColumn(tableId, "property_id"));
        return tableId;
    }

    private void addClassPath(String uri, String table) throws Exception {
        if (!classPaths.containsKey(uri)) {
            classPaths.put(uri, getClassPath(uri));
        }
        String pTable = addCPTable("classpaths");
        classPathBindings.put(pTable, classPaths.get(uri));
        tableBindings.put(appendColumn(pTable, "class_id"), appendColumn(table, "class_id"));
    }

    private void addClassPath(String uri, Column col) throws Exception {
        if (!classPaths.containsKey(uri)) {
            classPaths.put(uri, getClassPath(uri));
        }
        Table cpTab = new Table("classpaths");
        Column cpCol = new Column(cpTab, "path");

        query.addJoin(col.getTable(), col.getName(), cpTab, "class_id");
        query.addCriteria(new MatchCriteria(cpCol, MatchCriteria.LIKE, classPaths.get(uri) + '%'));
    }

    /**
     *
     * @param table
     * @return
     */
    public String addTable(String table) {
        tables.add(table);
        return TABLE_IDENTIFIER + tables.size();
    }

    /**
     *
     * @param table
     * @return
     */
    public String addCPTable(String table) {
        cpTables.add(table);
        return CPATH_TABLE_IDENTIFIER + cpTables.size();
    }

    /**
     *
     * @param table
     * @return
     */
    public String addPPTable(String table) {
        ppTables.add(table);
        return PPATH_TABLE_IDENTIFIER + ppTables.size();
    }

    private void prepClasses() throws Exception {
        for (Concept c : classes) {
            // System.out.println("Processing: " + c.getName() + "(" + c.getValue() + ")");

            String tableId = addTable("individuals");
            Table t = new Table("individuals");
            Column col = new Column(t, "class_id");

            addClassPath(c.getName(), tableId); // set the classpath for this table
            addClassPath(c.getName(), col); // set the classpath for this table


            if (c.getValue().isVar()) {
                //System.out.println("HA");
                bindVar(c.getValue().getVar(), appendColumn(tableId, "uri"));
                bindVarClass(c.getValue().getVar(), tableId);

                addDistinguishedVar(c.getValue().getVar());

                varColBinding.put(c.getValue().getVar(), col);

            } else {
                //System.out.println("OH");
                bindURI(c.getValue().getURI().toString(), appendColumn(tableId, "uri"));
                query.addCriteria(new MatchCriteria(t, "uri", MatchCriteria.EQUALS, c.getValue().getURI().toString()));
            }
        }
    }

    private void identifyDistinguishedVars() {
        Set<String> dvcDomain = new HashSet<String>();
        Set<String> dvcRange = new HashSet<String>();

        for (Role r : properties) {
            if (isVar(r.getDomain())) {
                dvcDomain.add(r.getDomain());
            }

            if (isVar(r.getRange())) {
                dvcRange.add(r.getRange());
            }
        }

        dvcDomain.retainAll(dvcRange); // messy way of saying intersect the sets
        distinguishedVars.addAll(dvcDomain);
    }

    private void prepProperties() throws Exception {
        //System.out.println("Processing properties");
        identifyDistinguishedVars();

        for (Role r : properties) {
            //  System.out.println("Processing: " + r.getURI() + "(" + r.getDomain() + "," + r.getRange() + ")");

            /*if (isVar(r.getDomain()) && isVar(r.getRange()) && distinguishedVars.contains(r.getDomain()) && distinguishedVars.contains(r.getRange())) {
            // two variables, both are distinguished
            String tableId = addPropertyPath(r.getURI(), tableId);


            //                bindVar(r.getDomain(), appendColumn(individualTableId, "uri"));

            String subjectTableId = null, objectTableId = null;
            if ((varBindings.getOneOf(r.getDomain())) == null) {
            subjectTableId = addTable("individuals");
            bindVar(r.getDomain(), appendColumn(tableId, "subject_id"));
            } else {
            subjectTableId = stripColumn(varBindings.getOneOf(r.getDomain()));
            }
            if ((varBindings.getOneOf(r.getRange())) == null) {
            objectTableId = addTable("individuals");
            bindVar(r.getRange(), appendColumn(tableId, "object"));
            } else {
            objectTableId = stripColumn(varBindings.getOneOf(r.getDomain()));
            }


            tableBindings.put(appendColumn(subjectTableId, "id"), appendColumn(tableId, "subject_id"));
            tableBindings.put(appendColumn(objectTableId, "uri"), appendColumn(tableId, "object"));

            } else if (isVar(r.getDomain()) && isVar(r.getRange()) && distinguishedVars.contains(r.getDomain())) {
            // two variables and domain is ndv, replace with concept expression NegExists-R
            String tableId = addTable("individuals");
            String expression = "exists-" + r.getURI();
            addClassPath(expression, tableId); // set the classpath for this table

            bindVar(r.getDomain(), appendColumn(tableId, "uri"));
            bindVarClass(r.getDomain(), tableId);
            } else if (isVar(r.getDomain()) && isVar(r.getRange()) && distinguishedVars.contains(r.getRange())) {
            // two variables and domain is ndv, replace with concept expression NegExists-R
            String tableId = addTable("individuals");
            String expression = "negexists-" + r.getURI();
            addClassPath(expression, tableId); // set the classpath for this table

            bindVar(r.getRange(), appendColumn(tableId, "uri"));
            bindVarClass(r.getRange(), tableId);
            } else {*/
            String tableId = addPropertyPath(r.getURI());
            //String subjectTableId = addTable("individuals");
            //String objectTableId  = addTable("individuals");

            if (isVar(r.getDomain())) {
//                    bindVar(r.getDomain(), appendColumn(tableId, "subject_id"));
                bindVar(r.getDomain(), appendColumn(tableId, "subject_id"));
            } else {
                bindURI(r.getDomain(), appendColumn(tableId, "subject_id"));
            }

            if (propertyTypes.get(r.getURI()).equals("d")) {
                if (isVar(r.getRange())) {
                    bindVar(r.getRange(), appendColumn(tableId, "object"));
                } else {
                    bindURI(r.getRange(), appendColumn(tableId, "object"));
                }
            } else {
                if (isVar(r.getRange())) {
                    bindVar(r.getRange(), appendColumn(tableId, "object_id"));
                } else {
                    bindURI(r.getRange(), appendColumn(tableId, "object_id"));
                }

                //tableBindings.put(appendColumn(objectTableId, "id"), appendColumn(tableId, "object_id"));
            }


            //tableBindings.put(appendColumn(subjectTableId, "id"), appendColumn(tableId, "subject_id"));

        }
    }

    private String stripColumn(String in) {
        int firstDot = in.indexOf('.');

        if (firstDot == -1) {
            return in;
        } else {
            return in.substring(0, firstDot);
        }
    }

    private String toSQL() throws Exception {
        prepClasses();
        prepProperties();

        /*  for (String what: foundAt.keySet()) {
        String where = foundAt.get(what);
        //String binding = varBindings.getOneOf(what);
        String tableId = addTable("ontologies");
        bindVar(where, tableId + ".uri");
        }*/

        //System.out.println("Building SQL");
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DISTINCT ");

        //if (this.distinct) {
        //    sql.append("DISTINCT ";
        //}

        Iterator boundVarI = resultVars.keySet().iterator();

        while (boundVarI.hasNext()) {
            String currentVar = (String) boundVarI.next();
            System.out.println(currentVar);
            // get a binding for the var
            String col = (String) varBindings.getOneOf(currentVar);
            if (col.endsWith("_id")) {
                String resultTableId = addTable("individuals");
                bindVar(currentVar, resultTableId + ".uri");
                col = resultTableId + ".uri";
            }

            sql.append(" " + col + " AS " + currentVar + ", "); // substring to remove the ?
            Column any = varColBinding.getOneOf(currentVar);
            query.addColumn(any.getTable(), any.getName());
        }




        sql.delete(sql.length() - 2, sql.length());
        sql.append("\nFROM ");

        int i = 0;
        for (String currentTable : tables) {
            i++;
            sql.append(currentTable + " as " + TABLE_IDENTIFIER + i + ", ");
        }
        i = 0;
        for (String currentTable : cpTables) {
            i++;
            sql.append("classpaths as " + CPATH_TABLE_IDENTIFIER + i + ", ");
        }
        i = 0;
        for (String currentTable : ppTables) {
            i++;
            sql.append("propertypaths as " + PPATH_TABLE_IDENTIFIER + i + ", ");
        }

        sql.delete(sql.length() - 2, sql.length()); // removes trailing ", "

        Iterator varI = varBindings.keySet().iterator();
        Iterator tabBindI = tableBindings.keySet().iterator();
        //Iterator<String> fuzzyI = fuzzyMap.keySet().iterator();

        sql.append("\nWHERE ");

        while (varI.hasNext()) {
            String currentVar = (String) varI.next();

            /* for each variable, we have to equate it to each column it is bound to
             * so for a conjunction of A(?x) and B(?x) we say that A.id = B.id
             * for A(?x), B(?x) and C(?x) we say that A.id = B.id and A.id = C.id
             * if the query is just for A(?x), the SELECT A.id as x from A is enough */
            Iterator tabI = varBindings.get((Object) currentVar).iterator();

            String baseTab = (String) tabI.next();
            while (tabI.hasNext()) {
                String cmpTab = (String) tabI.next();
                sql.append(baseTab + " = " + cmpTab + "\nAND ");
            }
        }

        for (String var : varColBinding.keySet()) {
            Iterator<Column> it = varColBinding.get(var).iterator();
            if (it.hasNext()) {
                Column baseCol = it.next();
                while (it.hasNext()) {
                    Column col = it.next();
                    query.addJoin(baseCol.getTable(), baseCol.getName(), col.getTable(), col.getName());
                }
            }
        }



        while (tabBindI.hasNext()) { // for each table binding
            String currentTab = (String) tabBindI.next();
            String currentTabVal = currentTab;
            //if (currentTab.charAt(0) != '"') {
            //    currentTabVal = "'" + currentTab.replaceAll("'", "\\\\'") + "'";
            //}

            /* for each variable, we have to equate it to each URI it is bound to
             * for A(x), B(y) and C(z) we say that A.id = 'x' and B.id = 'y' */
            Iterator tabI = tableBindings.get((Object) currentTab).iterator();

            //String baseTab = (String) tabI.next();
            while (tabI.hasNext()) {
                String cmpTab = (String) tabI.next();
                //if (cmpTab.charAt(0) != '"') {
                //    cmpTab = "'" + cmpTab.replaceAll("'", "\\\\'") + "'";
                //}
                sql.append(currentTabVal + " = " + cmpTab + "\nAND ");
            }
        }





        /* for each reference to a class or property, we have to ensure that the class
         * path is being correctly dealt with.
         * So if we have C(x), we need to equate the t1.class_id to p1.class_id, and then
         * p1.path LIKE 'thing%' */

        for (Map.Entry<String, String> e : classPathBindings.entrySet()) {
            sql.append(e.getKey() + ".path LIKE '" + e.getValue() + "%'\nAND ");
        }

        for (Map.Entry<String, String> e : propertyPathBindings.entrySet()) {
            sql.append(e.getKey() + ".path LIKE '" + e.getValue() + "%'\nAND ");
        }

        // if the last 4 characters are " AND", we need to remove them
        if (sql.toString().endsWith("\nAND ")) { // ick
            sql.delete(sql.length() - 4, sql.length());
        }

        if (sql.toString().endsWith("\nWHERE ")) { // double ick!
            sql.delete(sql.length() - 6, sql.length());
        }

        if (this.limit > 0) {
            sql.append(" LIMIT " + limit);
        }

        if (this.offset > 0) {
            sql.append(" OFFSET " + offset);
        }

        //

        System.out.println(sql);
        System.out.println(query);
        return sql.toString();
    }

    /**
     *
     * @param var
     */
    protected void addBoundVar(String var) {
        resultVars.put((Object) var, (Object) Boolean.TRUE);
        addDistinguishedVar(var);
        // System.out.println("pp" + var);
    }

    /**
     *
     * @param var
     */
    protected void addDistinguishedVar(String var) {
        distinguishedVars.add(var);
    }

    /**
     *
     * @param candidate
     * @return
     */
    protected boolean isVar(String candidate) {
        return (candidate.startsWith("?"));
    }

    private String appendColumn(String table, String column) {
        return (table + "." + column);
    }

    private void bindVar(String var, String column) {
        varBindings.put(var, column);
    }

    private void bindVarClass(String var, String column) {
        varClassBindings.put(var, column);
    }

    private void bindURI(String uri, String column) {
        tableBindings.put(String.valueOf(FNV.hash(removeQuotes(uri))), column);
    }

    private String removeQuotes(String in) {
        if (in != null) {
            if (in.charAt(0) == '"' && in.charAt(in.length() - 1) == '"') {
                return in.substring(1, in.length() - 1);
            }
        }
        return in;
    }

    private String incrementPath(String path) {
        if (path.length() > 0) {
            char last = path.charAt(path.length() - 1);
            last++;
            return path.substring(0, path.length() - 1) + last;
        }
        return path;
    }

    private Set<Node> findProxyNodes() {
        Set<Node> ns = new HashSet<Node>();
        Set<String> ndv = new HashSet<String>();

        for (Role r : properties) {
            if (isVar(r.getDomain()) && !resultVars.containsKey(r.getDomain())) {
                // r.domain is unbound
                // find all nodes which connect to this, these are proxies
                if (!ndv.contains(r.getDomain())) {
                    ndv.add(r.getDomain());
                }
            }

            if (isVar(r.getRange()) && !resultVars.containsKey(r.getRange())) {
                // r.domain is unbound
                // find all nodes which connect to this, these are proxies
                if (!ndv.contains(r.getRange())) {
                    ndv.add(r.getRange());
                }
            }
        }

        for (Role r : properties) {
            if (isVar(r.getDomain()) && ndv.contains(r.getDomain()) && !ndv.contains(r.getRange())) {
                // r.domain is unbound
                // find all nodes which connect to this, these are proxies
                Node n = new Node();
                n.end = End.RANGE;
                n.role = r;
                ns.add(n);
            }

            if (isVar(r.getRange()) && ndv.contains(r.getRange()) && !ndv.contains(r.getDomain())) {
                // r.domain is unbound
                // find all nodes which connect to this, these are proxies
                Node n = new Node();
                n.end = End.DOMAIN;
                n.role = r;
                ns.add(n);
            }
        }

        return ns;
    }

    class Node {

        public Role role;
        public End end;
    }

    class PropertyMetaData {

        public String path;
        public char type;
    }
}

enum End {

    DOMAIN, RANGE
}
