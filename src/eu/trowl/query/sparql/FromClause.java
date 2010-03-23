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
