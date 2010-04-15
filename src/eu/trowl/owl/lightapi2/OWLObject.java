/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.lightapi2;

import java.util.Set;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectVisitor;
import org.semanticweb.owl.model.OWLObjectVisitorEx;

/**
 *
 * @author ed
 */
public class OWLObject implements org.semanticweb.owl.model.OWLObject {

    public void accept(OWLObjectVisitor owlov) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <O> O accept(OWLObjectVisitorEx<O> owlove) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLEntity> getSignature() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLClass> getClassesInSignature() {
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

    public int compareTo(org.semanticweb.owl.model.OWLObject o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
