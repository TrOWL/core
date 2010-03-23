/*
 * QueryDatabaseException.java
 *
 * Created on 14 August 2006, 13:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

/**
 *
 * @author ethomas
 */
public class QueryDatabaseException extends QueryException {
    
    /**
     * Creates a new instance of <code>QueryDatabaseException</code> without detail message.
     */
    public QueryDatabaseException() {
    }
    
    
    /**
     * Constructs an instance of <code>QueryDatabaseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QueryDatabaseException(String msg) {
        super(msg);
    }
    
    /**
     *
     * @param cause
     */
    public QueryDatabaseException(Throwable cause) {
        super(cause);
    }
    
    /**
     *
     * @param message
     * @param cause
     */
    public QueryDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
