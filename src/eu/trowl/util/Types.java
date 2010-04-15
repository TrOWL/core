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

import java.util.*;

/**
 *
 * @author ed
 */
public class Types {

    public static <K, V> Map<K, V> newMap() {
        return new HashMap<K, V>();
    }

    public static <K> Set<K> newSet() {
        return new HashSet<K>();
    }

    public static <T1, T2> Pair<T1, T2> newPair() {
        return new Pair<T1, T2>();
    }

    public static <K> Set<K> newOrderedSet() {
        return new LinkedHashSet<K>();
    }

    public static <K> List<K> newList() {
        return new LinkedList<K>();
    }

    public static <K> Stack<K> newStack() {
        return new Stack<K>();
    }

    public static <K> Set<K> newTrustedSet() {
        return new TrustedSet<K>();
    }
}
