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
