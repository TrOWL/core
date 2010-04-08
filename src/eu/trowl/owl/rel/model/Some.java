package eu.trowl.owl.rel.model;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Some extends Description {

	public Role role;
	public Description concept;
	
	public Some()
	{
		
	}
	
	public Some(Role role, Description concept){
		this.role = role;
		this.concept = concept;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
