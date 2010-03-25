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
package eu.trowl.owl;

import eu.trowl.util.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

/**
 *
 * @author ed
 */
public abstract class ReasonerBase implements Reasoner {
    protected boolean isClassified = false;
    protected OWLOntologyManager manager;
    protected Set<OWLOntology> ontologies;
    protected Set<OWLOntology> groundOntologies; // these are the ontologies explicitly loaded, not including imports
    protected Logger log;

    public ReasonerBase() {
        log = Logger.getLogger(this.getClass().getName());
    }

    protected void _init() {
        ontologies = Types.newSet();
        groundOntologies = Types.newSet();
    }

    public boolean isClassified() {
        return this.isClassified;
    }

    static final Class promoteTo = null;

    protected <T> Set<T> flattenSetOfSets(Set<Set<T>> in) {
        Set<T> out = new HashSet<T>();
        for (Set<T> candidate : in) {
            out.addAll(candidate);
        }
        return out;
    }

    public Set<OWLClass> getClasses() {
        Set<OWLClass> out = Types.newSet();
        for (OWLOntology o: ontologies) {
            out.addAll(o.getReferencedClasses());
        }
        return out;
    }

    public Set<OWLObjectProperty> getObjectProperties() {
        Set<OWLObjectProperty> out = Types.newSet();
        for (OWLOntology o: ontologies) {
            out.addAll(o.getReferencedObjectProperties());
        }
        return out;
    }

    public Set<OWLDataProperty> getDataProperties() {
        Set<OWLDataProperty> out = Types.newSet();
        for (OWLOntology o: ontologies) {
            out.addAll(o.getReferencedDataProperties());
        }
        return out;
    }

    public Set<OWLIndividual> getIndividuals() {
        Set<OWLIndividual> out = Types.newSet();
        for (OWLOntology o: ontologies) {
            out.addAll(o.getReferencedIndividuals());
        }
        return out;
    }
}
