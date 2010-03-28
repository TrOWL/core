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
 * along with TrOWL .  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 University of Aberdeen
 */

package eu.trowl.owl.rel.factory;

import java.util.Set;

import org.semanticweb.owl.model.OWLLogicalAxiom;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import eu.trowl.owl.rel.model.ELCQOntology;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class ELCOntologyFactory {

	private final OWLOntologyManager manager;
	private final Set<OWLOntology> ontologies;
	PermanentELCOntologyBuilder builder;
	protected ELCQOntology elcontology = new ELCQOntology();


	public ELCOntologyFactory(Set<OWLOntology> ontologies, OWLOntologyManager manager)
	{
		this.manager = manager;
		this.ontologies = ontologies;
	}

	public void createbuilder() {
		builder = new PermanentELCOntologyBuilder(ontologies, manager, elcontology);		
	}

	public ELCQOntology createELOntology() {	
		for(OWLOntology ontology:ontologies){
			for(OWLLogicalAxiom axiom:ontology.getLogicalAxioms())
				axiom.accept(builder);
		}

		builder.OrderingCardinality();

		return elcontology = builder.getELOntology();
	}
}