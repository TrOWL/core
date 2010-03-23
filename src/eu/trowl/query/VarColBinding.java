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
