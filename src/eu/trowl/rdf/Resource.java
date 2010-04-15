/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

import java.net.URI;

/**
 *
 * @author ed
 */
public class Resource extends Node {

    /**
     *
     */
    public Resource() {
        this.type = Node.TYPE_RESOURCE;
    }

    /**
     *
     * @param name
     */
    public Resource(String name) {
        this.value = name;
        this.type = Node.TYPE_RESOURCE;
    }

        public Resource(URI name) {
        this.value = name.toString();
        this.uriValue = name;
        this.type = Node.TYPE_RESOURCE;
    }


    /**
     *
     * @param name
     * @return
     */
    public static Resource fromBNode(String name) {
        Resource r= new Resource(name);
        r.type = Node.TYPE_BNODE;
        return r;
    }

    /**
     *
     * @param in
     * @return
     */
    public static Resource fromNode(Node in) {
        Resource r= new Resource(in.getURI());
        r.type = Node.TYPE_RESOURCE;
        return r;
    }

    /**
     *
     * @param name
     * @return
     */
    public static Resource fromURI(String name) {
        return new Resource(name);
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean sameAs(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this.value == null || !this.value.equals(obj.toString())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Resource other = (Resource) obj;
        if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
}

