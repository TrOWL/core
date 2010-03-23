/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl;

/**
 *
 * @author ethomas
 */
public class ConsistencyException extends Exception {

    /**
     * Creates a new instance of <code>ConsistencyException</code> without detail message.
     */
    public ConsistencyException() {
    }


    /**
     * Constructs an instance of <code>ConsistencyException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConsistencyException(String msg) {
        super(msg);
    }
}
