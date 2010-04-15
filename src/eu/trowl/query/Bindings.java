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
 * Bindings.java
 *
 * Created on 26 July 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

import java.util.*;

/**
 *
 * @author ethomas
 */
public class Bindings {
    private HashMap data;
    
    /**
     * Creates a new instance of Bindings
     */
    public Bindings() {
        data = new HashMap();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void put(Object key, Object value) {
        if (data.containsKey(key)) {
            Set s = (Set) data.get (key);
            s.add(value);
            data.put (key, (Object) s);
        } else {
            HashSet s = new HashSet();
            s.add (value);
            data.put (key, (Object) s);            
        }
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Set get(Object key) {
        return (Set) data.get (key);
    }
    
    /**
     *
     * @param key
     * @return
     */
    public String getOneOf(Object key) {
        Set s = (Set) data.get (key);
        if (s == null) {
            return null;
        }
        return (String) s.iterator().next();
    }
    
    /**
     *
     * @return
     */
    public Set keySet() {
        return data.keySet();
    }
}
