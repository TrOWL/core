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
