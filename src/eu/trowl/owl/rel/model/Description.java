package eu.trowl.owl.rel.model;

import java.util.HashSet;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Description {
	public int id = -1;

	public HashSet<ELCQEntry> Ohat = new HashSet<ELCQEntry>();
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
		Description other = (Description) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
