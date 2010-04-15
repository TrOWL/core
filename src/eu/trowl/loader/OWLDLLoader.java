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

package eu.trowl.loader;

import eu.trowl.db.*;

import eu.trowl.owl.OntologyLoadException;
import eu.trowl.owl.Reasoner;
import eu.trowl.owl.ReasonerFactory;
import eu.trowl.vocab.OWLRDF;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;

import java.util.Set;
import java.util.ArrayList;
import java.net.URI;
import java.util.Map;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.ReaderInputSource;

/**
 *
 * @author ethomas
 */
public class OWLDLLoader extends Loader {
    /**
     *
     */
    public static final String ID = "TROWL_LOADER_OWLDL";
    /**
     *
     */
    public static final boolean THREAD_SAFE = false;

    private Reasoner reasoner;
    private OWLOntologyManager manager;
    private OWLOntology ont;
    private ArrayList<OWLClass> classes;
    private ArrayList<OWLObjectProperty> objectProperties;
    private OWLClass nothing;

    /**
     *
     * @throws Exception
     */
    public OWLDLLoader() throws Exception {
        super();
    }

    /**
     *
     * @throws LoaderInitException
     */
    @SuppressWarnings("static-access")
    public void init() throws LoaderInitException {
        try {
            manager = OWLManager.createOWLOntologyManager();
            OWLOntologyInputSource readerInput = new ReaderInputSource(in);
            ont = manager.loadOntology(readerInput);
            nameRestrictions();
            objectProperties = new ArrayList<OWLObjectProperty>();
            ReasonerFactory rf = new ReasonerFactory();
            reasoner = rf.load(manager);
            // turn off all logging (for speed)
            nothing = manager.getOWLDataFactory().getOWLNothing();
            
        } catch (OntologyLoadException ex) {
            LoaderInitException e = new LoaderInitException();
            e.initCause(ex);
            throw (e);
        } catch (URISyntaxException ex) {
            LoaderInitException e = new LoaderInitException();
            e.initCause(ex);
            throw (e);
        } catch (OWLOntologyChangeException ex) {
            LoaderInitException e = new LoaderInitException();
            e.initCause(ex);
            throw (e);
        } catch (OWLOntologyCreationException ex) {
            LoaderInitException e = new LoaderInitException();
            e.initCause(ex);
            throw (e);
        }
    }

    public void run() {
        go(0); // default of no completeness
    }

    /**
     *
     * @param completenessLevels
     */
    public void go(int completenessLevels) {
        try {
            reasoner.classify();
            if (!reasoner.isConsistent(ont)) {
                //throw new Exception("Submitted ontology is not consistent");
            }
            getClasses(out, manager.getOWLDataFactory().getOWLThing());
            getObjectProperties(out);
            getDatatypeProperties(out);
            getInstances(out);
            getPropertyInstances(out);
            if (completenessLevels > 1) {
                getCompleteness(completenessLevels);
            }
            stopTimer();
        } catch (OWLReasonerException ex) {
            Logger.getLogger(OWLDLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @throws URISyntaxException
     * @throws OWLOntologyChangeException
     */
    @SuppressWarnings("static-access")
    public void nameRestrictions() throws URISyntaxException, OWLOntologyChangeException {
        //OWLObjectSomeRestriction r;
        OWLDataFactory factory = manager.getOWLDataFactory();
        //Set<OWLAxiom> axes = new HashSet();

        /*for (OWLClass c : ont.getReferencedClasses()) {
        for (OWLSubClassAxiom ax : ont.getSubClassAxiomsForLHS(c)) {
        OWLDescription superCls = ax.getSuperClass();
        if (superCls.getClass() == OWLObjectSomeRestriction.class) {
        r = (OWLObjectSomeRestriction) superCls;
        URI newClassURI = new URI("urn:class:" + makeHash(r.getProperty().asOWLObjectProperty().getURI().toString()));
        OWLClass newClass = factory.getOWLClass(newClassURI);
        OWLAxiom newAxiom = factory.getOWLEquivalentClassesAxiom(newClass, superCls);
        axes.add(newAxiom);
        }
        }
        }*/

        for (OWLObjectProperty o : ont.getReferencedObjectProperties()) {
            @SuppressWarnings("static-access")
            URI newClassURI = new URI("urn:class:" + SQLBuilder.getPathHash(o.getURI()));
            OWLClass newClass = factory.getOWLClass(newClassURI);
            manager.addAxiom(ont, factory.getOWLEquivalentClassesAxiom(factory.getOWLObjectSomeRestriction(o, factory.getOWLThing()), newClass));

            newClassURI = new URI("urn:class:" + SQLBuilder.getPathHash("neg:" + o.getURI().toString()));
            newClass = factory.getOWLClass(newClassURI);

            manager.addAxiom(ont, factory.getOWLEquivalentClassesAxiom(factory.getOWLObjectSomeRestriction(o.getInverseProperty(), factory.getOWLThing()), newClass));
        }
    }

    /**
     *
     * @param sql
     * @param cur
     */
    public void getClasses(SQLBuilder sql, OWLClass cur) {
        try {
//        for (OWLClass c : reasoner.getClasses()) {
            sql.createClass(cur.getURI());
            for (Set<OWLClass> subs : reasoner.getSubClasses(cur)) {
                for (OWLClass sub : subs) {
                    if (!sub.equals(cur) && !sub.equals(nothing)) {
                        sql.setSubClassOf(cur.getURI(), sub.getURI());
                        getClasses(sql, sub);
                    }
                }
            }
        } catch (OWLReasonerException ex) {
            Logger.getLogger(OWLDLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    public void finish() {
        try {
            reasoner.dispose();
        } catch (OWLReasonerException ex) {
            Logger.getLogger(OWLDLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.storePaths();
        out.rebuildIndices();
        out.close();
    }

    /**
     *
     * @param sql
     */
    public void getObjectProperties(SQLBuilder sql) {
        for (OWLObjectProperty p : ont.getReferencedObjectProperties()) {
            try {
                sql.createProperty(p.getURI());
                for (Set<OWLObjectProperty> subs : reasoner.getSubProperties(p)) {
                    for (OWLObjectProperty sub : subs) {
                        sql.setSubPropertyOf(p.getURI(), sub.getURI());
                    }
                }
            } catch (OWLReasonerException ex) {
                Logger.getLogger(OWLDLLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param sql
     */
    public void getDatatypeProperties(SQLBuilder sql) {
        for (OWLDataProperty p : ont.getReferencedDataProperties()) {
            try {
                sql.createProperty(p.getURI());
                for (Set<OWLDataProperty> subs : reasoner.getSubProperties(p)) {
                    for (OWLDataProperty sub : subs) {
                        sql.setSubPropertyOf(p.getURI(), sub.getURI());
                    }
                }
            } catch (OWLReasonerException ex) {
                Logger.getLogger(OWLDLLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getInstances(SQLBuilder sql) {
        for (OWLClass c : reasoner.getClasses()) {
            try {
                for (OWLIndividual i : reasoner.getIndividuals(c, true)) {
                    sql.createIndividual(i.getURI(), c.getURI());
                }
            } catch (OWLReasonerException ex) {
                Logger.getLogger(OWLDLLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void getObjectPropertyInstancesNoReasoner(SQLBuilder sql) throws Exception {
        for (OWLIndividual subject : ont.getReferencedIndividuals()) {
            for (OWLObjectPropertyAssertionAxiom a : ont.getObjectPropertyAssertionAxioms(subject)) {
                OWLObjectProperty property = a.getProperty().getNamedProperty();
                OWLIndividual object = a.getObject();

                sql.createObjectPropertyInstance(subject.getURI(), property.getURI(), object.getURI());
            }
        }
    }

    private void getPropertyInstances(SQLBuilder sql) {
        for (OWLIndividual subject : reasoner.getIndividuals()) {

            try {
                Map<OWLDataProperty, Set<OWLConstant>> propertyMap = reasoner.getDataPropertyRelationships(subject);
                for (OWLDataProperty predicate : propertyMap.keySet()) {
                    for (OWLConstant object : propertyMap.get(predicate)) {
                        sql.createDatatypePropertyInstance(subject.getURI(), predicate.getURI(), object.getLiteral(), "");
                    }
                }
            } catch (OWLReasonerException ex) {
                //log.info(ex);
            }

            try {
                Map<OWLObjectProperty, Set<OWLIndividual>> propertyMap = reasoner.getObjectPropertyRelationships(subject);
                for (OWLObjectProperty predicate : propertyMap.keySet()) {
                    for (OWLIndividual object : propertyMap.get(predicate)) {
                        sql.createObjectPropertyInstance(subject.getURI(), predicate.getURI(), object.getURI());
                    }
                }
            } catch (OWLReasonerException ex) {
                //log.info(ex);
            }
        }
    }

    private void getCompleteness(int level) {
        for (OWLObjectProperty predicate : reasoner.getObjectProperties()) {
            System.out.println("checking :" + predicate.getURI().toString());
            followRole(level, 0, predicate, manager.getOWLDataFactory().getOWLThing(), "T");
        }

        for (OWLObjectProperty predicate : reasoner.getObjectProperties()) {
            System.out.println("checking :" + predicate.getURI().toString());
            followRole(level, 0, manager.getOWLDataFactory().getOWLObjectPropertyInverse(predicate), manager.getOWLDataFactory().getOWLThing(), "T");
        }
    }

    private void followRole(int maxLevel, int level, OWLObjectPropertyExpression role, OWLDescription stem, String name) {
        level = level + 1;
        if (level <= maxLevel) {
            OWLDescription newStem = manager.getOWLDataFactory().getOWLObjectSomeRestriction(role, stem);
            OWLObjectProperty namedRole = null;
            try {
                namedRole = role.asOWLObjectProperty();
            } catch (Exception e) {
                try {
                    namedRole = ((OWLObjectPropertyInverse) role).getInverse().asOWLObjectProperty();
                } catch (Exception ee) {
                    // this just means there is no inverse possible
                }
            }

            Set<OWLIndividual> is;
            try {
                if (reasoner.isSatisfiable(newStem)) {
                    is = reasoner.getIndividuals(newStem, false);
                    String fname = namedRole.getURI().getFragment() + "." + name;

                    if (!is.isEmpty()) {
                        for (OWLIndividual i : is) {
                            //out.createIndividual(i.getURI(), fname);
                        }

                        for (OWLObjectProperty predicate : reasoner.getObjectProperties()) {
                            followRole(maxLevel, level, predicate, newStem, fname);
                        }
                    }
                }

                newStem = manager.getOWLDataFactory().getOWLObjectSomeRestriction(manager.getOWLDataFactory().getOWLObjectPropertyInverse(role), stem);

                if (reasoner.isSatisfiable(newStem)) {
                    is = reasoner.getIndividuals(newStem, false);
                    String fname = namedRole.getURI().getFragment() + "-." + name;
                    System.out.println(fname);
                    if (!is.isEmpty()) {
                        for (OWLIndividual i : is) {
                            System.out.println("  : " + i.getURI());
                        }

                        for (OWLObjectProperty predicate : reasoner.getObjectProperties()) {
                            followRole(maxLevel, level, predicate, newStem, fname);
                        }
                    }
                }
            } catch (Exception e) {
                // this happens on the Wine ontology for reasons I cannot explain
                // I suspect a bug in pellet
                System.out.println("Pellet choked, continuing...");
            }
        }
    }
}