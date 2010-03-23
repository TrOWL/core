/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

/**
 *
 * @author ed
 */
public class OwlSubClassAxiom extends OwlAxiom {
    private OwlClass sup;
    private OwlClass sub;

    /**
     *
     * @return
     */
    public OwlClass getSubClass() {
        return sub;
    }

    /**
     *
     * @return
     */
    public OwlClass getSuperClass() {
        return sup;
    }

    /**
     *
     * @param sup
     * @param sub
     */
    public OwlSubClassAxiom(OwlClass sup, OwlClass sub) {
        this.sup = sup;
        this.sub = sub;
    }
}
