package com.truemesh.squiggle.criteria;

import java.util.Set;

import com.truemesh.squiggle.Criteria;
import com.truemesh.squiggle.Matchable;
import com.truemesh.squiggle.Table;
import com.truemesh.squiggle.output.Output;

public class IsNotNullCriteria extends Criteria {
	private final Matchable matched;
	
	public IsNotNullCriteria(Matchable matched) {
		this.matched = matched;
	}

	@Override
	public void write(Output out) {
		matched.write(out);
		out.print(" IS NOT NULL");
	}

	public void addReferencedTablesTo(Set<Table> tables) {
		matched.addReferencedTablesTo(tables);
	}
}
