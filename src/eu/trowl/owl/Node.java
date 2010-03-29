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
package eu.trowl.owl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import eu.trowl.util.Types;

/**
 *
 * @author ed
 */
public class Node<T> implements Set<T> {
    private Set<Node<T>> children;
    private Set<Node<T>> parents;
    private Set<T> contents;
//    private Set<T> hiddenContents;

    public Node() {
        children = Types.newSet();
        parents = Types.newSet();
        contents = Types.newSet();
 //       hiddenContents = new HashSet<T>();
    }

    public <T> T[] toArray(T[] a) {
        return contents.toArray(a);
    }

    public Object[] toArray() {
        return contents.toArray();
    }

    public int size() {
        return contents.size();
    }

    public boolean retainAll(Collection<?> c) {
        return contents.retainAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return contents.removeAll(c);
    }

    public boolean remove(Object o) {
        return contents.remove(o);
    }

    public Iterator<T> iterator() {
        return contents.iterator();
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public int hashCode() {
        return contents.hashCode();
    }

    public boolean equals(Object o) {
        return contents.equals(o);
    }

    public boolean containsAll(Collection<?> c) {
        return contents.containsAll(c);
    }

    public boolean contains(Object o) {
        return contents.contains(o);
        
    }

    public void clear() {
        contents.clear();
    }

    @Override
    public String toString() {
//        return "hello";
        return contents.toString();
    }

    public Set<T> asSet() {
        return contents;
    }

    public boolean addAll(Collection<? extends T> c) {
        return contents.addAll(c);
    }

    public boolean add(T e) {
        return contents.add(e);
    }

    public boolean addChild(Node<T> n) {
        if (children.add(n)) {
            n.parents.add(this);
            return true;
        }
        return false;
    }

    public Set<Node<T>> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Set<Node<T>> getAscendants() {
        Set<Node<T>> ascendants = new HashSet<Node<T>>();
        getDescendants(ascendants, this);
        return Collections.unmodifiableSet(ascendants);
    }

    private void getAscendants(Set<Node<T>> ascendants, Node<T> current) {
        ascendants.add(current);
        for (Node<T> parent: current.getParents()) {
            if (!ascendants.contains(parent))
            getAscendants(ascendants, parent);
        }
    }

    public Set<Node<T>> getDescendants() {
        Set<Node<T>> descendants = new HashSet<Node<T>>();
        getDescendants(descendants, this);
        return Collections.unmodifiableSet(descendants);
    }

    private void getDescendants(Set<Node<T>> descendants, Node<T> current) {
        descendants.add(current);
        for (Node<T> child: current.getChildren()) {
            if (!descendants.contains(child))
            getDescendants(descendants, child);
        }
        
    }

    public Set<Node<T>> getParents() {
        return Collections.unmodifiableSet(parents);
    }

    public boolean hasDescendant(Node<T> candidate) {
        return hasDescendant(candidate, this);
    }

    private boolean hasDescendant(Node<T> candidate, Node<T> current) {
        if (current == null || current.getChildren() == null) {
            return false;
        }
        if (current.getChildren().contains(candidate)) {
            return true;
        }
        for (Node<T> n : current.getChildren()) {
            if (n != null) {
                return hasDescendant(candidate, n);
            }
        }
        return false;
    }

    public boolean hasAscendant(Node<T> candidate) {
        return hasAscendant(candidate, this);
    }

    private boolean hasAscendant(Node<T> candidate, Node<T> current) {
        if (current == null || current.getParents() == null) {
            return false;
        }
        if (current.getParents().contains(candidate)) {
            return true;
        }
        for (Node<T> n : current.getParents()) {
            if (n != null) {
                return hasAscendant(candidate, n);
            }
        }
        return false;
    }

    public boolean hasDescendant(T candidate) {
        return hasDescendant(candidate, this);
    }

    private boolean hasDescendant(T candidate, Node<T> current) {
        if (current == null) {
            return false;
        }

        for (Node<T> n : current.getChildren()) {
            if (n != null) {
                if (n.contains(candidate)) {
                    return true;
                } else {
                    return hasDescendant(candidate, n);
                }
            }
        }
        return false;
    }

    public boolean hasAscendant(T candidate) {
        return hasAscendant(candidate, this);
    }

    private boolean hasAscendant(T candidate, Node<T> current) {
        if (current == null || current.getParents() == null) {
            return false;
        }
        for (Node<T> n : current.getParents()) {
            if (n != null) {
                if (n.contains(candidate)) {
                    return true;
                } else {
                    return hasAscendant(candidate, n);
                }
            }
        }
        return false;
    }
    
    private void removeDescendant(Node<T> candidate) {
        removeDescendant(candidate, this);
    }
    
    private void removeDescendant(Node<T> candidate, Node<T> current) {
        if (current == null) {
            return;
        }

        if (current.children.contains(candidate)) {
            current.children.remove(candidate);
            return;
        }
        
        for (Node<T> n : current.getChildren()) {
            removeDescendant(candidate, n);
        }
    }

    public static <T> Node<T> mergeNodes(Node<T> a, Node<T> b) {
        Node<T> n = new Node<T>();
        n.contents.addAll(a);
        n.contents.addAll(b);
        a.removeDescendant(b);
        b.removeDescendant(a);
        n.children.addAll(a.children);
        n.parents.addAll(a.parents);
        n.children.addAll(b.children);
        n.parents.addAll(b.parents);
        
        for (Node<T> parent: a.parents) {
            parent.children.remove(a);
            parent.children.add(n);
        }

        for (Node<T> parent: b.parents) {
            parent.children.remove(b);
            parent.children.add(n);
        }

        for (Node<T> child: a.parents) {
            child.parents.remove(a);
            child.parents.add(n);
        }

        for (Node<T> child: b.parents) {
            child.parents.remove(b);
            child.parents.add(n);
        }

        return n;
    }
}
