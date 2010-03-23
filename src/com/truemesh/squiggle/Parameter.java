package com.truemesh.squiggle;

import java.util.Set;

import com.truemesh.squiggle.output.Output;

public class Parameter implements Matchable {
	public void write(Output out) {
		out.print("?");
	}

	public void addReferencedTablesTo(Set<Table> tables) {
	}
}
