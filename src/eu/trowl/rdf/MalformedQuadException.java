/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

/**
 *
 * @author ed
 */
public class MalformedQuadException extends Exception {

    /**
     * Creates a new instance of <code>MalformedTripleException</code> without detail message.
     */
    public MalformedQuadException() {
    }


    /**
     * Constructs an instance of <code>MalformedTripleException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MalformedQuadException(String msg) {
        super(msg);
    }
}
