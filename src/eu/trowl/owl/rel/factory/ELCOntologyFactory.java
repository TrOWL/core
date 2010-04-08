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