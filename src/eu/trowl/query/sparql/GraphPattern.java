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

package eu.trowl.query.sparql;

import java.util.*;
import eu.trowl.rdf.*;

/**
 *
 * @author ed
 */
public class GraphPattern {
	private final List<SPARQLTriple> triples = new ArrayList<SPARQLTriple>();
	
        /**
         *
         */
        public GraphPattern() {
		
	}
	
        /**
         *
         * @param triple
         */
        public void addTriple(SPARQLTriple triple) {
		triples.add(triple);
	}

        @Override
	public String toString() {
		return triples.toString();
	}
	
        /**
         *
         * @return
         */
        public List<SPARQLTriple> getTriples() {
            return triples;
        }
}
