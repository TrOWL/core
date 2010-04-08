package eu.trowl.owl.rel.model;

import java.util.HashSet;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Singleton extends Basic {

	public Individual value;
	
	public HashSet<Basic> synonyms = new HashSet<Basic>();
	
	public Singleton(){
		hasModel = true;
	}
	
	public Singleton(Individual value)
	{
		this.value = value;
		value.singleton = this;
		hasModel = true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return value.toString();
	}
}
