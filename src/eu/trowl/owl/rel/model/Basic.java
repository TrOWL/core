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
