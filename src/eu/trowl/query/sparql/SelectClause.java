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

import eu.trowl.rdf.*;
import java.util.*;

/**
 *
 * @author ed
 */
public class SelectClause {
	
	private final List<Node> variables;;

        /**
         *
         */
        public SelectClause() {
        variables = new ArrayList<Node>();
	}
	
        /**
         *
         * @return
         */
        public List<Node> getVariables() {
		return variables;
	}
	
        /**
         *
         * @param v
         */
        public void addVariable(Node v) {
		variables.add(v);
	}
	
    @Override
	public String toString() {
		return variables.toString();
	}
	
}
