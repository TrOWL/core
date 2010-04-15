/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

package eu.trowl.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ed
 */
public abstract class DB {
    /**
     *
     */
    protected static final String DEFAULT_DATABASE = "trowl2_default";
    /**
     *
     */
    protected String dbDriver;
    /**
     *
     */
    protected String database;
    /**
     *
     */
    protected Connection dbCon;
    /**
     *
     */
    protected Statement stmt;

    /**
     *
     * @param repo
     */
    public DB(String repo) {
        this.database = "trowl2_" + repo;
    }

    /**
     *
     * @param repo
     */
    public void setRepository(String repo) {
        this.database = "trowl2_" + repo;
    }

    /**
     *
     */
    public DB() {
        this.database = DEFAULT_DATABASE;
    }

    /**
     *
     * @param dbName
     * @return
     * @throws Exception
     */
    public boolean connect(String dbName) throws Exception {
        database = dbName;
        return this.connect();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public abstract boolean connect() throws Exception;

    /**
     *
     */
    public void close() {
        if (dbCon != null) {
            try {
                dbCon.close();
            } catch (SQLException ignored) {
            }
        }
    }

    /**
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {

        dbCon.commit();

    }

    /**
     *
     * @param autocommit
     * @throws SQLException
     */
    public void setAutoCommit(boolean autocommit) throws SQLException {
        dbCon.setAutoCommit(autocommit);
    }

    /**
     *
     * @throws SQLException
     */
    public void rollback() throws SQLException {
        dbCon.rollback();
    }

    /**
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement s = dbCon.prepareStatement(sql);
        return s;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Statement createStatement() throws SQLException {
        Statement s = dbCon.createStatement();
        return s;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    protected Statement getStatement() throws SQLException {
        if (stmt == null)
            stmt = createStatement();

        return stmt;
    }


    /**
     *
     * @param s
     * @return
     * @throws SQLException
     */
    public int executeUpdate(String s) throws SQLException {
        int count = createStatement().executeUpdate(s);
        return count;
    }

    /**
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet execSQL(String sql) throws SQLException {
        Statement s = dbCon.createStatement();

        ResultSet rs = null;
        try {
            rs = s.executeQuery(sql);
        } catch (SQLException e) {

            throw e;
        }

        return rs;
    }

    /**
     *
     * @param s
     * @return
     * @throws SQLException
     */
    public boolean execute(String s) throws SQLException {
        //System.out.println("DEBUG: Executing SQL " + s);
        return getStatement().execute(s);
    }

    /**
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet execSQL(StringBuffer sql) throws SQLException {
        return execSQL(sql.toString());
    }

    /**
     *
     * @return
     */
    public String getDbDriver() {
        return this.dbDriver;
    }

    /**
     *
     * @param newValue
     */
    public void setDbDriver(String newValue) {
        this.dbDriver = newValue;
    }

    /**
     *
     */
    public void checkTables() {
        int i = 0;
        String[] types = {"TABLE"};
        List<String> tablesFound = new ArrayList<String>();
        try {
            ResultSet tabRS = dbCon.getMetaData().getTables(null, null, "%", types);
            while (tabRS.next()) {
                tablesFound.add(tabRS.getString("TABLE_NAME").toUpperCase());
            }
        } catch (SQLException ex) {
            System.err.print("Error checking tables exist");
        }

        for (String table: Queries.TABLES) {
            if (!tablesFound.contains(table.toUpperCase())) {
                try {
                    execute(Queries.CREATE_TABLES[i]);
                } catch (SQLException ex) {
System.err.println("Unable to create table: " + table);
ex.printStackTrace();
System.exit(1);
                }
            }
            i++;
        }
    }

    /**
     *
     * @param qry
     * @param param
     * @return
     * @throws SQLException
     */
    public List<String> queryAtomicList(String qry, String param) throws SQLException {
        PreparedStatement s = dbCon.prepareStatement(qry);
        s.setString(1, param);
        //System.out.print("Atomic Query: " + qry + ":" + param);
        ResultSet r = s.executeQuery();
        r.next();
        ArrayList<String> results = new ArrayList<String>();
        try {
          //  System.out.print("Atomic Query succeeded: " + r.getString(1));
            for (int i = 0; i < r.getMetaData().getColumnCount(); i ++) {
                results.add(r.getString(i + 1));
            }
        } catch (SQLException e) {
          //  System.out.print("Atomic Query Failed: " + e.getMessage());

        }
        return results;
    }

    /**
     *
     * @param qry
     * @param param
     * @return
     * @throws SQLException
     */
    public String queryAtomic(String qry, String param) throws SQLException {
        PreparedStatement s = dbCon.prepareStatement(qry);
        s.setString(1, param);
        //System.out.print("Atomic Query: " + qry + ":" + param);
        ResultSet r = s.executeQuery();
        r.next();
        try {
          //  System.out.print("Atomic Query succeeded: " + r.getString(1));
            return r.getString(1);
        } catch (SQLException e) {
          //  System.out.print("Atomic Query Failed: " + e.getMessage());
            return "";
        }
    }

    /**
     *
     * @param qry
     * @return
     * @throws SQLException
     */
    public String queryAtomic(String qry) throws SQLException {
        PreparedStatement s = this.prepareStatement(qry);
        //s.setString(1, param);
        //System.out.print("Atomic Query: " + qry);
        ResultSet r = s.executeQuery();
        r.next();
        try {
            return r.getString(1);
        } catch (SQLException e) {
            System.out.print("Atomic Query Failed: " + e.getMessage());
            return "";
        }
    }

    /**
     *
     * @param uri
     * @return
     * @throws SQLException
     */
    public String getClassPath(String uri) throws SQLException {
        return queryAtomic(Queries.GET_PATH_FOR_CLASS_URI, uri);
    }

    /**
     *
     * @param uri
     * @return
     * @throws SQLException
     */
    public String getPropertyPath(String uri) throws SQLException {
        return queryAtomic(Queries.GET_PATH_FOR_PROPERTY_URI, uri);
    }
}
    /*

    public void newClass(String classURI, String from) throws SQLException {
        if (!tableExists(classURI)) {
            // This class does not yet exist
            String sql = "CREATE TABLE \"" + classURI + "\" (id varchar(255) NOT NULL PRIMARY KEY, ont varchar(255), weight float DEFAULT 1.0 NOT NULL CHECK (weight <= 1.0))";

            this.execute(sql);
            newClassInstance("http://www.w3.org/2002/07/owl#Class", classURI, from);
        }
    }

    public void newClassInstance(String classURI, String instanceURI, String from) throws SQLException {
        newClassInstance(classURI, instanceURI, from, new Float(1.0));
    }

    public void newClassInstance(String classURI, String instanceURI, String from, Float weight) throws SQLException {
        if (!tableExists(classURI)) {
            newClass(classURI, from);
        }

        String sql = "INSERT INTO \"" + classURI + "\" (id, ont, weight) VALUES ('" + instanceURI + "', '" + from + "', " + weight.toString() + ")";
        try {
            this.execute(sql);
        } catch (SQLException ex) {
            // instance already exists in the database, we ignore this currently
            //e.printStackTrace();
        }
    }

    public String queryAtomic(String qry, String[] params) throws SQLException {
        PreparedStatement s = this.prepareStatement(qry);
        int i = 1;
        for (String p : params) {
            s.setString(i, p);
            i++;
        }
        ResultSet r = s.executeQuery();
        r.next();

        try {
            return r.getString(0);
        } catch (SQLException e) {
            return "";
        }
    }



/*
private void createIndexes(String table, String[] cols) throws SQLException {
for (int i = 0; i < cols.length; i++) {
execute("CREATE INDEX \"idx_" + MD5.hash(cols[i] + table) + "\" ON \"" + table + "\" (" + cols[i] + ");");
}
}

public void newProperty(String propertyURI, String domain, String range, String from) throws SQLException {
newProperty(propertyURI, domain, range, from, false);
}

public void newProperty(String propertyURI, String domain, String range, String from, boolean isDataTypeProperty) throws SQLException {
if (!tableExists(propertyURI)) {
int rangeSize = isDataTypeProperty ? 768 : 255;

// This property does not yet exist
String sql = "CREATE TABLE \"" + propertyURI + "\" (domain varchar(255) not null, range varchar(" + rangeSize + ") not null, ont varchar(255), weight float DEFAULT 1.0 NOT NULL CHECK (weight <= 1.0), PRIMARY KEY (domain, range))";
this.execute(sql);
createIndexes(propertyURI, new String[]{"domain", "range"});
newClassInstance("http://www.w3.org/2002/07/owl#ObjectProperty", propertyURI, from);
if (domain != null) {
newPropertyInstance("http://www.w3.org/2000/01/rdf-schema#domain", propertyURI, domain, from);
}

if (range != null) {
newPropertyInstance("http://www.w3.org/2000/01/rdf-schema#range", propertyURI, range, from);
}
}
}

public void newPropertyInstance(String propertyURI, String domain, String range, String from, Float weight) throws SQLException {
newPropertyInstance(propertyURI, domain, range, from, weight, false);
}

public void newPropertyInstance(String propertyURI, String domain, String range, String from, boolean isDataTypeProperty) throws SQLException {
newPropertyInstance(propertyURI, domain, range, from, new Float(1.0), isDataTypeProperty);
}

public void newPropertyInstance(String propertyURI, String domain, String range, String from) throws SQLException {
newPropertyInstance(propertyURI, domain, range, from, new Float(1.0));
}

public void newPropertyInstance(String propertyURI, String domain, String range, String from, Float weight, boolean isDataTypeProperty)
throws SQLException {
if (!tableExists(propertyURI)) {
newProperty(propertyURI, (String) null, (String) null, from, isDataTypeProperty);
}

// bleh...
if (isDataTypeProperty) {
range = range.replaceAll("'", "\\\\'");
}


String sql = "INSERT INTO \"" + propertyURI + "\" (domain, range, ont, weight) VALUES ('" + domain + "', '" + range + "', '" + from + "', " + weight.toString() + ")";
try {
this.execute(sql);
} catch (SQLException ex) {
// instance already exists in the database, we ignore this currently
//ex.printStackTrace();
}
}

public boolean tableExists(String tableName) {
//System.out.println ("Testing to check existance of " + tableName);

if (tables.containsKey(tableName)) {
return true;
}

try {
this.execSQL("select * from \"" + tableName + "\" where 1=0");
} catch (Exception e) {
// table does not exist or some other problem
return false;
}

//System.out.print ("Setting true " + tableName);
tables.put(tableName, new Boolean(true));
return true;
}

public boolean columnExists(String tableName, String columnName) {
if (columns.containsKey(tableName + "|" + columnName)) {
return true;
}

try {
this.execSQL("select \"" + columnName + "\" from \"" + tableName + "\" where 1=0");
} catch (Exception e) {
// column does not exist or some other problem
return false;
}

columns.put(tableName + "|" + columnName, new Boolean(true));
return true;
}

public ArrayList<String> getSubClasses(String uri) {
ArrayList<String> subs = new ArrayList<String>();
ResultSet r;
try {
r = execSQL("SELECT domain FROM \"http://www.w3.org/2000/01/rdf-schema#subClassOf\" where range='" + uri + "'");
while (r.next()) {
subs.add(r.getString("domain"));
}
} catch (SQLException ex) {
ex.printStackTrace();
}

return subs;
}

public void removeOntology(String ontologyURL) throws SQLException {
ResultSet rs = null;
PreparedStatement pstmt = null;
try {
pstmt = prepareStatement("DELETE FROM ? WHERE ont = ?;");
rs = execSQL("SELECT tablename FROM pg_tables;");

while (rs.next()) {
String table = rs.getString("tablename");

if (table.startsWith("http://")) {
pstmt.clearParameters();
pstmt.setString(1, table);
pstmt.setString(2, ontologyURL);
pstmt.addBatch();

System.out.println(pstmt.toString());
}
}

pstmt.executeBatch();
} catch (SQLException e) {
e.printStackTrace();
e.getNextException().printStackTrace();
throw e;
} finally {
try {
rs.close();
pstmt.close();
} catch (Exception ignored) {
}
}
}

public void removeClassInstance(String classURI, String instanceURI) {
try {
execute("DELETE FROM \"" + classURI + "\" WHERE id = '" + instanceURI + "';");
execute("DELETE FROM \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" WHERE domain = '" + instanceURI + "' AND range = '" + classURI + "';");
} catch (SQLException ex) {
ex.printStackTrace();
}
}

public void removeClass(String uri) {
try {
removeClassInstance("http://www.w3.org/2002/07/owl#Class", uri);
execute("DELETE FROM \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" WHERE range = '" + uri + "';");
execute("DELETE FROM \"http://www.w3.org/2000/01/rdf-schema#subClassOf\" WHERE domain = '" + uri + "' OR range = '" + uri + "';");
execute("DROP TABLE \"" + uri + "\";");
} catch (SQLException ex) {
ex.printStackTrace();
}
}

public void removeProperty(String uri) {
try {
removeClassInstance("http://www.w3.org/2002/07/owl#ObjectProperty", uri);
execute("DROP TABLE \"" + uri + "\";");
} catch (SQLException ex) {
// Ignore for just now.
}
}

public void removePropertyInstance(String uri, String domain, String range) {
try {
execute("DELETE FROM \"" + uri + "\" WHERE domain = '" + domain + "' AND range = '" + range + "';");
} catch (SQLException ex) {
// Ignore for just now.
}
}

public Set<String> fetchPropertyRange(String property) throws SQLException {
Set<String> classes = new HashSet<String>();

ResultSet rs = execSQL("SELECT range FROM \"" + RDFS.range + "\" where domain = '" + property + "'");

while (rs.next()) {
classes.add(rs.getString("range"));
}

return classes;
}

public Set<String> fetchPropertyDomain(String property) throws SQLException {
Set<String> classes = new HashSet<String>();

ResultSet rs = execSQL("SELECT range FROM \"" + RDFS.domain + "\" where domain = '" + property + "'");

while (rs.next()) {
classes.add(rs.getString("range"));
}

return classes;
}

public boolean isObjectProperty(String property) {
ResultSet rs = null;
try {
rs = execSQL("select * from \"http://www.w3.org/2002/07/owl#ObjectProperty\" where id = '" + property + "'");
return rs.next();
} catch (Exception e) {
return false;
} finally {
if (rs != null) {
try {
rs.close();
} catch (SQLException ignored) {
}
}
}
}
}
//	public ResultSet doQuery(String qry) {
//		ResultSet rs = null;
//
//		try {
//			// use the DbBean to make the connection to the database
//			// now all db connections can be routed through the one place
//			connect();
//			rs = execSQL(new StringBuffer(qry));
//		} catch (ClassNotFoundException e) {
//			System.out.println("ClassNotFound: " + e.getMessage());
//		} catch (SQLException e) {
//			System.out.println("SQLException: " + e.getMessage());
//		}
//		// uncomment these two catches if using mysql
//		catch (InstantiationException e) {
//			System.out.println("InstantiationException: " + e.getMessage());
//		} catch (IllegalAccessException e) {
//			System.out.println("IllegalAccessException: " + e.getMessage());
//		}
//
//		return rs;
//	}
/*public void newDatatypeProperty (String propertyURI, String domain, String range, String from) throws SQLException {
if (!tableExists(propertyURI)) {
// This property does not yet exist
String sql = "CREATE TABLE \"" + propertyURI + "\" (uid serial primary key, domain varchar(255) not null, range varchar(255) not null, ont varchar(255), UNIQUE (domain, range))";
this.execute(sql);
newClassInstance("http://www.w3.org/2002/07/owl#DatatypeProperty", propertyURI, from);
if (domain != null) {
newPropertyInstance("http://www.w3.org/2000/01/rdf-schema#domain", propertyURI, domain, from);
}

if (range != null) {
newPropertyInstance("http://www.w3.org/2000/01/rdf-schema#range", propertyURI, range, from);
}
}
}

public void newDatatypePropertyInstance(String classURI, String propertyURI, String instanceURI, String value, String from) throws SQLException{
if (!tableExists(propertyURI)) {
newProperty (propertyURI, (String) null, (String) null, from);
}

String sql = "INSERT INTO \"" + propertyURI + "\" (domain, range, ont) VALUES ('" + domain + "', '" + range + "', '" + from + "')";
try {
this.execute(sql);
} catch (SQLException ex) {
// instance already exists in the database, we ignore this currently
}
}*/
