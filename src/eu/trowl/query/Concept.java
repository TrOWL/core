/*
 * Concept.java
 *
 * Created on 01 June 2007, 10:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

import eu.trowl.rdf.*;

/**
 *
 * @author ethomas
 */
public class Concept implements Cloneable {
    private String name;
    private Node value;
    private Float fuzziness;
    private Boolean optional;
    
    /** Creates a new instance of Concept
     * @param n
     * @param v
     * @param f
     * @param optional
     */
    public Concept(String n, Node v, Boolean optional, Float f) {
        name = n;
        value = v;
        fuzziness = f;
	this.optional = optional;
    }
    
    /**
     *
     * @param n
     * @param v
     * @param f
     */
    public Concept(String n, Node v, Float f) {
	this(n, v, null, f);
    }
    
    /**
     *
     * @param n
     * @param v
     * @param optional
     */
    public Concept(String n, Node v, Boolean optional) {
	this(n, v, optional, null);
    }
    
    /**
     *
     * @param n
     * @param v
     */
    public Concept(String n, Node v) {
	this(n, v, false, null);
    }
    
    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     *
     * @return
     */
    public Node getValue() {
        return value;
    }
    
    /**
     *
     * @return
     */
    public Float getFuzziness() {
        return fuzziness;
    }
    
    /**
     *
     * @return
     */
    public Boolean getOptional() {
	return optional;
    }

    @Override
    public String toString() {
        return name + "(" + value.toString() + ")";
    }

    @Override
    public Concept clone() {
        return new Concept(name, value, optional, fuzziness);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals (Object cmp) {
        return (cmp.toString().equals(this.toString()));
    }
}
