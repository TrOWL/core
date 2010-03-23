package eu.trowl.owl.syntax;

import org.semanticweb.owl.model.OWLAxiom;

public class OWLSyntaxException extends RuntimeException {

	private final OWLAxiom axiom;
	
	public OWLSyntaxException() {
		this(null, null);
	}

	public OWLSyntaxException(String message) {
		this(message, null);
	}

	public OWLSyntaxException(OWLAxiom axiom) {
		this(null, axiom);
	}

	public OWLSyntaxException(String message, OWLAxiom axiom) {
		super(message);
		this.axiom = axiom;
	}
	
	public OWLAxiom getAxiom() {
		return axiom;
	}

}
