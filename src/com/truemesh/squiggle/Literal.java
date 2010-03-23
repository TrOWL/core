package com.truemesh.squiggle;

import java.util.Set;

import com.truemesh.squiggle.output.Outputable;

/**
 * A literal value, such as a number, string or boolean.
 * 
 * @author Nat Pryce
 * 
 */
public abstract class Literal implements Outputable, Matchable, Selectable {
	public void addReferencedTablesTo(Set<Table> tables) {
	}
}
