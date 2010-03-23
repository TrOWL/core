/*
 * QuerySyntaxException.java
 *
 * Created on 09 August 2006, 20:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

/**
 *
 * @author ethomas
 */
public class QuerySyntaxException extends QueryException {
    
    /**
     * Creates a new instance of <code>QuerySyntaxException</code> without detail message.
     */
    public QuerySyntaxException() {
    }
    
    
    /**
     * Constructs an instance of <code>QuerySyntaxException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QuerySyntaxException(String msg) {
        super(msg);
    }

    /**
     *
     * @param msg
     * @param line
     * @param col
     */
    public QuerySyntaxException(String msg, int line, int col) {
        super(msg + " on line " + line + ", column " + col);
    }
}
