/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;
import eu.trowl.SyntaxException;

/**
 *
 * @author ed
 */
public class RDFSyntaxException extends SyntaxException {

    /**
     * Creates a new instance of <code>RDFSyntaxException</code> without detail message.
     */
    public RDFSyntaxException() {
    }


    /**
     * Constructs an instance of <code>RDFSyntaxException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RDFSyntaxException(String msg) {
        super(msg);
    }
}
