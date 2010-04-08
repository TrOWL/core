package eu.trowl.owl.rel.model;


/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class RoleConcept {
	
	public RoleConcept()
	{
		this(null,null);
	}

	public RoleConcept(Role role, Basic concept) {
		super();
		this.role = role;
		this.concept = concept;
	}
	public Role role;
	public Basic concept;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + concept.id;
		result = prime * result + role.id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleConcept other = (RoleConcept) obj;
		if (concept != other.concept)
			return false;
		if (role != other.role)
			return false;
		return true;
	}
}
