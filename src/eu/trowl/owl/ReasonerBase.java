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
