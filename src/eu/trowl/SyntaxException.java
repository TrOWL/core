/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl;

/**
 *
 * @author ed
 */
public class SyntaxException extends TrOWLException {

    /**
     * Creates a new instance of <code>SyntaxException</code> without detail message.
     */
    public SyntaxException() {
    }


    /**
     * Constructs an instance of <code>SyntaxException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SyntaxException(String msg) {
        super(msg);
    }
}
