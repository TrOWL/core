/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import eu.trowl.owl.ql.QLGeneralClass;
import eu.trowl.owl.ql.QLBasicClass;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ed
 */
public class OwlIntersection extends QLGeneralClass {
    private Set<QLGeneralClass> components;

    /**
     *
     */
    public OwlIntersection() {
        components = new HashSet<QLGeneralClass>();
    }

    /**
     *
     * @return
     */
    public Set<QLGeneralClass> getComponents() {
        return components;
    }

    /**
     *
     * @return
     */
    public Set<QLBasicClass> getBasicComponents() {
        return new HashSet<QLBasicClass>();
    }

    /**
     *
     * @return
     */
    public String getUri() {
        return "urn:class:intersection:";
    }
}
