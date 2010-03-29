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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.util;

import java.util.*;

/**
 *
 * @author ed
 */
public class Types {
    public static <K,V> Map<K,V> newMap() { return new HashMap<K,V>(); }
    public static <K> Set<K> newSet() { return new HashSet<K>(); }
            public static <T1, T2> Pair<T1,T2> newPair() {
            return new Pair<T1, T2>(); 
        }

}
