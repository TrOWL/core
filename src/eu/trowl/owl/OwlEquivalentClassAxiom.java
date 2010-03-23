/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import eu.trowl.owl.ql.QLSubClassAxiom;
import eu.trowl.owl.ql.QLBasicClass;

/**
 *
 * @author ed
 */
public class OwlEquivalentClassAxiom extends QLSubClassAxiom {
    /**
     *
     * @param c1
     * @param c2
     */
    public OwlEquivalentClassAxiom(QLBasicClass c1, QLBasicClass c2) {
        super (c1, c2);
    }
}
