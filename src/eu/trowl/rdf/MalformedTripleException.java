/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

/**
 *
 * @author ed
 */
public class MalformedTripleException extends Exception {

    /**
     * Creates a new instance of <code>MalformedTripleException</code> without detail message.
     */
    public MalformedTripleException() {
    }


    /**
     * Constructs an instance of <code>MalformedTripleException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MalformedTripleException(String msg) {
        super(msg);
    }
}
