/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import java.net.URI;
import java.util.Set;
import java.util.HashSet;

/**
 *
 * @author ed
 */
public class OwlIndividual implements OwlNamedThing {
    private URI uri;

    /**
     *
     * @param uri
     */
    public OwlIndividual(URI uri) {
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
