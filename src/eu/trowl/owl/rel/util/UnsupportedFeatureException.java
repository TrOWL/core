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
