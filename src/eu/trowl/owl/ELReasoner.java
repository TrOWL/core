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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.StringInputSource;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import uk.ac.abdn.REL.model.AxiomPool;
import uk.ac.abdn.REL.reasoner.RELReasoner;

/**
 *
 * @author ed
 */
public class ELReasoner implements Reasoner {

    private RELReasoner rel;
    private OWLOntology ont;
    private OWLOntologyManager manager;
    private boolean classified = false;

    public void store() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void store(String repository) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void store(DB repository) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void storeNegative() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void storeNegative(String repository) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void storeNegative(DB repository) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean consistent() {
        return true;
    }

    public boolean allSatisfiable() {
        return getUnsatisfiable().isEmpty();
    }

    public Set<OWLClass> getUnsatisfiable() {
        Set<OWLClass> out = new HashSet<OWLClass>();

        for (OWLClass candidate : ont.getReferencedClasses()) {
            OWLSubClassAxiom axiom = manager.getOWLDataFactory().getOWLSubClassAxiom(candidate, manager.getOWLDataFactory().getOWLNothing());
            if (rel.entail(axiom)) {
                out.add(candidate);
            }
        }
        return out;
    }

    public void load(OWLOntologyManager man) throws OntologyLoadException {
        try {
            manager = man;
            ont = manager.getOntologies().iterator().next();
            rel = new RELReasoner(manager);
            rel.loadOntology(ont); //BUG cannotdeal with multiple ontologies!
            rel.TBoxClassification();
        } catch (OWLOntologyChangeException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        } catch (OWLOntologyCreationException ex) {
            OntologyLoadException e2 = new OntologyLoadException();
            e2.initCause(ex);
            throw (e2);
        }
    }

    public Set<OWLIndividual> getDirectInstances(OWLDescription c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLIndividual> getInstances(OWLDescription c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean subsumes(OWLClass superclass, OWLClass subclass) {
        OWLSubClassAxiom ax = manager.getOWLDataFactory().getOWLSubClassAxiom(superclass, subclass);
        return rel.entail(ax);
    }

    public Set<OWLClass> getDirectSubClasses(OWLDescription c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void reload() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void close(OWLClass c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeTree(OWLClass c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSatisfiable(OWLDescription c) {
        return subsumes (manager.getOWLDataFactory().getOWLNothing(), c.asOWLClass());
    }

    /**
     *
     * @param c
     * @return
     */
    public Set<OWLAxiom> justifySatisfiable(OWLDescription c) {
        OWLSubClassAxiom axiom = manager.getOWLDataFactory().getOWLSubClassAxiom(c, manager.getOWLDataFactory().getOWLNothing());
        Set<OWLAxiom> axes = new HashSet<OWLAxiom>();
        try {
            for (AxiomPool axPool : rel.justify(ont, axiom, 1)) {
                axes.addAll(axPool.axioms);
            }
            return axes;
        } catch (OWLException ex) {
            return Collections.EMPTY_SET;
        }
    }

    public Set<Set<OWLAxiom>> justifySatisfiableAll(OWLDescription c) {
        OWLSubClassAxiom axiom = manager.getOWLDataFactory().getOWLSubClassAxiom(c, manager.getOWLDataFactory().getOWLNothing());
        Set<Set<OWLAxiom>> axes = new HashSet<Set<OWLAxiom>>();
        try {
            for (AxiomPool axPool : rel.justify(ont, axiom)) {
                Set<OWLAxiom> thisSet = new HashSet<OWLAxiom>();
                thisSet.addAll(axPool.axioms);
                axes.add(thisSet);
            }
            return axes;
        } catch (OWLException ex) {
            return Collections.EMPTY_SET;
        }
    }

    public Set<OWLAxiom> justifyInconsistent() {
        if (this.consistent())
            return null;

        HashSet<OWLAxiom> out = new HashSet<OWLAxiom>();
        try {
            out.addAll(rel.inconsistencyJustification(ont, 1).get(0).axioms);
        } catch (OWLOntologyChangeException ex) {
            Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    public Set<OWLAxiom> justify(OWLAxiom ax) {
       if (this.consistent())
            return null;

        if (ax.getClass().isAssignableFrom(OWLSubClassAxiom.class)) {
           OWLSubClassAxiom scax = (OWLSubClassAxiom) ax;
           Set<OWLAxiom> out = new HashSet<OWLAxiom>();
            try {
                for (AxiomPool p : rel.justify(ont, scax, 1)) {
                    out.addAll(p.axioms);
                }
            } catch (OWLOntologyChangeException ex) {
                Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OWLOntologyCreationException ex) {
                Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
            }

           return out;
       } else {
           throw new UnsupportedOperationException();
       }
    }

    public Set<Set<OWLAxiom>> justifyAll(OWLAxiom ax) {
       if (this.consistent())
            return null;

        if (ax.getClass().isAssignableFrom(OWLSubClassAxiom.class)) {
           OWLSubClassAxiom scax = (OWLSubClassAxiom) ax;
           Set<Set<OWLAxiom>> out = new HashSet<Set<OWLAxiom>>();
            try {
                for (AxiomPool p : rel.justify(ont, scax)) {
                    Set<OWLAxiom> current = new HashSet<OWLAxiom>();
                    current.addAll(p.axioms);
                    out.add(current);
                }
            } catch (OWLOntologyChangeException ex) {
                Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OWLOntologyCreationException ex) {
                Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
            }

           return out;
       } else {
           throw new UnsupportedOperationException();
       }
    }

    public Set<Set<OWLAxiom>> justifyInconsistentAll() {
        if (this.consistent())
            return null;

        Set<Set<OWLAxiom>> out = new HashSet<Set<OWLAxiom>>();
        try {
            for (AxiomPool p : rel.inconsistencyJustification(ont)) {
                Set<OWLAxiom> temp = new HashSet<OWLAxiom>();
                temp.addAll(p.axioms);
                out.add(temp);
            }
        } catch (OWLOntologyChangeException ex) {
            Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(ELReasoner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }


    public Class promoteTo() {
        return DLReasoner.class;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public OWLDataFactory getDataFactory() {
        return manager.getOWLDataFactory();
    }

    public void close(OWLObjectProperty p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeTree(OWLObjectProperty p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUnderlyingReasoner() {
        return rel;
    }

    public Set<OWLClass> classifyIndividual(OWLIndividual i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public OWLOntology getOntology() {
        return ont;
    }

    public boolean isConsistent(OWLOntology arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void loadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isClassified() throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void classify() throws OWLReasonerException {
        rel.TBoxClassification();
        classified = true;
    }

    public boolean isRealised() throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void realise() throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefined(OWLClass arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefined(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefined(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefined(OWLIndividual arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLOntology> getLoadedOntologies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unloadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clearOntologies() throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dispose() throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSubClassOf(OWLDescription arg0, OWLDescription arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEquivalentClass(OWLDescription arg0, OWLDescription arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLClass>> getSuperClasses(OWLDescription arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLClass>> getAncestorClasses(OWLDescription arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLClass>> getSubClasses(OWLDescription arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLClass>> getDescendantClasses(OWLDescription arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLClass> getEquivalentClasses(OWLDescription arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLClass> getInconsistentClasses() throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual arg0, boolean arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLIndividual> getIndividuals(OWLDescription arg0, boolean arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasType(OWLIndividual arg0, OWLDescription arg1, boolean arg2) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasObjectPropertyRelationship(OWLIndividual arg0, OWLObjectPropertyExpression arg1, OWLIndividual arg2) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasDataPropertyRelationship(OWLIndividual arg0, OWLDataPropertyExpression arg1, OWLConstant arg2) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual arg0, OWLObjectPropertyExpression arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLConstant> getRelatedValues(OWLIndividual arg0, OWLDataPropertyExpression arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getRanges(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isInverseFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTransitive(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReflexive(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isIrreflexive(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAntiSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDescription>> getDomains(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataRange> getRanges(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFunctional(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
