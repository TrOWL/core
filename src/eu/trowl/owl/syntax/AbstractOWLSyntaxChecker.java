package eu.trowl.owl.syntax;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.io.ToStringRenderer;
import org.semanticweb.owl.model.OWLAxiom;

public abstract class AbstractOWLSyntaxChecker extends AbstractChecker implements OWLSyntaxChecker {
	
	private final Set<OWLAxiom> unsupportedAxioms = new HashSet<OWLAxiom>();
	protected boolean lazyMode = !true;
	
	public void close() {
		unsupportedAxioms.clear();
		valid = true;
	}
	
	public void reset() {
		close();
	}
	
	public void setLazyMode(boolean lazyMode) {
		this.lazyMode = lazyMode;
	}
	
	public boolean isLazyMode() {
		return lazyMode;
	}
	
	public Set<OWLAxiom> getUnsupportedAxioms() {
		return unsupportedAxioms;
	}
	
	protected void unsupported(OWLAxiom axiom) {
		unsupportedAxioms.add(axiom);
		valid = false;
		
		if (lazyMode)
			throw new OWLSyntaxException("Unsupported Axiom: " + ToStringRenderer.getInstance().getRendering(axiom), axiom);
	}

}
