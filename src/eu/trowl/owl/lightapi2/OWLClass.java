/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.lightapi2;

import java.net.URI;
import java.util.Set;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAnnotationAxiom;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDescriptionVisitor;
import org.semanticweb.owl.model.OWLDescriptionVisitorEx;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLEntityVisitor;
import org.semanticweb.owl.model.OWLEntityVisitorEx;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLNamedObjectVisitor;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectVisitor;
import org.semanticweb.owl.model.OWLObjectVisitorEx;
import org.semanticweb.owl.model.OWLOntology;

/**
 *
 * @author ed
 */
public class OWLClass extends OWLNamedObject
        implements org.semanticweb.owl.model.OWLClass {
    private URI uri;

    public Set<OWLDescription> getSuperClasses(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getSuperClasses(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getSubClasses(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getSubClasses(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getEquivalentClasses(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getEquivalentClasses(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getDisjointClasses(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getDisjointClasses(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLIndividual> getIndividuals(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLIndividual> getIndividuals(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefined(OWLOntology owlo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefined(Set<OWLOntology> set) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAnonymous() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isLiteral() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLClass asOWLClass() {
        return this;
    }

    public boolean isOWLThing() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOWLNothing() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLDescription getNNF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OWLDescription getComplementNNF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> asConjunctSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> asDisjunctSet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void accept(OWLDescriptionVisitor owldv) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <O> O accept(OWLDescriptionVisitorEx<O> owldve) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void accept(OWLObjectVisitor owlov) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <O> O accept(OWLObjectVisitorEx<O> owlove) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLEntity> getSignature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<org.semanticweb.owl.model.OWLClass> getClassesInSignature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataProperty> getDataPropertiesInSignature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLIndividual> getIndividualsInSignature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int compareTo(OWLObject o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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

    public void accept(OWLNamedObjectVisitor owlnov) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
