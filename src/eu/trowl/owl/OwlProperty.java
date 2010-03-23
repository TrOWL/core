/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import java.net.URI;

/**
 *
 * @author ed
 */
public abstract class OwlProperty implements OwlNamedThing {
    /**
     *
     */
    protected URI uri;
    
    /**
     *
     * @param uri
     */
    public OwlProperty(URI uri) {
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
