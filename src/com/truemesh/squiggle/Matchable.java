package com.truemesh.squiggle;

import java.util.Set;

import com.truemesh.squiggle.output.Outputable;

/**
 * Something that can be part of a match expression in a where clause
 * 
 * @author Nat Pryce
 */
public interface Matchable extends Outputable {
	void addReferencedTablesTo(Set<Table> tables);
}
