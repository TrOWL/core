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

