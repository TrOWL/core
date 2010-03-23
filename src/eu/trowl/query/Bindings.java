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
