/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

/**
 *
 * @author ed
 */
public class OwlExistential extends OwlClass {
    private OwlProperty on;
    private OwlClass what;

    /**
     *
     * @return
     */
    public OwlProperty getOn() {
        return on;
    }

    /**
     *
     * @param on
     */
    public void setOn(OwlProperty on) {
        this.on = on;
    }

    /**
     *
     * @return
     */
    public String getUri() {
        return "urn:class:exists";
    }
}
