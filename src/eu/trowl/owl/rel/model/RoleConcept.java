/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL .  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 University of Aberdeen
 */

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
