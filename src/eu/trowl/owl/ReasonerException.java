/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import org.semanticweb.owl.inference.OWLReasonerException;

/**
 *
 * @author ed
 */
class ReasonerException extends OWLReasonerException {

    public ReasonerException(ReasonerException ex) {
        super (ex);
    }

    public ReasonerException(String a) {
        super (a);
    }

}
