/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.util;

import eu.trowl.owl.*;
import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owl.model.*;

/**
 *
 * @author ed
 */
public class Convert {
    /**
     *
     * @param in
     * @return
     */
    public static OwlNamedClass c(OWLClass in) {
        return new OwlNamedClass(in.getURI());
    }
    /**
     *
     * @param in
     * @return
     */
    public static Set<OwlNamedClass> c(Set<OWLClass> in) {
        HashSet<OwlNamedClass> out = new HashSet<OwlNamedClass>();
        for (OWLClass cls: in) {
           out.add(c(cls));
        }
        return out;
    }

    /**
     *
     * @param in
     * @return
     */
    public static OwlIndividual c(OWLIndividual in) {
        return new OwlIndividual(in.getURI());
    }

    /**
     *
     * @param in
     * @return
     */
    public static OwlObjectProperty c(OWLObjectProperty in) {
        return new OwlObjectProperty(in.getURI());
    }

    /**
     *
     * @param in
     * @return
     */
    public static OwlDatatypeProperty c(OWLDataProperty in) {
        return new OwlDatatypeProperty(in.getURI());
    }

    /*public static Set<OwlAxiom> c (Set<OWLAxiom> in) {
        Set<OwlAxiom> out = new HashSet<OwlAxiom>();
        for (OWLAxiom ax: in) {
            out.add(c(ax));
        }
        return out;
    }*/

    /**
     *
     * @param in
     * @return
     */
    public static OwlAxiom c(OWLAxiom in) {
        if (in.getClass().isAssignableFrom(OWLSubClassAxiom.class)) {
            OWLSubClassAxiom sc = (OWLSubClassAxiom) in;
            OWLClass sub = sc.getSubClass().asOWLClass();
            OWLClass sup = sc.getSuperClass().asOWLClass();
            OwlSubClassAxiom out = new OwlSubClassAxiom(c(sup), c(sub));
            return out;
        }
        return null;
    }

   /* public static Set<OWLAxiom> u (Set<OwlAxiom> in) {
        Set<OWLAxiom> out = new HashSet<OWLAxiom>();
        for (OwlAxiom ax: in) {
            out.add(u(ax));
        }
        return out;
    }

    //public static OWLAxiom u (OwlAxiom in) {
    //    return new OWLAxiom();
    //}*/
}
