/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.loader;

import java.io.BufferedReader;
import eu.trowl.db.*;
import eu.trowl.rdf.*;
import eu.trowl.vocab.*;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ed
 */
public class NTripleLoader extends RDFLoader {
    /**
     *
     */
    public static final String ID = "TROWL_LOADER_NTRIPLE";
    private URI base;

    /**
     *
     */
    public final static boolean THREAD_SAFE = true;

    /**
     *
     */
    public NTripleLoader() {
        super();
    }

    /**
     *
     */
    @Override
    public void init() {
        out.init();
    }

    @Override
    public void run() {
        NTripleReader reader = new NTripleReader();
        reader.read(this, in, this.base);
        out.close();
    }

    /**
     *
     * @param uri
     */
    public void setBase(String uri) {
        try {
            out.setURI(new URI(uri));
        } catch (URISyntaxException ex) {
            Logger.getLogger(NTripleLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    /**
     *
     * @param uri
     */
    public void setBase(URI uri) {
        base = uri;
    }

    /**
     *
     * @param t
     */
    public void processTriple(Triple t) {
                if (t.isType()) {
                    // if it is owl:Class or rdfs:Class, create new class, ditto properties, else create new thingy
                    if (t.getObject().sameAs(RDFS.CLASS) || t.getObject().equals(OWLRDF.CLASS)) {
                        out.createClass(t.getSubject().getURI());
                    } else if (t.getObject().sameAs(RDF.PROPERTY) || t.getObject().sameAs(OWLRDF.OBJECT_PROPERTY) || t.getObject().sameAs(OWLRDF.DATATYPE_PROPERTY)) {
                        out.createProperty(t.getSubject().getURI());
                    } else {
                        out.createIndividual(t.getSubject().getURI(), t.getObject().getURI());
                    }
                } else if (t.isSubClass()) {
                    out.setSubClassOf(t.getSubject().getURI(), t.getObject().getURI());
                //out.createClassPath(q.getSubject(), db.getClassPath(q.getObject()) + SQLBuilder.getPathHash(q.getObject()));
                } else if (t.isSubProperty()) {
                    out.setSubPropertyOf(t.getSubject().getURI(), t.getObject().getURI());
                } else if (t.isTBox()) {
                    // some other TBox axiom, what can these be?
                    // log and check sometime I think
                } else {
                    // new property instance
                    if (t.getObject().isResource()) {
                    out.createObjectPropertyInstance(t.getSubject().getURI(), t.getPredicate().getURI(), t.getObject().getURI());
                    } else {
                    out.createDatatypePropertyInstance(t.getSubject().getURI(), t.getPredicate().getURI(), t.getObject().toString(), t.getObject().getLang());
                        
                    }
                }
    }

    /**
     *
     */
    public void finish() {
        System.out.println("All threads finished, storing paths...");
        out.storePaths();
        out.rebuildIndices();
        out.close();
        System.out.println("All done!");
    }
}
