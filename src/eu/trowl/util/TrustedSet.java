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

package eu.trowl.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class TrustedSet<T> implements Set<T> {
        private List<T> store;

        public TrustedSet() {
            store = Types.newList();
        }

        public <T> T[] toArray(T[] a) {
            return store.toArray(a);
        }

        public Object[] toArray() {
            return store.toArray();
        }

        public List<T> subList(int fromIndex, int toIndex) {
            return store.subList(fromIndex, toIndex);
        }

        public int size() {
            return store.size();
        }

        public T set(int index, T element) {
            return store.set(index, element);
        }

        public boolean retainAll(Collection<?> c) {
            return store.retainAll(c);
        }

        public boolean removeAll(Collection<?> c) {
            return store.removeAll(c);
        }

        public T remove(int index) {
            return store.remove(index);
        }

        public boolean remove(Object o) {
            return store.remove((T) o);
        }

        public ListIterator<T> listIterator(int index) {
            return store.listIterator(index);
        }

        public ListIterator<T> listIterator() {
            return store.listIterator();
        }

        public int lastIndexOf(Object o) {
            return store.lastIndexOf(o);
        }

        public Iterator<T> iterator() {
            return store.iterator();
        }

        public boolean isEmpty() {
            return store.isEmpty();
        }

        public int indexOf(Object o) {
            return store.indexOf(o);
        }

        public int hashCode() {
            return store.hashCode();
        }

        public T get(int index) {
            return store.get(index);
        }

        public boolean equals(Object o) {
            return store.equals(o);
        }

        public boolean containsAll(Collection<?> c) {
            return store.containsAll(c);
        }

        public boolean contains(Object o) {
            return store.contains(o);
        }

        public void clear() {
            store.clear();
        }

        public boolean addAll(int index, Collection<? extends T> c) {
            return store.addAll(index, c);
        }

        public boolean addAll(Collection<? extends T> c) {
            return store.addAll(c);
        }

        public void add(int index, T element) {
            store.add(index, element);
        }

        public boolean add(T e) {
            return store.add(e);
        }

    }
