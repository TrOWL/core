package eu.trowl.owl.syntax;

import java.util.Set;

import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLOntology;

public interface OWLSyntaxChecker {
	
	void check(OWLOntology ontology);
	void close();
	void reset();
	
	boolean isValid();
	
	void setLazyMode(boolean lazyMode);
	boolean isLazyMode();
	
	Set<OWLAxiom> getUnsupportedAxioms();
	String getLanguageName();

}
