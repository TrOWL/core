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
