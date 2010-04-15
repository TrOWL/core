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

package eu.trowl.owl;

import eu.trowl.db.DB;
import java.util.Set;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

/**
 *
 * @author ed
 */
public interface Reasoner extends OWLReasoner {

    /**
     * Stores the ontology currently loaded in the reasoner into the
     * default repository.
     */
    public void store();

    /**
     * Stores the ontology currently loaded in the reasoner into the
     * repository identified in the argument.
     * 
     * @param repository An identifier for the repository where the ontology is to be stored
     */
    public void store(String repository);

    /**
     * Store the ontology into the database object provided.
     *
     * @param repository a DB object where the ontology is to be stored
     */
    public void store(DB repository);

    /**
     * Stores the ontology currently loaded in the reasoner into the default
     * repository, and also store the negations of each named class so that
     * these can later be queried.
     */
    public void storeNegative();

    /**
     * Stores the ontology currently loaded in the reasoner into the default
     * repository, and also store the negations of each named class so that
     * these can later be queried.
     *
     * @param repository An identifier for the repository where the ontology is to be stored
     */
    public void storeNegative(String repository);

    /**
     * Stores the ontology currently loaded in the reasoner into the default
     * repository, and also store the negations of each named class so that
     * these can later be queried.
     *
     * @param repository a DB object where the ontology is to be stored
     */
    public void storeNegative(DB repository);

    /**
     * Check if the ontology currently loaded into the reasoner is allConsistent
     *
     * @return true if the ontology is allConsistent
     */
    public boolean allConsistent();

    /**
     * Check if all named classes in the ontology are allConsistent
     *
     * @return true if all named classes in the ontology are satisfiable
     */
    public boolean allSatisfiable();

    /**
     * Get a set of all unsatisfiable named classes in the ontology.
     *
     * @return A set of all named classes in the ontology which are not satisfiable
     */
    public Set<OWLClass> getUnsatisfiable();

    /**
     *
     * @param input
     * @throws OntologyLoadException
     */
    public void load(OWLOntologyManager input) throws OntologyLoadException;

    /**
     *
     */
    public void reload();

    /**
     * Close a particular named class under local closed world view. This means
     * that the reasoner will not create anonymous instances of this class to
     * maintain a allConsistent ontology.
     *
     * @param c A named class to close
     */
    public void close(OWLClass c);

    /**
     * Close a particular named class, and all its subclasses under local closed
     * world view. This means that the reasoner will not create anonymous
     * instances of this class to maintain a allConsistent ontology.
     *
     * @param c A named which is the parent of the subtree of the TBox you wish
     * to close
     */
    public void closeTree(OWLClass c);

    /**
     * Close a particular named property under local closed world view. This means
     * that the reasoner will not create anonymous instances of this property to
     * maintain a allConsistent ontology.
     *
     * @param p A named property to close
     */
    public void close(OWLObjectProperty p);

    /**
     * Close a particular named property, and all its subproperties under local closed
     * world view. This means that the reasoner will not create anonymous
     * instances of these properties to maintain a allConsistent ontology.
     *
     * @param p
     */
    public void closeTree(OWLObjectProperty p);

    /**
     *
     * @param c
     * @return
     */
    public Set<OWLAxiom> justifySatisfiable(OWLDescription c);

    public Set<Set<OWLAxiom>> justifySatisfiableAll(OWLDescription c);

    public Set<OWLAxiom> justifyInconsistent();

    public Set<Set<OWLAxiom>> justifyInconsistentAll();

    public Set<OWLAxiom> justify(OWLAxiom ax);

    public Set<Set<OWLAxiom>> justifyAll(OWLAxiom ax);

    /**
     *
     * @return
     */
    Class promoteTo();

    /**
     *
     * @return
     */
    public OWLOntologyManager getManager();

    public OWLDataFactory getDataFactory();

    public Object getUnderlyingReasoner();

    Set<OWLClass> getClasses();

    Set<OWLDataProperty> getDataProperties();

    Set<OWLIndividual> getIndividuals();

    Set<OWLObjectProperty> getObjectProperties();
    /* ADD NEW:
     * addClass(OwlNamedClass))
     * addIndividual(class, individual)
     * addPropertyInstance(property, i1, i2)
     * addObjectProperty(OwlObjectProperty)
     *
     * Should do some laziness here, set that dirty flag etc
     */
}
