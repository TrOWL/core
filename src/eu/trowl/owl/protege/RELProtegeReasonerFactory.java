/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl.protege;

import eu.trowl.owl.ReasonerFactory;
import eu.trowl.owl.rel.reasoner.RELReasonerFactory;
import org.protege.editor.owl.model.inference.ProtegeOWLReasonerFactoryAdapter;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerFactory;
import org.semanticweb.owl.model.OWLOntologyManager;


/**
 *
 * @author ed
 */
public class RELProtegeReasonerFactory extends ProtegeOWLReasonerFactoryAdapter {

    public OWLReasoner createReasoner(OWLOntologyManager owlom) {
        OWLReasonerFactory rf = new RELReasonerFactory();
        return rf.createReasoner(owlom);
    }


        public void initialise() throws Exception {
    }


    public void dispose() throws Exception {
    }


    public boolean requiresExplicitClassification() {
        return true;
    }
}
