/*
 * This file is part of TrOWL.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

package eu.trowl.query.sparql;

import java.net.URI;
import java.util.*;

/**
 *
 * @author ed
 */
public class FromClause {
	
	private final Set<URI> graphURIs = new LinkedHashSet<URI>();

        /**
         *
         */
        public FromClause() {
		// TODO Auto-generated constructor stub
	}
	
        /**
         *
         * @return
         */
        public Set<URI> getGraphURIs() {
		return graphURIs;
	}
	
        /**
         *
         * @param graphURI
         */
        public void addGraphURI(URI graphURI) {
		this.graphURIs.add(graphURI);
	}

	
        /**
         *
         * @param graphURIs
         */
        public void addGraphURI(Collection<URI> graphURIs) {
		this.graphURIs.addAll(graphURIs);
	}
	
    @Override
	public String toString() {
		return graphURIs.toString();
	}

}
