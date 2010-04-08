package eu.trowl.owl.rel.model;


/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class ERestriction implements ELCQEntry {
	public Role role;
	public Basic concept;
	public int id;
	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ERestriction other = (ERestriction) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
