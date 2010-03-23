/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.ql;

import eu.trowl.owl.*;

/**
 *
 * @author ed
 */
public class QLSubClassAxiom extends OwlAxiom {
    private QLBasicClass sup;
    private QLBasicClass sub;

    /**
     *
     * @param sup
     * @param sub
     */
    public QLSubClassAxiom(QLBasicClass sup, QLBasicClass sub) {
        this.sup = sup;
        this.sub = sub;
    }

    /**
     *
     * @return
     */
    public QLBasicClass getSuperClass() {
        return sup;
    }

    /**
     *
     * @param sup
     */
    public void setSuperClass(QLBasicClass sup) {
        this.sup = sup;
    }

    /**
     *
     * @return
     */
    public QLBasicClass getSubClass() {
        return sup;
    }

    /**
     *
     * @param sub
     */
    public void setSubClass(QLBasicClass sub) {
        this.sub = sub;
    }

}
