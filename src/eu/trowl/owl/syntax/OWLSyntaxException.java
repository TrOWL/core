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
