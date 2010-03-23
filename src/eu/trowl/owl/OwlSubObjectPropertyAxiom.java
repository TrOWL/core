/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

/**
 *
 * @author ed
 */
public class OwlSubObjectPropertyAxiom extends OwlAxiom {
    private OwlObjectProperty lhs;
    private OwlObjectProperty rhs;

    /**
     *
     * @return
     */
    public OwlObjectProperty getLhs() {
        return lhs;
    }

    /**
     *
     * @param lhs
     */
    public void setLhs(OwlObjectProperty lhs) {
        this.lhs = lhs;
    }

    /**
     *
     * @return
     */
    public OwlObjectProperty getRhs() {
        return rhs;
    }

    /**
     *
     * @param rhs
     */
    public void setRhs(OwlObjectProperty rhs) {
        this.rhs = rhs;
    }
}
