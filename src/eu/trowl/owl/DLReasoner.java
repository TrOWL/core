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

import eu.trowl.db.DB;
import eu.trowl.db.DBFactory;
import eu.trowl.util.Settings;
import eu.trowl.util.Types;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerFactory;
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
    private OWLReasoner pellet;
    private Set<OWLOntology> onts;
    private boolean dirty = false;
    private boolean allConsistent = true;
    //private PelletExplanation explainer = null;

    public void load(OWLOntologyManager man) {
        try {
            //PelletExplanation.setup();
            manager = man;
            OWLReasonerFactory fact = (OWLReasonerFactory) Class.forName(Settings.get("trowl.DLReasonerFactory")).newInstance();
            pellet = fact.createReasoner(manager);
            pellet.loadOntologies(manager.getImportsClosure(manager.getOntologies().iterator().next()));
            //pellet = new org.mindswap.pellet.owlapi.Reasoner(manager);
            for (OWLOntology ont : man.getOntologies()) {
                onts.addAll(man.getImportsClosure(ont));
            }

            for (OWLOntology ont : onts) {
                if (!pellet.isConsistent(ont)) {
                    allConsistent = false;
                }
            }

            if (allConsistent) {
                pellet.classify();
            }
        } catch (Exception ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        for (OWLOntology ont : onts) {
            for (OWLClass c : ont.getReferencedClasses()) {
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
        }

        reload();

        store(repository);
    }

    public void store(DB repository) {
        if (dirty) {
            reload();
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean allConsistent() {
        if (dirty) {
            reload();
        }
        return allConsistent;
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

        Set<OWLClass> unsatisfiable = Types.newSet();
        
        return unsatisfiable;
    }

    private void closeDirect(OWLClass c) {
        try {
            OWLClass toClose = c;
            Set<OWLIndividual> individuals = pellet.getIndividuals(c, false);
            Set<OWLAxiom> newAxes = Types.newSet();
            OWLDescription nominal = manager.getOWLDataFactory().getOWLObjectOneOf(individuals);
            OWLAxiom axiom = manager.getOWLDataFactory().getOWLEquivalentClassesAxiom(toClose, nominal);
            newAxes.add(axiom);
            onts.add(manager.createOntology(newAxes));
            dirty = true;
        } catch (Exception ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeTree(OWLClass c) {
        try {
            for (OWLClass toClose : flattenSetOfSets(pellet.getDescendantClasses(c))) {
                closeDirect(toClose);
            }
        } catch (OWLReasonerException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void close(OWLClass c) {
        closeDirect(c);
    }

    public void close(OWLObjectProperty p) {
        closeDirect(p);
    }

    public void closeTree(OWLObjectProperty p) {
        try {
            for (OWLObjectProperty toClose : flattenSetOfSets(pellet.getDescendantProperties(p))) {
                closeDirect(toClose);
            }
        } catch (OWLReasonerException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeDirect(OWLObjectProperty p) {
        OWLDataFactory fact = manager.getOWLDataFactory();
       /* try {
            OWLObjectProperty op = p;
            Map<OWLIndividual, Set<OWLIndividual>> instances = pellet.getObjectPropertyAssertions(op);

            Set<OWLAxiom> newAxes = Types.newSet();
            for (OWLIndividual subject : instances.keySet()) {
                OWLDescription rhs = fact.getOWLObjectOneOf(instances.get(subject));
                OWLDescription nominal = fact.getOWLObjectOneOf(subject);
                OWLDescription lhs = fact.getOWLObjectSomeRestriction(op, nominal);
                OWLAxiom axiom = fact.getOWLEquivalentClassesAxiom(lhs, rhs);
                           newAxes.add(axiom);

            }
            onts.add(manager.createOntology(newAxes));
            dirty = true;
        } catch (OWLOntologyChangeException ex) {
            Logger.getLogger(DLReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        throw new UnsupportedOperationException();
    }

    public void reload() {
        try {
            pellet.dispose();
        } catch (OWLReasonerException ex) {
            pellet = null;
        }
        load(manager);
        dirty = false;
    }


    public boolean isSatisfiable(OWLDescription c) throws OWLReasonerException {
        return pellet.isSatisfiable(c);
    }

    /**
     *
     * @param ax
     * @return
     */
    public Set<OWLAxiom> justify(OWLAxiom ax) {
        throw new UnsupportedOperationException("Not supported yet.");
        
        /*if (pellet.isEntailed(ax)) {
            return pellet.getExplanation();
        } else {
            return null;
        }*/
    }

    /**
     *
     * @param axes
     * @return
     */
    public Set<OWLAxiom> justify(Set<OWLAxiom> axes) {
        throw new UnsupportedOperationException("Not supported yet.");
/*        if (pellet.isEntailed(axes)) {
            return pellet.getExplanation();
        } else {
            return null;
        }*/
    }

    private void setupExplanation() {
        throw new UnsupportedOperationException("Not supported yet.");
        //explainer = new PelletExplanation(pellet);
    }

    /**
     *
     * @param c
     * @return
     */
    public Set<OWLAxiom> justifySatisfiable(OWLDescription c) {
                throw new UnsupportedOperationException("Not supported yet.");
/*
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
        }*/
    }

    public Class promoteTo() {
        return null;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public Set<OWLAxiom> justifyInconsistent() {
                throw new UnsupportedOperationException("Not supported yet.");
/*
        if (!pellet.isConsistent()) {
            if (explainer == null) {
                setupExplanation();
            }
            return explainer.getInconsistencyExplanation();
        } else {
            return null;
        }*/
    }

    public Set<Set<OWLAxiom>> justifyInconsistentAll() {
                throw new UnsupportedOperationException("Not supported yet.");

        /*
        if (!pellet.isConsistent()) {
            if (explainer == null) {
                setupExplanation();
            }
            return explainer.getInconsistencyExplanations();
        } else {
            return null;
        }*/
    }

    public Set<Set<OWLAxiom>> justifyAll(OWLAxiom ax) {
                throw new UnsupportedOperationException("Not supported yet.");
/*if (explainer == null) {
            setupExplanation();
        }
        return explainer.getEntailmentExplanations(ax);*/
    }

    public Set<Set<OWLAxiom>> justifySatisfiableAll(OWLDescription c) {
                throw new UnsupportedOperationException("Not supported yet.");
/*
        throw new UnsupportedOperationException("Not supported yet.");*/
    }

    public OWLDataFactory getDataFactory() {
        return manager.getOWLDataFactory();
    }

    public Object getUnderlyingReasoner() {
        return pellet;
    }

    public void unloadOntology(OWLOntology ontology) {
        onts.remove(ontology);
        reload();
    }

    public void unloadOntologies(Set<OWLOntology> ontologies) {
        onts.removeAll(ontologies);
        reload();
    }

    public void realise() throws OWLReasonerException {
        pellet.realise();
    }

    public boolean isTransitive(OWLObjectProperty p) throws OWLReasonerException {
        return pellet.isTransitive(p);
    }

    public boolean isSymmetric(OWLObjectProperty p) throws OWLReasonerException {
        return pellet.isSymmetric(p);
    }

    public boolean isSubClassOf(OWLDescription c1, OWLDescription c2) throws OWLReasonerException {
        return pellet.isSubClassOf(c1, c2);
    }

    public boolean isReflexive(OWLObjectProperty p) throws OWLReasonerException {
        return pellet.isReflexive(p);
    }

    public boolean isRealised() throws OWLReasonerException {
        return pellet.isRealised();
    }

    public boolean isIrreflexive(OWLObjectProperty p) throws OWLReasonerException {
        return pellet.isIrreflexive(p);
    }

    public boolean isInverseFunctional(OWLObjectProperty p) throws OWLReasonerException {
        return pellet.isInverseFunctional(p);
    }

    public boolean isFunctional(OWLObjectProperty p) throws OWLReasonerException {
        return pellet.isFunctional(p);
    }

    public boolean isFunctional(OWLDataProperty p) throws OWLReasonerException {
        return pellet.isFunctional(p);
    }

    public boolean isEquivalentClass(OWLDescription c1, OWLDescription c2) throws OWLReasonerException {
        return pellet.isEquivalentClass(c1, c2);
    }

    public boolean isConsistent(OWLOntology arg0) throws OWLReasonerException {
        return pellet.isConsistent(arg0);
    }

    public void loadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
        pellet.loadOntologies(arg0);
    }

    public void classify() throws OWLReasonerException {
        pellet.classify();
        this.isClassified = true;
    }

    public boolean isDefined(OWLClass arg0) throws OWLReasonerException {
        return pellet.isDefined(arg0);
    }

    public boolean isDefined(OWLObjectProperty arg0) throws OWLReasonerException {
        return pellet.isDefined(arg0);
    }

    public boolean isDefined(OWLDataProperty arg0) throws OWLReasonerException {
        return pellet.isDefined(arg0);
    }

    public boolean isDefined(OWLIndividual arg0) throws OWLReasonerException {
        return pellet.isDefined(arg0);
    }

    public Set<OWLOntology> getLoadedOntologies() {
        return pellet.getLoadedOntologies();
    }

    public void clearOntologies() throws OWLReasonerException {
        pellet.clearOntologies();
    }

    public void dispose() throws OWLReasonerException {
        pellet.dispose();
    }

    public Set<Set<OWLClass>> getSuperClasses(OWLDescription arg0) throws OWLReasonerException {
        return pellet.getSuperClasses(arg0);
    }

    public Set<Set<OWLClass>> getAncestorClasses(OWLDescription arg0) throws OWLReasonerException {
        return pellet.getAncestorClasses(arg0);
    }

    public Set<Set<OWLClass>> getSubClasses(OWLDescription arg0) throws OWLReasonerException {
        return pellet.getSubClasses(arg0);
    }

    public Set<Set<OWLClass>> getDescendantClasses(OWLDescription arg0) throws OWLReasonerException {
        return pellet.getDescendantClasses(arg0);
    }

    public Set<OWLClass> getEquivalentClasses(OWLDescription arg0) throws OWLReasonerException {
        return pellet.getEquivalentClasses(arg0);
    }

    public Set<OWLClass> getInconsistentClasses() throws OWLReasonerException {
        return pellet.getInconsistentClasses();
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual arg0, boolean arg1) throws OWLReasonerException {
        return pellet.getTypes(arg0, arg1);
    }

    public Set<OWLIndividual> getIndividuals(OWLDescription arg0, boolean arg1) throws OWLReasonerException {
        return pellet.getIndividuals(arg0, arg1);
    }

    public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        return pellet.getObjectPropertyRelationships(arg0);
    }

    public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        return pellet.getDataPropertyRelationships(arg0);
    }

    public boolean hasType(OWLIndividual arg0, OWLDescription arg1, boolean arg2) throws OWLReasonerException {
        return pellet.hasType(arg0, arg1, arg2);
    }

    public boolean hasObjectPropertyRelationship(OWLIndividual arg0, OWLObjectPropertyExpression arg1, OWLIndividual arg2) throws OWLReasonerException {
        return pellet.hasObjectPropertyRelationship(arg0, arg1, arg2);
    }

    public boolean hasDataPropertyRelationship(OWLIndividual arg0, OWLDataPropertyExpression arg1, OWLConstant arg2) throws OWLReasonerException {
        return pellet.hasDataPropertyRelationship(arg0, arg1, arg2);
    }

    public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual arg0, OWLObjectPropertyExpression arg1) throws OWLReasonerException {
        return pellet.getRelatedIndividuals(arg0, arg1);
    }

    public Set<OWLConstant> getRelatedValues(OWLIndividual arg0, OWLDataPropertyExpression arg1) throws OWLReasonerException {
        return pellet.getRelatedValues(arg0, arg1);
    }

    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return pellet.getSuperProperties(arg0);
    }

    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return pellet.getSubProperties(arg0);
    }

    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return pellet.getInverseProperties(arg0);
    }

    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return pellet.getEquivalentProperties(arg0);
    }

    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty arg0) throws OWLReasonerException {
        return pellet.getDomains(arg0);
    }

    public Set<OWLDescription> getRanges(OWLObjectProperty arg0) throws OWLReasonerException {
return pellet.getRanges(arg0);
    }

    public boolean isAntiSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
        return pellet.isAntiSymmetric(arg0);
    }

    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty arg0) throws OWLReasonerException {
return pellet.getSuperProperties(arg0);
    }

    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty arg0) throws OWLReasonerException {
        return pellet.getSubProperties(arg0);
    }

    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty arg0) throws OWLReasonerException {
return pellet.getAncestorProperties(arg0);
    }

    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty arg0) throws OWLReasonerException {
return pellet.getDescendantProperties(arg0);
    }

    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty arg0) throws OWLReasonerException {
return pellet.getEquivalentProperties(arg0);
    }

    public Set<Set<OWLDescription>> getDomains(OWLDataProperty arg0) throws OWLReasonerException {
        return pellet.getDomains(arg0);
    }

    public Set<OWLDataRange> getRanges(OWLDataProperty arg0) throws OWLReasonerException {
        return pellet.getRanges(arg0);
    }

}
