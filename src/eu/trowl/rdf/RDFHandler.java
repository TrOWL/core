/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

import java.net.URI;

/**
 *
 * @author ed
 */
public interface RDFHandler {
    /**
     *
     * @param base
     */
    public void setBase(URI base);
    /**
     *
     * @param t
     */
    public void processTriple(Triple t);
}
