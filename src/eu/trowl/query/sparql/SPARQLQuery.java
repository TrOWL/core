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

package eu.trowl.query.sparql;

import eu.trowl.query.*;
import eu.trowl.vocab.*;
import eu.trowl.rdf.*;

import java.net.URI;
import java.util.*;

/**
 * A SPARQL select query.
 * 
 * @author sttaylor
 *
 */
public class SPARQLQuery extends Query {

    private final SelectClause selectClause = new SelectClause();
    private final FromClause fromClause = new FromClause();
    private final WhereClause whereClause = new WhereClause();
    /**
     *
     */
    protected ArrayList<Concept> concepts;
    /**
     *
     */
    protected ArrayList<Role> roles;
    private int queryType = 0;
    private URI baseURI = null;
    private boolean queryResultStar = false;
    /**
     *
     */
    public static final int TYPE_SELECT = 1;
    /**
     *
     */
    public static final int TYPE_ASK = 2;
    /**
     *
     */
    public static final int TYPE_DESCRIBE = 4;
    /**
     *
     */
    public static final int TYPE_CONSTRUCT = 8;
    private Map<String, URI> prefixes;

    /**
     *
     * @throws Exception
     */
    public SPARQLQuery() throws Exception {
        super();
        prefixes = new HashMap<String, URI>();
    }

    /**
     *
     * @param repo
     * @throws Exception
     */
    public SPARQLQuery(String repo) throws Exception {
        super(repo);
        prefixes = new HashMap<String, URI>();
    }

    /**
     *
     * @throws QueryTypeException
     */
    @SuppressWarnings("static-access")
    public void setQuerySelectType() throws QueryTypeException {
        this.queryType = SPARQLQuery.TYPE_SELECT;
    }

    /**
     *
     * @throws QueryTypeException
     */
    @SuppressWarnings("static-access")
    public void setQueryDescribeType() throws QueryTypeException {
        this.queryType = SPARQLQuery.TYPE_DESCRIBE;
    }

    /**
     *
     * @throws QueryTypeException
     */
    @SuppressWarnings("static-access")
    public void setQueryAskType() throws QueryTypeException {
        this.queryType = SPARQLQuery.TYPE_ASK;
    }

    /**
     *
     * @throws QueryTypeException
     */
    @SuppressWarnings("static-access")
    public void setQueryConstructType() throws QueryTypeException {
        this.queryType = SPARQLQuery.TYPE_CONSTRUCT;
    }

    /**
     *
     * @param val
     */
    public void setDistinct(boolean val) {
    }

    /**
     *
     * @param val
     */
    public void setReduced(boolean val) {
    }

    /**
     *
     * @param val
     */
    public void setQueryResultStar(boolean val) {
        this.queryResultStar = val;
    }

    /**
     *
     * @return
     */
    public SelectClause getSelectClause() {
        return selectClause;
    }

    /**
     *
     * @return
     */
    public FromClause getFromClause() {
        return fromClause;
    }

    /**
     *
     * @return
     */
    public WhereClause getWhereClause() {
        return whereClause;
    }

    /**
     *
     * @param name
     * @param uri
     */
    public void setPrefix(String name, URI uri) {
        //System.out.println("Set: " + name);

        this.prefixes.put(name, uri);
    }

    /**
     *
     * @param name
     * @return
     */
    public URI getPrefix(String name) {
        //System.out.println("Get: " + name);
        return this.prefixes.get(name);
    }

    /**
     *
     * @param uri
     */
    public void setBaseURI(URI uri) {
        this.baseURI = uri;
    }

    /**
     *
     * @throws QuerySyntaxException
     */
    public void process() throws QuerySyntaxException {
        for (Node v : selectClause.getVariables()) {
            this.addBoundVar(v.getVar());
        }

        for (SPARQLTriple t : whereClause.getBasicGraphPattern().getTriples()) {
            //System.out.println("Checking triple! " + t.toString());
            if (t.getPredicate().isVar()) {
                throw (new QuerySyntaxException("Variables not supported as object in rdf:type triples"));
            } else {

                if (t.isType()) {
                    if (t.getObject().isVar()) {
                        throw (new QuerySyntaxException("Variables not supported as object in rdf:type triples"));
                    }
                    //System.out.println ("Found Class in BGP");

                    this.classes.add(new Concept(t.getObject().toString(), t.getSubject()));
                } else if (t.isSubClass()) {
                    // Special case predicate: subClassOf
                } else if (t.isSubProperty()) {
                    // Special case predicate: subPropertyOf
                } else if (t.isFoundAt()) {
                    // Special case predicate: return ontology uri
                    if (t.getObject().isVar() && t.getSubject().isVar()) {
                        // yay
                        foundAt.put(t.getSubject().getVar(), t.getObject().getVar());
                    } else {
                        throw new QuerySyntaxException("trowl:foundAt must be part of a triple with two variables");

                    }
                } else {
//                    if (t.getPredicate().getURI())
                    //System.out.println ("Found Property in BGP");
                    this.properties.add(new Role(t.getPredicate().toString(), t.getSubject().toString(), t.getObject().toString()));
                    //if (t.getSubject().isVariable())
                    //    this.addDistinguishedVar(t.getSubject().toString());
                }
            }
        }
    }    // TODO add some convenience methods for select,from,where ...
}
