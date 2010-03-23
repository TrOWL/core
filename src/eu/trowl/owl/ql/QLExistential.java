/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.ql;

import eu.trowl.owl.*;
import eu.trowl.vocab.OWLRDF;

/**
 *
 * @author ed
 */
public class QLExistential extends OwlExistential {
    private final OwlClass what = new OwlNamedClass(OWLRDF.THING);
}
