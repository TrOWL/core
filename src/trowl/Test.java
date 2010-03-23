/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trowl;

import eu.trowl.owl.QLReasoner;
import eu.trowl.owl.Reasoner;
import eu.trowl.owl.ReasonerFactory;
import eu.trowl.query.Query;
import eu.trowl.query.QueryException;
import eu.trowl.query.QueryFactory;
import java.net.URI;
import java.util.logging.Logger;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLOntology;

/**
 *
 * @author ed
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //eu.trowl.query.Query q = QueryFactory.create("select ?s WHERE {?s a <http://example.org/test>}");
        //q.execute("default");
        ReasonerFactory rf = new ReasonerFactory();
        rf.setType(QLReasoner.class);
        Reasoner r = rf.load(new URI("file:/RO.owl"));
        r.consistent();

        for (OWLOntology o: r.getManager().getOntologies()) {
            for (OWLClass c: o.getReferencedClasses()) {
                System.out.println(c+"  " + r.getDescendantClasses(c));
            }
        }
    }

}
