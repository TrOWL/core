/*
 * QueryTypeException.java
 *
 * Created on 09 August 2006, 17:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

/**
 *
 * @author ethomas
 */
public class QueryTypeException extends QueryException {
    
    /**
     * Creates a new instance of <code>QueryTypeException</code> without detail message.
     */
    public QueryTypeException() {
    }
    
    
    /**
     * Constructs an instance of <code>QueryTypeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QueryTypeException(String msg) {
        super(msg);
    }
}
