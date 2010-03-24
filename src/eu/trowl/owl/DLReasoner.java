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
package eu.trowl.owl;

import aterm.ATermAppl;
import com.clarkparsia.explanation.PelletExplanation;
import eu.trowl.db.DB;
import eu.trowl.db.DBFactory;
import eu.trowl.db.OntologyMeta;
import eu.trowl.db.SQLBuilder;
import eu.trowl.hashing.FNV;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindswap.pellet.KnowledgeBase;
import org.mindswap.pellet.owlapi.PelletLoader;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyInverse;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLProperty;
import org.semanticweb.owl.model.OWLPropertyExpression;
import org.semanticweb.owl.model.OWLRuntimeException;

/**
 *
 * @author ed
 */
public class DLReasoner extends ReasonerBase implements Reasoner {

    private org.mindswap.pellet.owlapi.Reasoner pellet;
    private OWLOntologyManager manager;
    private OWLOntology ont;
    private boolean dirty = false;
    private PelletExplanation explainer = null;

    public void load(OWLOntologyManager man) {
        PelletExplanation.setup();
        manager = man;
        org.mindswap.pellet.owlapi.PelletReasonerFactory fact = new org.mindswap.pellet.owlapi.PelletReasonerFactory();
        pellet = fact.createReasoner(manager);
        pellet.loadOntologies(manager.getImportsClosure(manager.getOntologies().iterator().next()));
        //pellet = new org.mindswap.pellet.owlapi.Reasoner(manager);

        if (pellet.isConsistent())
            pellet.classify();

        //} catch (URISyntaxException ex) {
        //    Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        //} catch (OWLOntologyChangeException ex) {
        //    Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);

    }

    public void store() {
        DB db;
        //nameRestrictions();
        try {
            db = DBFactory.construct();
            store(db);
        } catch (InstantiationException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void store(String repository) {
        DB db;

        try {
            db = DBFactory.construct(repository);
            store(db);
        } catch (InstantiationException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void storeNegative() {
        DB db;

        try {
            db = DBFactory.construct();
            storeNegative(db);
        } catch (InstantiationException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void storeNegative(String repository) {
        DB db;

        try {
            db = DBFactory.construct(repository);
            storeNegative(db);
        } catch (InstantiationException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void storeNegative(DB repository) {
        for (OWLClass c : pellet.getClasses()) {
            try {
                URI invUri = new URI("urn:class:inverse:" + c.getURI());
                OWLClass inv = manager.getOWLDataFactory().getOWLClass(null);
                OWLAxiom ax = manager.getOWLDataFactory().getOWLDisjointClassesAxiom(c, inv);
                manager.addAxiom(ont, ax);
                dirty = true;
            } catch (OWLOntologyChangeException ex) {
                Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        reload();

        store(repository);
    }

    public Set<OWLClass> classifyIndividual(OWLIndividual i) {
        if (!pellet.isClassified())
            pellet.classify();
        return flattenSetOfSets(pellet.getTypes(i, true));
    }

    public void store(DB repository) {
        if (dirty) {
            reload();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean consistent() {
        if (dirty) {
            reload();
        }
        return pellet.isConsistent();
    }

    public boolean allSatisfiable() {
        if (dirty) {
            reload();
        }
        return getUnsatisfiable().isEmpty();
    }

    public Set<OWLClass> getUnsatisfiable() {
        if (dirty) {
            reload();
        }

        Set<OWLClass> unsatisfiable = new HashSet<OWLClass>();
        for (OWLClass c : pellet.getClasses()) {
            if (!pellet.isSatisfiable(c)) {
                unsatisfiable.add(c);
            }
        }
        return unsatisfiable;
    }

    public Set<OWLIndividual> getInstances(OWLDescription c) {
        if (dirty) {
            reload();
        }

        return pellet.getIndividuals(c, true);
    }

    /**
     *
     * @param c
     * @return
     */
    public Set<OWLIndividual> getAllInstances(OWLClass c) {
        if (dirty) {
            reload();
        }
        return pellet.getIndividuals(c, false);
    }

    public boolean subsumes(OWLClass superclass, OWLClass subclass) {
        if (dirty) {
            reload();
        }
        return this.getSubClasses(superclass).contains(subclass);
    }

    /**
     *
     * @param p
     * @return
     */
    public Set<OWLObjectProperty> getSubObjectProperties(OWLObjectProperty p) {
        if (dirty) {
            reload();
        }

        return flattenSetOfSets(pellet.getDescendantProperties(p));
    }

    public Set<OWLClass> getDirectSubClasses(OWLDescription c) {
        if (dirty) {
            reload();
        }

        return flattenSetOfSets(pellet.getSubClasses(c));
    }

    public Set<OWLIndividual> getDirectInstances(OWLDescription c) {
        if (dirty) {
            reload();
        }
        return pellet.getIndividuals(c, true);
    }

    private void closeDirect(OWLClass c) {
        try {
            OWLClass toClose = c;
            Set<OWLIndividual> individuals = pellet.getIndividuals(c, false);

            OWLDescription nominal = manager.getOWLDataFactory().getOWLObjectOneOf(individuals);
            OWLAxiom axiom = manager.getOWLDataFactory().getOWLEquivalentClassesAxiom(toClose, nominal);
            manager.addAxiom(ont, axiom);
            dirty = true;
        } catch (OWLOntologyChangeException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeTree(OWLClass c) {
        for (OWLClass toClose : flattenSetOfSets(this.getSubClasses(c))) {
            closeDirect(toClose);
        }
    }

    public void close(OWLClass c) {
        closeDirect(c);
    }

    public void close(OWLObjectProperty p) {
        closeDirect(p);
    }

    public void closeTree(OWLObjectProperty p) {
        for (OWLObjectProperty toClose : this.getSubObjectProperties(p)) {
            closeDirect(toClose);
        }
    }

    private void closeDirect(OWLObjectProperty p) {
        OWLDataFactory fact = manager.getOWLDataFactory();
        try {
            OWLObjectProperty op = p;
            Map<OWLIndividual, Set<OWLIndividual>> instances = pellet.getObjectPropertyAssertions(op);
            for (OWLIndividual subject : instances.keySet()) {
                OWLDescription rhs = fact.getOWLObjectOneOf(instances.get(subject));
                OWLDescription nominal = fact.getOWLObjectOneOf(subject);
                OWLDescription lhs = fact.getOWLObjectSomeRestriction(op, nominal);
                OWLAxiom ax = fact.getOWLEquivalentClassesAxiom(lhs, rhs);
                manager.addAxiom(ont, ax);
                dirty = true;
            }
        } catch (OWLOntologyChangeException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reload() {
        pellet.dispose();
        pellet = new org.mindswap.pellet.owlapi.Reasoner(manager);
        dirty = false;
    }

    // Semantic Approximation functions follow
    /**
     *
     * @param db
     */
    public void go(DB db) {
        Set<OWLOntology> importsClosure = manager.getImportsClosure(ont);

        pellet.loadOntologies(importsClosure);

        if (!pellet.isClassified()) {
            pellet.classify();
        }

        if (!pellet.isConsistent(ont)) {
            //throw new Exception("Submitted ontology is not consistent");
        }

        OntologyMeta meta = new OntologyMeta();
        SQLBuilder out = new SQLBuilder(db, ont.getURI(), meta);


        getClasses(out, manager.getOWLDataFactory().getOWLThing());
        getObjectProperties(out);
        getDatatypeProperties(out);

        getInstances(out);
        getObjectPropertyInstances(out);
        getDatatypePropertyInstances(out);

        out.storePaths();
        out.rebuildIndices();
        out.close();
    }

    /**
     *
     * @throws URISyntaxException
     * @throws OWLOntologyChangeException
     */
    public void nameRestrictions() throws URISyntaxException, OWLOntologyChangeException {
        //OWLObjectSomeRestriction r;
        OWLDataFactory factory = manager.getOWLDataFactory();

        for (OWLObjectProperty o : ont.getReferencedObjectProperties()) {
            URI newClassURI = new URI("urn:class:" + FNV.hash32(o.getURI()));
            OWLClass newClass = factory.getOWLClass(newClassURI);
            manager.addAxiom(ont, factory.getOWLEquivalentClassesAxiom(factory.getOWLObjectSomeRestriction(o, factory.getOWLThing()), newClass));

            newClassURI = new URI("urn:class:" + FNV.hash32("neg:" + o.getURI()));
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

//        for (OWLClass c : pellet.getClasses()) {
        sql.createClass(cur.getURI());
        for (Set<OWLClass> subs : pellet.getSubClasses(cur)) {
            for (OWLClass sub : subs) {
                if (!sub.equals(cur) && !sub.equals(manager.getOWLDataFactory().getOWLNothing())) {
                    sql.setSubClassOf(cur.getURI(), sub.getURI());
                    getClasses(sql, sub);
                }
            }
        }
    }

    /**
     *
     * @param sql
     */
    public void getObjectProperties(SQLBuilder sql) {
        for (OWLObjectProperty p : ont.getReferencedObjectProperties()) {
            sql.createProperty(p.getURI());
            for (Set<OWLObjectProperty> subs : pellet.getSubProperties(p)) {
                for (OWLObjectProperty sub : subs) {
                    sql.setSubPropertyOf(p.getURI(), sub.getURI());
                }
            }
        }
    }

    /**
     *
     * @param sql
     */
    public void getDatatypeProperties(SQLBuilder sql) {
        for (OWLDataProperty p : ont.getReferencedDataProperties()) {
            sql.createProperty(p.getURI());
            for (Set<OWLDataProperty> subs : pellet.getSubProperties(p)) {
                for (OWLDataProperty sub : subs) {
                    sql.setSubPropertyOf(p.getURI(), sub.getURI());
                }
            }
        }
    }

    private void getInstances(SQLBuilder sql) {
        for (OWLClass c : pellet.getClasses()) {
            for (OWLIndividual i : pellet.getIndividuals(c, true)) {
                sql.createIndividual(i.getURI(), c.getURI());
            }
        }
    }

    private void getObjectPropertyInstances(SQLBuilder sql) {
        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
            Map<OWLIndividual, Set<OWLIndividual>> propertyMap = pellet.getObjectPropertyAssertions(predicate);

            for (Map.Entry<OWLIndividual, Set<OWLIndividual>> line : propertyMap.entrySet()) {
                OWLIndividual subject = line.getKey();
                for (OWLIndividual object : line.getValue()) {
                    sql.createObjectPropertyInstance(subject.getURI(), predicate.getURI(), object.getURI());
                }
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

    private void getDatatypePropertyInstances(SQLBuilder sql) {
        for (OWLDataProperty predicate : pellet.getDataProperties()) {
            Map<OWLIndividual, Set<OWLConstant>> propertyMap = pellet.getDataPropertyAssertions(predicate);

            for (Map.Entry<OWLIndividual, Set<OWLConstant>> line : propertyMap.entrySet()) {
                OWLIndividual subject = line.getKey();
                for (OWLConstant object : line.getValue()) {
                    sql.createDatatypePropertyInstance(subject.getURI(), predicate.getURI(), object.getLiteral(), "");
                }
            }
        }
    }

    private void getCompleteness(int level, SQLBuilder out) {
        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
            System.out.println("checking :" + predicate.getURI());
            followRole(level, 0, predicate, manager.getOWLDataFactory().getOWLThing(), "T", out);
        }

        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
            System.out.println("checking :" + predicate.getURI());
            followRole(level, 0, manager.getOWLDataFactory().getOWLObjectPropertyInverse(predicate), manager.getOWLDataFactory().getOWLThing(), "T", out);
        }
    }

    private void followRole(int maxLevel, int level, OWLObjectPropertyExpression role, OWLDescription stem, String name, SQLBuilder out) {
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
                if (pellet.isSatisfiable(newStem)) {
                    is = pellet.getIndividuals(newStem, false);
                    String fname = namedRole.getURI().getFragment() + "." + name;
                    System.out.println(fname);
                    if (!is.isEmpty()) {
                        for (OWLIndividual i : is) {
//                            out.createIndividual(i.getURI(), fname);
                        }

                        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
                            followRole(maxLevel, level, predicate, newStem, fname, out);
                        }
                    }
                }

                newStem = manager.getOWLDataFactory().getOWLObjectSomeRestriction(manager.getOWLDataFactory().getOWLObjectPropertyInverse(role), stem);

                if (pellet.isSatisfiable(newStem)) {
                    is = pellet.getIndividuals(newStem, false);
                    String fname = namedRole.getURI().getFragment() + "-." + name;
                    System.out.println(fname);
                    if (!is.isEmpty()) {
                        for (OWLIndividual i : is) {
                            System.out.println("  : " + i.getURI());
                        }

                        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
                            followRole(maxLevel, level, predicate, newStem, fname, out);
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

    public boolean isSatisfiable(OWLDescription c) {
        return pellet.isSatisfiable(c);
    }

    /**
     *
     * @param ax
     * @return
     */
    public Set<OWLAxiom> justify(OWLAxiom ax) {
        if (pellet.isEntailed(ax)) {
            return pellet.getExplanation();
        } else {
            return null;
        }
    }

    /**
     *
     * @param axes
     * @return
     */
    public Set<OWLAxiom> justify(Set<OWLAxiom> axes) {
        if (pellet.isEntailed(axes)) {
            return pellet.getExplanation();
        } else {
            return null;
        }
    }

    private void setupExplanation() {
        explainer = new PelletExplanation(pellet);
    }

    /**
     *
     * @param c
     * @return
     */
    public Set<OWLAxiom> justifySatisfiable(OWLDescription c) {
        if (explainer == null) {
            setupExplanation();
        }

        if (!pellet.isSatisfiable(c)) {
            try {
                return explainer.getUnsatisfiableExplanation(c);
            } catch (OWLRuntimeException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public Class promoteTo() {
        return null;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public Set<OWLAxiom> justifyInconsistent() {
        if (!pellet.isConsistent()) {
            if (explainer == null) {
                setupExplanation();
            }
            return explainer.getInconsistencyExplanation();
        } else {
            return null;
        }
    }

    public Set<Set<OWLAxiom>> justifyInconsistentAll() {
        if (!pellet.isConsistent()) {
            if (explainer == null) {
                setupExplanation();
            }
            return explainer.getInconsistencyExplanations();
        } else {
            return null;
        }
    }

    public Set<Set<OWLAxiom>> justifyAll(OWLAxiom ax) {
        if (explainer == null) {
            setupExplanation();
        }
        return explainer.getEntailmentExplanations(ax);
    }

    public Set<Set<OWLAxiom>> justifySatisfiableAll(OWLDescription c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLDataFactory getDataFactory() {
        return manager.getOWLDataFactory();
    }

    public Object getUnderlyingReasoner() {
        return pellet;
    }

    public OWLOntology getOntology() {
        return ont;
    }

    public void unloadOntology(OWLOntology ontology) {
        pellet.unloadOntology(ontology);
    }

    public void unloadOntologies(Set<OWLOntology> ontologies) {
        pellet.unloadOntologies(ontologies);
    }

    public void setOntology(OWLOntology ontology) {
        pellet.setOntology(ontology);
    }

    public void refresh() {
        pellet.refresh();
    }

    public void realise() throws OWLReasonerException {
        pellet.realise();
    }

    public void ontologiesChanged(List<? extends OWLOntologyChange> changes) {
        pellet.ontologiesChanged(changes);
    }

    public void loadOntology(OWLOntology ontology) {
        pellet.loadOntology(ontology);
    }

    public void loadOntologies(Set<OWLOntology> ontologies) {
        pellet.loadOntologies(ontologies);
    }

    public boolean isTransitive(OWLObjectProperty p) {
        return pellet.isTransitive(p);
    }

    public boolean isSymmetric(OWLObjectProperty p) {
        return pellet.isSymmetric(p);
    }

    public boolean isSubTypeOf(OWLDataType d1, OWLDataType d2) {
        return pellet.isSubTypeOf(d1, d2);
    }

    public boolean isSubPropertyOf(OWLObjectProperty c1, OWLObjectProperty c2) {
        return pellet.isSubPropertyOf(c1, c2);
    }

    public boolean isSubPropertyOf(OWLDataProperty c1, OWLDataProperty c2) {
        return pellet.isSubPropertyOf(c1, c2);
    }

    public boolean isSubClassOf(OWLDescription c1, OWLDescription c2) {
        return pellet.isSubClassOf(c1, c2);
    }

    public boolean isSameAs(OWLIndividual ind1, OWLIndividual ind2) {
        return pellet.isSameAs(ind1, ind2);
    }

    public boolean isReflexive(OWLObjectProperty p) {
        return pellet.isReflexive(p);
    }

    public boolean isRealised() throws OWLReasonerException {
        return pellet.isRealised();
    }

    public boolean isIrreflexive(OWLObjectProperty p) {
        return pellet.isIrreflexive(p);
    }

    public boolean isInverseOf(OWLObjectProperty p1, OWLObjectProperty p2) {
        return pellet.isInverseOf(p1, p2);
    }

    public boolean isInverseFunctional(OWLObjectProperty p) {
        return pellet.isInverseFunctional(p);
    }

    public boolean isFunctional(OWLObjectProperty p) {
        return pellet.isFunctional(p);
    }

    public boolean isFunctional(OWLDataProperty p) {
        return pellet.isFunctional(p);
    }

    public boolean isEquivalentProperty(OWLObjectProperty p1, OWLObjectProperty p2) {
        return pellet.isEquivalentProperty(p1, p2);
    }

    public boolean isEquivalentProperty(OWLDataProperty p1, OWLDataProperty p2) {
        return pellet.isEquivalentProperty(p1, p2);
    }

    public boolean isEquivalentClass(OWLDescription c1, OWLDescription c2) {
        return pellet.isEquivalentClass(c1, c2);
    }

    public boolean isEntailed(OWLAxiom axiom) {
        return pellet.isEntailed(axiom);
    }

    public boolean isEntailed(Set<? extends OWLAxiom> axioms) {
        return pellet.isEntailed(axioms);
    }

    public boolean isEntailed(OWLOntology ont) {
        return pellet.isEntailed(ont);
    }

    public boolean isDisjointWith(OWLObjectProperty p1, OWLObjectProperty p2) {
        return pellet.isDisjointWith(p1, p2);
    }

    public boolean isDisjointWith(OWLDescription c1, OWLDescription c2) {
        return pellet.isDisjointWith(c1, c2);
    }

    public boolean isDisjointWith(OWLDataProperty p1, OWLDataProperty p2) {
        return pellet.isDisjointWith(p1, p2);
    }

    public boolean isDifferentFrom(OWLIndividual ind1, OWLIndividual ind2) {
        return pellet.isDifferentFrom(ind1, ind2);
    }

    public boolean isDefined(OWLObjectProperty prop) {
        return pellet.isDefined(prop);
    }

    public boolean isDefined(OWLIndividual ind) {
        return pellet.isDefined(ind);
    }

    public boolean isDefined(OWLDataProperty prop) {
        return pellet.isDefined(prop);
    }

    public boolean isDefined(OWLClass cls) {
        return pellet.isDefined(cls);
    }

    public boolean isConsistent(OWLOntology ontology) {
        return pellet.isConsistent(ontology);
    }

    public boolean isConsistent(OWLDescription d) {
        return pellet.isConsistent(d);
    }

    public boolean isConsistent() {
        return pellet.isConsistent();
    }

    public boolean isComplementOf(OWLDescription c1, OWLDescription c2) {
        return pellet.isComplementOf(c1, c2);
    }

    public boolean isClassified() {
        return pellet.isClassified();
    }

    public boolean isAntiSymmetric(OWLObjectProperty p) {
        return pellet.isAntiSymmetric(p);
    }

    public boolean hasType(OWLIndividual individual, OWLDescription type, boolean direct) throws OWLReasonerException {
        return pellet.hasType(individual, type, direct);
    }

    public boolean hasType(OWLIndividual individual, OWLDescription type) {
        return pellet.hasType(individual, type);
    }

    public boolean hasRange(OWLObjectProperty p, OWLDescription c) {
        return pellet.hasRange(p, c);
    }

    public boolean hasRange(OWLDataProperty p, OWLDataRange d) {
        return pellet.hasRange(p, d);
    }

    public boolean hasObjectPropertyRelationship(OWLIndividual subject, OWLObjectPropertyExpression property, OWLIndividual object) {
        return pellet.hasObjectPropertyRelationship(subject, property, object);
    }

    public boolean hasDomain(OWLObjectProperty p, OWLDescription c) {
        return pellet.hasDomain(p, c);
    }

    public boolean hasDomain(OWLDataProperty p, OWLDescription c) {
        return pellet.hasDomain(p, c);
    }

    public boolean hasDataPropertyRelationship(OWLIndividual subject, OWLDataPropertyExpression property, OWLConstant object) {
        return pellet.hasDataPropertyRelationship(subject, property, object);
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual ind, boolean direct) {
        return pellet.getTypes(ind, direct);
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual individual) {
        return pellet.getTypes(individual);
    }

    public OWLClass getType(OWLIndividual ind) {
        return pellet.getType(ind);
    }

    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty p) {
        return pellet.getSuperProperties(p);
    }

    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty p) {
        return pellet.getSuperProperties(p);
    }

    public Set<Set<OWLClass>> getSuperClasses(OWLDescription c) {
        return pellet.getSuperClasses(c);
    }

    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty p) {
        return pellet.getSubProperties(p);
    }

    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty p) {
        return pellet.getSubProperties(p);
    }

    public Set<Set<OWLClass>> getSubClasses(OWLDescription c) {
        return pellet.getSubClasses(c);
    }

    public Set<OWLIndividual> getSameAsIndividuals(OWLIndividual ind) {
        return pellet.getSameAsIndividuals(ind);
    }

    public Set<OWLConstant> getRelatedValues(OWLIndividual subject, OWLDataPropertyExpression property) {
        return pellet.getRelatedValues(subject, property);
    }

    public OWLConstant getRelatedValue(OWLIndividual subject, OWLDataPropertyExpression property) {
        return pellet.getRelatedValue(subject, property);
    }

    public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual subject, OWLObjectPropertyExpression property) {
        return pellet.getRelatedIndividuals(subject, property);
    }

    public OWLIndividual getRelatedIndividual(OWLIndividual subject, OWLObjectPropertyExpression property) {
        return pellet.getRelatedIndividual(subject, property);
    }

    public Set<? extends OWLObject> getRelated(OWLIndividual ind, OWLPropertyExpression<?, ?> prop) {
        return pellet.getRelated(ind, prop);
    }

    public Set<OWLDescription> getRanges(OWLObjectProperty p) {
        return pellet.getRanges(p);
    }

    public Set<OWLDataRange> getRanges(OWLDataProperty p) {
        return pellet.getRanges(p);
    }

    public Set<OWLProperty<?, ?>> getProperties() {
        return pellet.getProperties();
    }

    public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual individual) {
        return pellet.getObjectPropertyRelationships(individual);
    }

    public Map<OWLIndividual, Set<OWLIndividual>> getObjectPropertyAssertions(OWLObjectProperty prop) {
        return pellet.getObjectPropertyAssertions(prop);
    }

    public Set<OWLObjectProperty> getObjectProperties() {
        return pellet.getObjectProperties();
    }

    public PelletLoader getLoader() {
        return pellet.getLoader();
    }

    public Set<OWLOntology> getLoadedOntologies() {
        return pellet.getLoadedOntologies();
    }

    public KnowledgeBase getKB() {
        return pellet.getKB();
    }

    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty prop) {
        return pellet.getInverseProperties(prop);
    }

    public Set<OWLIndividual> getIndividuals(OWLDescription clsC, boolean direct) {
        return pellet.getIndividuals(clsC, direct);
    }

    public Set<OWLIndividual> getIndividuals() {
        return pellet.getIndividuals();
    }

    public Set<OWLClass> getInconsistentClasses() {
        return pellet.getInconsistentClasses();
    }

    public Set<OWLAxiom> getExplanation() throws OWLRuntimeException {
        return pellet.getExplanation();
    }

    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty p) {
        return pellet.getEquivalentProperties(p);
    }

    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty p) {
        return pellet.getEquivalentProperties(p);
    }

    public Set<OWLClass> getEquivalentClasses(OWLDescription c) {
        return pellet.getEquivalentClasses(c);
    }

    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty p) {
        return pellet.getDomains(p);
    }

    public Set<Set<OWLDescription>> getDomains(OWLDataProperty p) {
        return pellet.getDomains(p);
    }

    public Set<Set<OWLClass>> getDisjointClasses(OWLDescription c) {
        return pellet.getDisjointClasses(c);
    }

    public Set<OWLIndividual> getDifferentFromIndividuals(OWLIndividual ind) {
        return pellet.getDifferentFromIndividuals(ind);
    }

    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty p) {
        return pellet.getDescendantProperties(p);
    }

    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty p) {
        return pellet.getDescendantProperties(p);
    }

    public Set<Set<OWLClass>> getDescendantClasses(OWLDescription c) {
        return pellet.getDescendantClasses(c);
    }

    public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual individual) {
        return pellet.getDataPropertyRelationships(individual);
    }

    public Map<OWLIndividual, Set<OWLConstant>> getDataPropertyAssertions(OWLDataProperty prop) {
        return pellet.getDataPropertyAssertions(prop);
    }

    public Set<OWLDataProperty> getDataProperties() {
        return pellet.getDataProperties();
    }

    public Set<OWLClass> getComplementClasses(OWLDescription c) {
        return pellet.getComplementClasses(c);
    }

    public Set<OWLClass> getClasses() {
        return pellet.getClasses();
    }

    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty p) {
        return pellet.getAncestorProperties(p);
    }

    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty p) {
        return pellet.getAncestorProperties(p);
    }

    public Set<Set<OWLClass>> getAncestorClasses(OWLDescription c) {
        return pellet.getAncestorClasses(c);
    }

    public Set<OWLClass> getAllEquivalentClasses(OWLDescription c) {
        return pellet.getAllEquivalentClasses(c);
    }

    public void dispose() {
        pellet.dispose();
    }

    public Set<OWLAxiom> convertAxioms(Set<ATermAppl> terms) throws OWLRuntimeException {
        return pellet.convertAxioms(terms);
    }

    public OWLAxiom convertAxiom(ATermAppl term) {
        return pellet.convertAxiom(term);
    }

    public void clearOntologies() {
        pellet.clearOntologies();
    }

    public void classify() {
        pellet.classify();
    }

}
