/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.lightapi2;

import java.util.Set;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLPropertyExpressionVisitor;
import org.semanticweb.owl.model.OWLPropertyExpressionVisitorEx;

/**
 *
 * @author ed
 */
public class OWLDataProperty extends OWLEntity implements org.semanticweb.owl.model.OWLDataProperty {
    public Set<OWLDescription> getDomains(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getDomains(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataRange> getRanges(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataRange> getRanges(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getSuperProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getSuperProperties(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getSubProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getSubProperties(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getEquivalentProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getEquivalentProperties(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getDisjointProperties(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataPropertyExpression> getDisjointProperties(Set<OWLOntology> set) {
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
