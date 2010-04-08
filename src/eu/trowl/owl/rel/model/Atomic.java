package eu.trowl.owl.rel.model;

import java.net.URI;

import org.semanticweb.owl.model.OWLClass;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Atomic extends Basic{

	public URI uri = null;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return uri.toString();
	}
	
	public Atomic()
	{
	}
	
	public Atomic(OWLClass concept)
	{
		uri = concept.getURI();
	}
	
	
}
