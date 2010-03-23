/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl;

/**
 *
 * @author ethomas
 */
public class QueryException extends Exception {

    /**
     * Creates a new instance of <code>QueryException</code> without detail message.
     */
    public QueryException() {
    }


    /**
     * Constructs an instance of <code>QueryException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QueryException(String msg) {
        super(msg);
    }
}
