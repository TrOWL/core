/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

/**
 *
 * @author ed
 */
public class NTripleSyntaxException extends RDFSyntaxException {

    /**
     * Creates a new instance of <code>NTripleSyntaxException</code> without detail message.
     */
    public NTripleSyntaxException() {
    }


    /**
     * Constructs an instance of <code>NTripleSyntaxException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NTripleSyntaxException(String msg) {
        super(msg);
    }
}
