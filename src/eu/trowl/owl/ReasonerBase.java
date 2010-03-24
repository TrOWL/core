/*
 * This file is part of TrOWL.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.owl;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import org.semanticweb.owl.model.OWLIndividual;

/**
 *
 * @author ed
 */
public abstract class ReasonerBase implements Reasoner {
    protected boolean isClassified = false;
    protected Logger log;

    public ReasonerBase() {
        log = Logger.getLogger(this.getClass().getName());
    }

    public boolean isClassified() {
        return this.isClassified;
    }

    static final Class promoteTo = null;

    protected <T> Set<T> flattenSetOfSets(Set<Set<T>> in) {
        Set<T> out = new HashSet<T>();
        for (Set<T> candidate : in) {
            out.addAll(candidate);
        }
        return out;
    }
}
