/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.query;

/**
 *
 * @author ed
 */
public abstract class QueryParser {
    /**
     *
     * @param input
     * @return
     */
    public abstract Query parse(String input);
}
