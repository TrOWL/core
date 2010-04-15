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
import org.semanticweb.owl.model.OWLEntityVisitor;
import org.semanticweb.owl.model.OWLEntityVisitorEx;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;

/**
 *
 * @author ed
 */
public class OWLEntity extends OWLNamedObject implements org.semanticweb.owl.model.OWLEntity {

    public Set<OWLAnnotation> getAnnotations(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLAnnotation> getAnnotations(OWLOntology owlo, URI uri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLAnnotationAxiom> getAnnotationAxioms(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isBuiltIn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOWLClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLClass asOWLClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOWLObjectProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLObjectProperty asOWLObjectProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOWLDataProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLDataProperty asOWLDataProperty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOWLIndividual() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLIndividual asOWLIndividual() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOWLDataType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLDataType asOWLDataType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void accept(OWLEntityVisitor owlev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <O> O accept(OWLEntityVisitorEx<O> owleve) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
