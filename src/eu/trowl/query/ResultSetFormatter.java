/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.query;

/**
 *
 * @author ed
 */
public abstract class ResultSetFormatter {
    /**
     *
     */
    protected ResultSet rs;

    /**
     *
     * @param rs
     */
    public ResultSetFormatter(ResultSet rs) {
        this.rs = rs;
    }
    
    /**
     *
     * @return
     */
    public abstract String format();
}
