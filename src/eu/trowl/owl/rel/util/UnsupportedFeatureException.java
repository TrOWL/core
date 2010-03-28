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

package eu.trowl.owl.rel.util;

/**
 * File name: UnsupportedFeatureException.java
 * <p><b>Description</b>: This class is used to describe unsupported features from the
 * original ontology.</p>
 *
 * @author Quentin Reul (q.reul@abdn.ac.uk)
 * @version 1.0
 * Created on 13 Jun 2008
 */
@SuppressWarnings("serial")
public class UnsupportedFeatureException extends RuntimeException {
	public UnsupportedFeatureException() {
	    super();
	}
	
	public UnsupportedFeatureException(String e) {
	    super(e);
	}
}
