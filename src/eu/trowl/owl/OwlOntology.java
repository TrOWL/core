/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import eu.trowl.owl.ql.QLSubClassAxiom;
import eu.trowl.owl.ql.QLBasicClass;
import java.net.URI;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * @author ed
 */
public class OwlOntology implements OwlNamedThing {
    private URI uri;
    private Set<QLBasicClass> classes;
    private Set<OwlObjectProperty> objectProperties;
    private Set<OwlDatatypeProperty> datatypeProperties;
    private Set<OwlAnnotationProperty> annotationProperties;
    private Set<QLSubClassAxiom> subClassAxioms;
    private Set<OwlSubObjectPropertyAxiom> subObjectPropertyAxioms;

    /**
     *
     * @param uri
     */
    public void setUri(URI uri) {
       this.uri = uri;
    }

    /**
     *
     * @return
     */
    public URI getUri() {
        return uri;
    }

    /**
     *
     * @param a
     * @return
     */
    public boolean addAxiom(OwlAxiom a) {
        return false;
    }

    /**
     *
     * @param c
     * @return
     */
    public boolean addClass(QLBasicClass c) {
        return classes.add(c);
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean addObjectProperty(OwlObjectProperty p) {
        if (objectProperties.contains(p))
            return false;
        objectProperties.add(p);
        return true;
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean addDatatypeProperty(OwlDatatypeProperty p) {
        if (datatypeProperties.contains(p))
            return false;
        datatypeProperties.add(p);
        return true;
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean addAnnotationProperty(OwlAnnotationProperty p) {
        return annotationProperties.add(p);
    }

    /**
     *
     * @param sup
     * @param sub
     * @return
     */
    public boolean addSubClass(QLBasicClass sup, OwlIntersection sub) {
        for (QLBasicClass b: sub.getBasicComponents()) {
            subClassAxioms.add(new QLSubClassAxiom(sup, b));
            addClass(b);
        }
            addClass(sup);
        return true;
    }

    /**
     *
     * @param sup
     * @param sub
     * @return
     */
    public boolean addSubClass(QLBasicClass sup, QLBasicClass sub) {
                    subClassAxioms.add(new QLSubClassAxiom(sup, sub));
                    addClass(sup);
                         addClass(sub);
                    return true;
    }


    /**
     *
     * @param c1
     * @param c2
     * @return
     */
    public boolean addEquivalentClass(QLBasicClass c1, QLBasicClass c2) {
        boolean b1 = addSubClass(c1, c2);
        boolean b2 = addSubClass(c2, c1);
        return (b1 | b2);
    }

}
