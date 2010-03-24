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
