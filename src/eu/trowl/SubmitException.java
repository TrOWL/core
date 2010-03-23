/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl;

/**
 *
 * @author ethomas
 */
public class SubmitException extends ConsistencyException {

    /**
     * Creates a new instance of <code>SubmitException</code> without detail message.
     */
    public SubmitException() {
    }


    /**
     * Constructs an instance of <code>SubmitException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SubmitException(String msg) {
        super(msg);
    }
}
