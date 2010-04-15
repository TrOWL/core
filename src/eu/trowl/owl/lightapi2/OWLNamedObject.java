/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.lightapi2;

import java.net.URI;
import org.semanticweb.owl.model.OWLNamedObjectVisitor;

/**
 *
 * @author ed
 */
public class OWLNamedObject extends OWLObject implements org.semanticweb.owl.model.OWLNamedObject {
    protected URI uri;

    public URI getURI() {
        return uri;
    }

    public void accept(OWLNamedObjectVisitor owlnov) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
