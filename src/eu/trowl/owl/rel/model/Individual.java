package eu.trowl.owl.rel.model;

import java.net.URI;

import org.semanticweb.owl.model.OWLIndividual;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Individual {

	public int id = -1;
	public URI uri = null;
	public boolean original = true;
	public Singleton singleton = null;
		
	public Individual(){
		
	}
	public Individual(OWLIndividual individual)
	{
		this.uri = individual.getURI();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return uri.toString();
	}
}
