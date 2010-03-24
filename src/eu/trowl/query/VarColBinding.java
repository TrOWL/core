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
 * Bindings.java
 *
 * Created on 26 July 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

import com.truemesh.squiggle.Column;
import java.util.*;

/**
 *
 * @author ethomas
 */
public class VarColBinding {
    private HashMap<String,Set<Column>> data;
    
    /**
     * Creates a new instance of Bindings
     */
    public VarColBinding() {
        data = new HashMap<String,Set<Column>>();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void put(String variable, Column column) {
        if (data.containsKey(variable)) {
            Set<Column> s = data.get(variable);
            s.add(column);
            //data.put (key, (Object) s);
        } else {
            HashSet<Column> s = new HashSet<Column>();
            s.add (column);
            //data.put (key, (Object) s);
        }
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Set<Column> get(String variable) {
        return data.get (variable);
    }
    
    /**
     *
     * @param key
     * @return
     */
    public Column getOneOf(String variable) {
        Set<Column> s = data.get (variable);
        if (s == null) {
            return null;
        }
        return s.iterator().next();
    }
    
    /**
     *
     * @return
     */
    public Set<String> keySet() {
        return data.keySet();
    }
}
