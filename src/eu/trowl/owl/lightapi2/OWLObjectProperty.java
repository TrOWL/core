/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.lightapi2;

import java.net.URI;
import java.util.Set;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAnnotationAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEntityVisitor;
import org.semanticweb.owl.model.OWLEntityVisitorEx;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLPropertyExpressionVisitor;
import org.semanticweb.owl.model.OWLPropertyExpressionVisitorEx;

/**
 *
 * @author ed
 */
public class OWLObjectProperty extends OWLEntity implements org.semanticweb.owl.model.OWLObjectProperty {

    public boolean isInverseFunctional(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isInverseFunctional(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSymmetric(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSymmetric(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAntiSymmetric(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAntiSymmetric(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAsymmetric(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAsymmetric(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReflexive(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReflexive(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isIrreflexive(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isIrreflexive(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTransitive(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTransitive(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getInverses(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getInverses(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLObjectPropertyExpression getInverseProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLObjectPropertyExpression getSimplified() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLObjectProperty getNamedProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getDomains(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getDomains(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getRanges(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getRanges(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getSuperProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getSuperProperties(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getSubProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getSubProperties(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getEquivalentProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getEquivalentProperties(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getDisjointProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectPropertyExpression> getDisjointProperties(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFunctional(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFunctional(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAnonymous() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void accept(OWLPropertyExpressionVisitor owlpev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <O> O accept(OWLPropertyExpressionVisitorEx<O> owlpv) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
