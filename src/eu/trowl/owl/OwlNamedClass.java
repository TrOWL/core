/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import eu.trowl.owl.ql.QLBasicClass;
import java.net.URI;

/**
 *
 * @author ed
 */
public class OwlNamedClass extends QLBasicClass implements OwlNamedThing {
    private URI uri;

    /**
     *
     * @param uri
     */
    public OwlNamedClass(URI uri) {
        this.uri = uri;
    }

    /**
     *
     * @return
     */
    public URI getUri() {
        return uri;
    }

    /**
     *
     * @param uri
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }
}
