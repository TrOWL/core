package com.truemesh.squiggle;

import java.util.Set;

import com.truemesh.squiggle.output.Output;
import com.truemesh.squiggle.output.Outputable;

/**
 * @author <a href="joe@truemesh.com">Joe Walnes</a>
 * @author Nat Pryce
 */
public abstract class Criteria implements Outputable {
    public abstract void write(Output out);
	public abstract void addReferencedTablesTo(Set<Table> tables);
}
