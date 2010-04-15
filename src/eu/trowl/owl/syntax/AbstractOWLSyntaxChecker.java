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
