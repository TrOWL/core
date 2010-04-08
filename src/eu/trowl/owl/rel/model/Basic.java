package eu.trowl.owl.rel.model;

import java.util.HashSet;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Basic extends Description {

	public Implies entry;
	public Basic complement = null;
	public HashSet<Basic> subsumers = new HashSet<Basic>();
	public HashSet<Basic> equivalence = new HashSet<Basic>();
	public HashSet<Description> queue = new HashSet<Description>();
	public HashSet<RoleConcept> LeftConnection = new HashSet<RoleConcept>();
	public CardinalityEntry[] cardins = null;
	public boolean original = true;
	
	//to handle nominal
	public HashSet<Basic> ichain = new HashSet<Basic>();	
	public boolean hasModel = false;
	// end to handle nominal
	

	public void addleftconnection(Role s, Basic a) {
	// TODO Auto-generated method stub
	RoleConcept rc = new RoleConcept(s,a);
	LeftConnection.add(rc);
}

}
