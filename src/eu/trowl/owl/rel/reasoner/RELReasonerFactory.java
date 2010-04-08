package eu.trowl.owl.rel.reasoner;

import org.semanticweb.owl.inference.OWLReasonerFactory;
import org.semanticweb.owl.model.OWLOntologyManager;

public class RELReasonerFactory implements OWLReasonerFactory {

	@Override
	public RELReasoner createReasoner(OWLOntologyManager manager) {
		// TODO Auto-generated method stub
		return new RELReasoner(manager);
	}

	@Override
	public String getReasonerName() {
		// TODO Auto-generated method stub
		return "REL";
	}

}
