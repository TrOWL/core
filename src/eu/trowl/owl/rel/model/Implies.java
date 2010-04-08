package eu.trowl.owl.rel.model;

import java.util.ArrayList;


/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Implies implements ELCQEntry {

	public ArrayList<Basic> lhs = new ArrayList<Basic>();
	public Basic rhs = null;
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
		Implies other = (Implies) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + rhs;
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
		Implies other = (Implies) obj;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		if (rhs != other.rhs)
			return false;
		return true;
	}
	*/
}
