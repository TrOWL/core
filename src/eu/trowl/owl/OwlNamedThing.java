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
public interface OwlNamedThing {
    /**
     *
     * @return
     */
    public URI getUri();
    /**
     *
     * @param uri
     */
    public void setUri(URI uri);
}
