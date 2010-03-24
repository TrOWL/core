/*
 * This file is part of TrOWL.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

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
