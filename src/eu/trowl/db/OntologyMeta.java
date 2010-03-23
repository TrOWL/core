/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.db;

import java.util.*;
import eu.trowl.loader.TreeThing;
import java.net.URI;

/**
 *
 * @author ed
 */
public class OntologyMeta {
    /**
     *
     */
    protected Map<URI, TreeThing> classes;
    /**
     *
     */
    protected Map<URI, TreeThing> properties;
    /**
     *
     */
    protected Set<URI> ontologies;
    /**
     *
     */
    protected Set<URI> topClasses;
    /**
     *
     */
    protected Set<URI> topProperties;

    /**
     *
     */
    public OntologyMeta() {
        classes = Collections.synchronizedMap(new HashMap<URI, TreeThing>(1000));
        properties = Collections.synchronizedMap(new HashMap<URI, TreeThing>(1000, 0.9f));
        ontologies = Collections.synchronizedSet(new HashSet<URI>(1000, 0.9f));
        topClasses = Collections.synchronizedSet(new HashSet<URI>(1000));
        topProperties = Collections.synchronizedSet(new HashSet<URI>(1000, 0.9f));
    }

    /**
     *
     * @return
     */
    public Map<URI, TreeThing> getClasses() {
        return classes;
    }

    /**
     *
     * @return
     */
    public Set<URI> getOntologies() {
        return ontologies;
    }

    /**
     *
     * @return
     */
    public Map<URI, TreeThing> getProperties() {
        return properties;
    }

    /**
     *
     * @return
     */
    public Set<URI> getTopClasses() {
        return topClasses;
    }

    /**
     *
     * @return
     */
    public Set<URI> getTopProperties() {
        return topProperties;
    }
}
