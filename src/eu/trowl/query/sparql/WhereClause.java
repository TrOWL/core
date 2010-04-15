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

import eu.trowl.query.*;
import java.util.*;

/**
 *
 * @author ed
 */
public class WhereClause {
	
	private GraphPattern basicGraphPattern;
	private List<GraphPattern> optionalGraphPatterns = new ArrayList<GraphPattern>();
	private Filter filter;
	
        /**
         *
         * @return
         */
        public GraphPattern getBasicGraphPattern() {
		return basicGraphPattern;
	}
	
        /**
         *
         * @param basicGraphPattern
         */
        public void setBasicGraphPattern(GraphPattern basicGraphPattern) {
		this.basicGraphPattern = basicGraphPattern;
	}
	
        /**
         *
         * @return
         */
        public List<GraphPattern> getOptionalGraphPatterns() {
		return optionalGraphPatterns;
	}
	
        /**
         *
         * @param optionalGraphPattern
         */
        public void addOptionalGraphPattern(GraphPattern optionalGraphPattern) {
		this.optionalGraphPatterns.add(optionalGraphPattern);
	}
	
        /**
         *
         * @return
         */
        public Filter getFilter() {
		return filter;
	}
	
        /**
         *
         * @param filter
         */
        public void setFilter(Filter filter) {
		this.filter = filter;
	}
	
    @Override
	public String toString() {
		return "BGP: " + String.valueOf(basicGraphPattern) +
			"; OPTIONAL: " + String.valueOf(optionalGraphPatterns) +
			"; FILTER: " + String.valueOf(filter);
	}


}
