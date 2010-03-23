/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.loader;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ed
 */
public class TreeThing {
private URI uri;
private Set<TreeThing> subThings;

/**
 *
 * @return
 */
public Set<TreeThing> getSubThings() {
        return subThings;
    }

/**
 *
 * @param uri
 */
public TreeThing(URI uri) {
        this.uri = uri;
        subThings = new HashSet<TreeThing>();
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
     * @param uri
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     *
     * @param t
     */
    public void addSubThing(TreeThing t) {
        this.subThings.add(t);
    }

    @Override
    public String toString() {
        return uri.toString();
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TreeThing other = (TreeThing) obj;
        if ((this.uri == null) ? (other.uri != null) : !this.uri.equals(other.uri)) {
            return false;
        }
        return true;
    }

}
