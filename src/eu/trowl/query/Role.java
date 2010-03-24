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
 * Role.java
 *
 * Created on 01 June 2007, 10:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

/**
 *
 * @author ethomas
 */
public class Role implements Cloneable {
    private String uri;
    private String domain;
    private String range;
    private Float fuzziness;
    private Boolean optional;
    
    /** Creates a new instance of Role
     * @param n
     * @param d
     * @param r
     * @param optional
     * @param f
     */
    public Role(String n, String d, String r, Boolean optional, Float f) {
        uri = n;
        domain = d;
        range = r;
        fuzziness = f;
	this.optional = optional;
    }
    
    /**
     *
     * @param n
     * @param d
     * @param r
     * @param f
     */
    public Role(String n, String d, String r, Float f) {
	this(n, d, r, null, f);
    }
    
    /**
     *
     * @param n
     * @param d
     * @param r
     * @param optional
     */
    public Role(String n, String d, String r, Boolean optional) {
	this(n, d, r, optional, null);
    }
    
    /**
     *
     * @param n
     * @param d
     * @param r
     */
    public Role(String n, String d, String r) {
	this(n, d, r, false, null);
    }
    
    /**
     *
     * @return
     */
    public String getURI() {
        return uri;
    }
    
    /**
     *
     * @return
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     *
     * @return
     */
    public String getRange() {
        return range;
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
        return uri + "(" + domain + "," + range + ")";
    }
    
    @Override
    public Role clone() {
        return new Role(uri, domain, range, optional, fuzziness);
    }
}
