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
