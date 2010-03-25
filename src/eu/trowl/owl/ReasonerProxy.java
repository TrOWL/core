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
import java.util.Map;
import java.util.Set;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;

/**
 *
 * @author ed
 */
public class ReasonerProxy extends ReasonerBase {
    private Reasoner use;

    public void unloadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
        use.unloadOntologies(arg0);
    }

    public void realise() throws OWLReasonerException {
        use.realise();
    }

    public void loadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
        use.loadOntologies(arg0);
    }

    public boolean isRealised() throws OWLReasonerException {
        return use.isRealised();
    }

    public boolean isDefined(OWLIndividual arg0) throws OWLReasonerException {
        return use.isDefined(arg0);
    }

    public boolean isDefined(OWLDataProperty arg0) throws OWLReasonerException {
        return use.isDefined(arg0);
    }

    public boolean isDefined(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isDefined(arg0);
    }

    public boolean isDefined(OWLClass arg0) throws OWLReasonerException {
        return use.isDefined(arg0);
    }

    public Set<OWLOntology> getLoadedOntologies() {
        return use.getLoadedOntologies();
    }

    public void dispose() throws OWLReasonerException {
        use.dispose();
    }

    public void clearOntologies() throws OWLReasonerException {
        use.clearOntologies();
    }

    public void classify() throws OWLReasonerException {
        use.classify();
    }

    public boolean isTransitive(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isTransitive(arg0);
    }

    public boolean isSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isSymmetric(arg0);
    }

    public boolean isReflexive(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isReflexive(arg0);
    }

    public boolean isIrreflexive(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isIrreflexive(arg0);
    }

    public boolean isInverseFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isInverseFunctional(arg0);
    }

    public boolean isFunctional(OWLDataProperty arg0) throws OWLReasonerException {
        return use.isFunctional(arg0);
    }

    public boolean isFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isFunctional(arg0);
    }

    public boolean isAntiSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.isAntiSymmetric(arg0);
    }

    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty arg0) throws OWLReasonerException {
        return use.getSuperProperties(arg0);
    }

    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getSuperProperties(arg0);
    }

    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty arg0) throws OWLReasonerException {
        return use.getSubProperties(arg0);
    }

    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getSubProperties(arg0);
    }

    public Set<OWLDataRange> getRanges(OWLDataProperty arg0) throws OWLReasonerException {
        return use.getRanges(arg0);
    }

    public Set<OWLDescription> getRanges(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getRanges(arg0);
    }

    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getInverseProperties(arg0);
    }

    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty arg0) throws OWLReasonerException {
        return use.getEquivalentProperties(arg0);
    }

    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getEquivalentProperties(arg0);
    }

    public Set<Set<OWLDescription>> getDomains(OWLDataProperty arg0) throws OWLReasonerException {
        return use.getDomains(arg0);
    }

    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getDomains(arg0);
    }

    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty arg0) throws OWLReasonerException {
        return use.getDescendantProperties(arg0);
    }

    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getDescendantProperties(arg0);
    }

    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty arg0) throws OWLReasonerException {
        return use.getAncestorProperties(arg0);
    }

    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        return use.getAncestorProperties(arg0);
    }

    public boolean hasType(OWLIndividual arg0, OWLDescription arg1, boolean arg2) throws OWLReasonerException {
        return use.hasType(arg0, arg1, arg2);
    }

    public boolean hasObjectPropertyRelationship(OWLIndividual arg0, OWLObjectPropertyExpression arg1, OWLIndividual arg2) throws OWLReasonerException {
        return use.hasObjectPropertyRelationship(arg0, arg1, arg2);
    }

    public boolean hasDataPropertyRelationship(OWLIndividual arg0, OWLDataPropertyExpression arg1, OWLConstant arg2) throws OWLReasonerException {
        return use.hasDataPropertyRelationship(arg0, arg1, arg2);
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual arg0, boolean arg1) throws OWLReasonerException {
        return use.getTypes(arg0, arg1);
    }

    public Set<OWLConstant> getRelatedValues(OWLIndividual arg0, OWLDataPropertyExpression arg1) throws OWLReasonerException {
        return use.getRelatedValues(arg0, arg1);
    }

    public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual arg0, OWLObjectPropertyExpression arg1) throws OWLReasonerException {
        return use.getRelatedIndividuals(arg0, arg1);
    }

    public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        return use.getObjectPropertyRelationships(arg0);
    }

    public Set<OWLIndividual> getIndividuals(OWLDescription arg0, boolean arg1) throws OWLReasonerException {
        return use.getIndividuals(arg0, arg1);
    }

    public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        return use.getDataPropertyRelationships(arg0);
    }

    public boolean isConsistent(OWLOntology arg0) throws OWLReasonerException {
        return use.isConsistent(arg0);
    }

    public boolean isSubClassOf(OWLDescription arg0, OWLDescription arg1) throws OWLReasonerException {
        return use.isSubClassOf(arg0, arg1);
    }

    public boolean isEquivalentClass(OWLDescription arg0, OWLDescription arg1) throws OWLReasonerException {
        return use.isEquivalentClass(arg0, arg1);
    }

    public Set<Set<OWLClass>> getSuperClasses(OWLDescription arg0) throws OWLReasonerException {
        return use.getSuperClasses(arg0);
    }

    public Set<Set<OWLClass>> getSubClasses(OWLDescription arg0) throws OWLReasonerException {
        return use.getSubClasses(arg0);
    }

    public Set<OWLClass> getInconsistentClasses() throws OWLReasonerException {
        return use.getInconsistentClasses();
    }

    public Set<OWLClass> getEquivalentClasses(OWLDescription arg0) throws OWLReasonerException {
        return use.getEquivalentClasses(arg0);
    }

    public Set<Set<OWLClass>> getDescendantClasses(OWLDescription arg0) throws OWLReasonerException {
        return use.getDescendantClasses(arg0);
    }

    public Set<Set<OWLClass>> getAncestorClasses(OWLDescription arg0) throws OWLReasonerException {
        return use.getAncestorClasses(arg0);
    }

    public ReasonerProxy(Reasoner use) {
        this.use = use;
    }

    private void promote(Reasoner r) {
        if (r.promoteTo() != null) {
            try {
                OWLOntologyManager man = r.getManager();
                ReasonerFactory rf = new ReasonerFactory();
                rf.setType(use.promoteTo());
                System.err.println("Promoting to " + use.promoteTo().getCanonicalName());
                use = rf.load(man);
            } catch (OntologyLoadException ex) {
                RuntimeException ex2 = new UnsupportedOperationException();
                ex2.initCause(ex);
                throw (ex2);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void storeNegative(DB repository) {
        use.storeNegative(repository);
    }

    public void storeNegative(String repository) {
        use.storeNegative(repository);
    }

    public void storeNegative() {
        use.storeNegative();
    }

    public void store(DB repository) {
        use.store(repository);
    }

    public void store(String repository) {
        use.store(repository);
    }

    public void store() {
        use.store();
    }

    public void reload() {
        use.reload();
    }


    public Set<OWLAxiom> justifySatisfiable(OWLDescription c) {
        return use.justifySatisfiable(c);
    }

    public Set<OWLClass> getUnsatisfiable() {
        return use.getUnsatisfiable();
    }
    public boolean allConsistent() {
        return use.allConsistent();
    }

    public void closeTree(OWLClass c) {
        use.closeTree(c);
    }

    public void close(OWLClass c) {
        try {
            use.close(c);
        } catch (UnsupportedOperationException ex) {
                promote(use);
                this.close(c);
        }
    }

    public boolean allSatisfiable() {
        return use.allSatisfiable();
    }

    public void load(OWLOntologyManager input) throws OntologyLoadException {
        use.load(input);
    }

    public Class promoteTo() {
        return use.promoteTo();
    }

    public OWLOntologyManager getManager() {
        return use.getManager();
    }

    public void close(OWLObjectProperty p) {
        try {
            use.close(p);
        } catch (UnsupportedOperationException ex) {
                promote(use);
                this.close(p);
        }
    }

    public void closeTree(OWLObjectProperty p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLAxiom>> justifySatisfiableAll(OWLDescription c) {
        return use.justifySatisfiableAll(c);
    }

    public Set<OWLAxiom> justifyInconsistent() {
        return use.justifyInconsistent();
    }

    public Set<Set<OWLAxiom>> justifyInconsistentAll() {
        return use.justifyInconsistentAll();
    }

    public Set<OWLAxiom> justify(OWLAxiom ax) {
        return use.justify(ax);
    }

    public Set<Set<OWLAxiom>> justifyAll(OWLAxiom ax) {
        return use.justifyAll(ax);
    }

    public OWLDataFactory getDataFactory() {
        return use.getDataFactory();
    }

    public Object getUnderlyingReasoner() {
        return use.getUnderlyingReasoner();
    }

    public boolean isSatisfiable(OWLDescription arg0) throws OWLReasonerException {
        return use.isSatisfiable(arg0);
    }
}
