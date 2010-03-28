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
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;

import org.semanticweb.owl.model.OWLObjectProperty;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class Role {
	
	public int id = -1;
	public URI uri = null;
	public boolean original = true;
	public HashMap<Basic, HashSet<Basic>> Relations = new HashMap<Basic, HashSet<Basic>>();
	public HashSet<Role> subsumers = new HashSet<Role>();
	public HashSet<Role> equivalence = new HashSet<Role>();

	public Role inverse = null;
	public boolean functional = false;
	public boolean transitive = false;
	public HashMap<Role, HashSet<Role>> RightComposition = new HashMap<Role, HashSet<Role>>();
	public HashMap<Basic, Some> somes = new HashMap<Basic, Some>();
	
	public void addrelation(Basic a, Basic b) {
		// TODO Auto-generated method stub
		if(Relations.get(a) != null)
		{
			Relations.get(a).add(b);
		}
		else
		{
			HashSet<Basic> newa = new HashSet<Basic>();
			newa.add(b);
			Relations.put(a, newa);
		}
	}
	public Role()
	{
		subsumers.add(this);
	}
	public Role(OWLObjectProperty role)
	{
		this();
		this.uri = role.getURI();
	}

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
		Role other = (Role) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return uri.toString();
	}

}
