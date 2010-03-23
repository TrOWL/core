/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import eu.trowl.owl.ql.QLBasicClass;
import java.net.URI;
import java.util.Set;

/**
 *
 * @author ed
 */
public class OwlObjectProperty extends OwlProperty {
    private Set<QLBasicClass> domain;
    private Set<QLBasicClass> range;

    /**
     *
     * @param uri
     */
    public OwlObjectProperty(URI uri) {
        super(uri);
    }

    /**
     *
     * @return
     */
    public Set<QLBasicClass> getDomain() {
        return domain;
    }

    /**
     *
     * @return
     */
    public Set<QLBasicClass> getRange() {
        return range;
    }
}
