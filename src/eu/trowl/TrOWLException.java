/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl;

/**
 *
 * @author ed
 */
public class TrOWLException extends RuntimeException {

    /**
     * Creates a new instance of <code>TrOWLException</code> without detail message.
     */
    public TrOWLException() {
    }

    /**
     *
     * @param e
     */
    public TrOWLException(Exception e) {
    }


    /**
     * Constructs an instance of <code>TrOWLException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TrOWLException(String msg) {
        super(msg);
    }
}
