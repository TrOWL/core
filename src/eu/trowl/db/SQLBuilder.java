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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.HashMap;
import eu.trowl.hashing.FNV;
import eu.trowl.loader.TreeThing;
import eu.trowl.vocab.*;
import java.net.URI;

/**
 *
 * @author ethomas
 */
public class SQLBuilder {

    private Outputter out;
    private URI ontologyURI; // uri of current ontology
    private Long ontologyId;
    /**
     *
     */
    protected OntologyMeta m;

    /**
     *
     * @param db
     * @param uri
     * @param meta
     */
    public SQLBuilder(DB db, URI uri, OntologyMeta meta) {
        try {
                    System.out.println("connecting");
            db.connect();
                    System.out.println("connected");
            m = meta;
            out = new Outputter(db);
            ontologyURI = uri;
            ontologyId = FNV.hash(ontologyURI);
        } catch (Exception ex) {
            System.out.println("Error preparing statements for insert");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     *
     */
    public void init() {
        cleanup();
        createOntology(ontologyURI);
    }

    /**
     *
     * @param uri
     */
    public void setURI(URI uri) {
        this.ontologyURI = uri;
    }

    private void cleanup() {
//        System.out.println("Cleaning database...");
        String[] tables = Queries.TABLES;
        for (String t : tables) {
            if (!t.equals("ontologies")) {
                try {
                    out.db.execute("DELETE FROM " +t + " WHERE ontology=" + FNV.hash(ontologyURI));
                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }
            }
        }
        try {
            out.db.execute("DELETE FROM ontologies WHERE uri='" + (ontologyURI) + "'");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
//        System.out.println("Dropping indices");
        for (String q : Queries.DROP_INDEXES) {
            try {
                out.db.execute(q);
            } catch (Exception e) {
            }
        }
        System.out.println("Completed");
    }

    /**
     *
     * @param uri
     */
    public void createOntology(URI uri) {
        if (!m.getOntologies().contains(uri)) {
            Object[] args = {FNV.hash(uri), uri};
            out.execute(out.insertOntology, args);
            m.getOntologies().add(uri);
        }
    }

    /**
     *
     * @param uri
     */
    public void createClass(URI uri) {
        if (!m.getClasses().containsKey(uri)) {
            //System.out.println("found new class " + uri);
            Object[] args = {FNV.hash(uri), uri, ontologyId};
            out.execute(out.insertClass, args);
            m.getClasses().put(uri, new TreeThing(uri));
            m.getTopClasses().add(uri);
        }
    }

    /**
     *
     * @param uri
     */
    public void createProperty(URI uri) {
        if (!m.getProperties().containsKey(uri)) {
            Object[] args = {FNV.hash(uri), uri, ontologyId};
            out.execute(out.insertProperty, args);
            m.getProperties().put(uri, new TreeThing(uri));
            m.getTopProperties().add(uri);
        }
    }

    /**
     *
     * @param uri
     */
    public void createObjectProperty(URI uri) {
        createIndividual(uri, OWLRDF.OBJECT_PROPERTY);
        createProperty(uri);
    }

    /**
     *
     * @param uri
     */
    public void createDatatypeProperty(URI uri) {
        createIndividual(uri, OWLRDF.DATATYPE_PROPERTY);
    }

    /**
     *
     * @param propertyURI
     * @param domainURI
     */
    public void setDomain(URI propertyURI, URI domainURI) {
        createObjectPropertyInstance(propertyURI, RDFS.DOMAIN, domainURI);
    }

    /**
     *
     * @param propertyURI
     * @param rangeURI
     */
    public void setRange(URI propertyURI, URI rangeURI) {
        createObjectPropertyInstance(propertyURI, RDFS.RANGE, rangeURI);
    }

    /**
     *
     * @param uri
     */
    public void createInverseFunctionalProperty(URI uri) {
        createIndividual(uri, OWLRDF.INVERSE_FUNCTIONAL_PROPERTY);
    }

    /**
     *
     * @param uri
     */
    public void createFunctionalProperty(URI uri) {
        createIndividual(uri, OWLRDF.FUNCTIONAL_PROPERTY);
    }

    /**
     *
     * @param uri
     */
    public void createReflexiveProperty(URI uri) {
        createIndividual(uri, OWLRDF.REFLEXIVE_PROPERTY);
    }

    /**
     *
     * @param uri
     */
    public void createTransitiveProperty(URI uri) {
        createIndividual(uri, OWLRDF.TRANSITIVE_PROPERTY);
    }

    /**
     *
     * @param uri
     */
    public void createSymmetricProperty(URI uri) {
        createIndividual(uri, OWLRDF.SYMMETRIC_PROPERTY);
    }

    /**
     *
     * @param uri
     * @param path
     */
    public void createClassPath(URI uri, String path) {
        Object[] args = {FNV.hash(uri), path, ontologyId};
        out.execute(out.insertClassPath, args);
    }

    /**
     *
     * @param sup
     * @param sub
     */
    public void setSubClassOf(URI sub, URI sup) {
        createClass(sub);
        createClass(sup);
        /* Streaming Reasoner loveliness, slow as hell
        out.commitBatch(); // important to write what has been done so far to disk
        
        String subCP = out.db.getClassPath(sub);
        String supCP = out.db.getClassPath(sup);

        if (subCP == null) {
        this.createClassPath(sub, supCP + this.getPathHash(sup));
        }
        String newCP = supCP + this.getPathHash(sup);
        out.println("UPDATE classpaths SET path='" + newCP + "' || path WHERE path LIKE '" + subCP + "%'");
        out.commit();
         */
        m.getClasses().get(sup).addSubThing(m.getClasses().get(sub));
        m.getTopClasses().remove(sub); // not a top class any more :D
        //System.out.println(sub);
    }

    /**
     *
     */
    public void storePaths() {
        storeClassPaths();
        storePropertyPaths();
    }

    private void storePropertyPaths() {
        for (URI uri : m.getTopProperties()) {
            traversePropertyPaths(m.getProperties().get(uri), "");
        }
    }

    /**
     *
     * @param base
     * @param currentPath
     */
    public void traversePropertyPaths(TreeThing base, String currentPath) {
        if (base.getUri() != null) {

            currentPath = currentPath + getPathHash(base.getUri());
            createPropertyPath(base.getUri(), currentPath);

            /* Find the children and recurse */
            for (TreeThing child : base.getSubThings()) {
                if (child.getUri() != null) {
                    if (!child.equals(base) && !currentPath.contains(getPathHash(child.getUri()))) {
                        traversePropertyPaths(child, currentPath);
                    }
                }
            }
        }
    }
    int i = 0;

    private void storeClassPaths() {
        for (URI uri : m.getTopClasses()) {
            System.out.println("Top Class: " + uri);
            traverseClassPaths(m.getClasses().get(uri), "");
        }
    }

    /**
     *
     * @param base
     * @param currentPath
     */
    public void traverseClassPaths(TreeThing base, String currentPath) {
        if (base.getUri() != null) {
            String newPath = currentPath + getPathHash(base.getUri());
            createClassPath(base.getUri(), newPath);
            //System.out.println(base.getUri() + " " + newPath);
            /* Find the children and recurse */
            for (TreeThing child : base.getSubThings()) {
                if (child.getUri() != null) {
                    if (!child.equals(base) && !currentPath.contains(getPathHash(child.getUri()))) {
                        traverseClassPaths(child, newPath);
                    }
                }
            }
        }
    }

    /**
     *
     * @param sub
     * @param sup
     */
    public void setSubPropertyOf(URI sub, URI sup) {
        createProperty(sub);
        createProperty(sup);
        /*
        out.commitBatch(); // important to write what has been done so far to disk
        String subCP = out.db.getPropertyPath(sub);
        String supCP = out.db.getPropertyPath(sup);

        if (subCP == null) {
        this.createPropertyPath(sub, supCP + this.getPathHash(sup));
        }
        String newCP = supCP + this.getPathHash(sup) + subCP;
        out.println("UPDATE propertypaths SET path='" + newCP + "' WHERE path LIKE '" + subCP + "%'");
        out.commit();
         */
        m.getProperties().get(sup).addSubThing(m.getProperties().get(sub));
        m.getTopProperties().remove(sub); // not a top property any more :D
    }

    /**
     *
     * @param uri
     * @param path
     */
    public void createPropertyPath(URI uri, String path) {
        Object[] args = {FNV.hash(uri), path, ontologyId};
        out.execute(out.insertPropertyPath, args);
    }

    /**
     *
     * @param uri
     * @param classURI
     */
    public void createIndividual(URI uri, URI classURI) {
        Object[] args = {FNV.hash(uri), FNV.hash(classURI), uri, ontologyId};
        out.execute(out.insertIndividual, args);
        createClass(classURI);
    }

    /**
     *
     * @param subject
     * @param predicate
     * @param object
     */
    public void createObjectPropertyInstance(URI subject, URI predicate, URI object) {
        Object[] args = {FNV.hash(subject), FNV.hash(predicate), FNV.hash(object), ontologyId};
        out.execute(out.insertOPropertyInstance, args);
        createProperty(predicate);
    }

    /**
     *
     * @param subject
     * @param predicate
     * @param object
     * @param lang
     */
    public void createDatatypePropertyInstance(URI subject, URI predicate, String object, String lang) {
        Object[] args = {FNV.hash(subject), FNV.hash(predicate), object, lang, ontologyId};
        out.execute(out.insertDPropertyInstance, args);
        createProperty(predicate);
    }

    /**
     *
     */
    public void rebuildIndices() {
        try {
            out.executeAll();
        } catch (SQLException ex) {
                System.out.println("SQL Error Occured: " + ex.getMessage());

                System.out.println("Cause: " + ex.getNextException().getMessage());
                //   System.exit(2);
            }
        //System.out.println("Rebuilding indices");
        for (String q : Queries.CREATE_INDEXES) {
            try {
                //System.out.println(q);
                out.db.execute(q);
            } catch (SQLException ex) {
                System.out.println("Could not create index");
                System.out.println(ex.getMessage());
            }
        }
        //System.out.println("Complete");
    }

    /**
     *
     */
    public void close() {
        out.close();
    }

    /**
     *
     * @param uri
     * @return
     */
    public static String getPathHash(Object uri) {
        String hashed = FNV.hash32(uri);
        return hashed;
    }

    private class Outputter {

        private Map<PreparedStatement, Boolean> preparedStatements;
        private DB db;
        private static final int PER_BATCH = 10000;
        private int statementsExecuted = 0;
        private PreparedStatement insertIndividual;
        private PreparedStatement insertClass;
        private PreparedStatement insertOPropertyInstance;
        private PreparedStatement insertDPropertyInstance;
        private PreparedStatement insertProperty;
        private PreparedStatement insertOntology;
        private PreparedStatement insertPropertyPath;
        private PreparedStatement insertClassPath;
        private long executed = 0;

        public Outputter(DB db) throws SQLException {
            this.db = db;
            insertOntology = db.prepareStatement(Queries.INSERT_ONTOLOGY);
            insertIndividual = db.prepareStatement(Queries.INSERT_INDIVIDUAL);
            insertClass = db.prepareStatement(Queries.INSERT_CLASS);
            insertOPropertyInstance = db.prepareStatement(Queries.INSERT_OPROPERTY_INSTANCE);
            insertDPropertyInstance = db.prepareStatement(Queries.INSERT_DPROPERTY_INSTANCE);
            insertClassPath = db.prepareStatement(Queries.INSERT_CLASS_PATH);
            insertPropertyPath = db.prepareStatement(Queries.INSERT_PROPERTY_PATH);
            insertProperty = db.prepareStatement(Queries.INSERT_PROPERTY);

            preparedStatements = new HashMap<PreparedStatement, Boolean>();

            preparedStatements.put(insertOntology, false);
            preparedStatements.put(insertIndividual, false);
            preparedStatements.put(insertClass, false);
            preparedStatements.put(insertProperty, false);
            preparedStatements.put(insertPropertyPath, false);
            preparedStatements.put(insertOPropertyInstance, false);
            preparedStatements.put(insertDPropertyInstance, false);
            preparedStatements.put(insertClassPath, false);
        }

        public void execute(PreparedStatement s, Object[] args) {
            int i = 1;
            try {
                for (Object a : args) {
                    s.setObject(i++, a);
                }
                s.addBatch();

                preparedStatements.put(s, true);
                statementsExecuted++;

                if (statementsExecuted > PER_BATCH) {
                    //System.out.println(statementsExecuted);
                    statementsExecuted = 0;
                    executeAll();
                }


            } catch (SQLException ex) {
                System.out.println("SQL Error Occured: " + ex.getMessage());

                System.out.println("Cause: " + ex.getNextException().getMessage());
                //   System.exit(2);
            }

        }

        public void executeAll() throws SQLException {
            for (PreparedStatement p : preparedStatements.keySet()) {
                if (preparedStatements.get(p)) {
                    p.executeBatch();
                    p.clearBatch();
                    preparedStatements.put(p, false);
                }
            }
            executed++;
            //System.out.print("\r");
            //for (int i = 0; i < (executed % 80); i++) {
            //    System.out.print (".");
            //}
            System.out.println(executed * PER_BATCH);
        }

        public void close() {
            try {
                executeAll();
                db.commit();
            } catch (SQLException ex) {
                System.out.println("SQL Error Occured: " + ex.getMessage());
            }
        }
    }
}
