/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import eu.trowl.owl.ql.QLGeneralClass;

/**
 *
 * @author ed
 */
public class OwlNegation extends QLGeneralClass {
    private QLGeneralClass of;

    /**
     *
     * @return
     */
    public QLGeneralClass getOf() {
        return of;
    }

    /**
     *
     * @return
     */
    public String getUri() {
        return "urn:class:neg:";
    }
}
