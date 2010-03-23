/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.loader;

import eu.trowl.db.SQLBuilder;
import eu.trowl.owl.*;
import java.io.BufferedReader;
import java.io.Reader;
import eu.trowl.rdf.*;
import eu.trowl.vocab.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ed
 */
public class OWLQLLoader extends Loader {

    /**
     *
     */
    public static final String ID = "TROWL_LOADER_OWLQL";
    /**
     *
     */
    public static final boolean THREAD_SAFE = false;
    private OwlOntology ql;
    private Reasoner r;
    private String data;

    /**
     *
     */
    public OWLQLLoader() {
    }

    /**
     *
     * @throws LoaderInitException
     */
    public void init() throws LoaderInitException {
        try {
            ReasonerFactory rf = new ReasonerFactory();
            rf.setType(QLReasoner.class);
            r = rf.load(in);
        } catch (OntologyLoadException ex) {
            LoaderInitException ex2 = new LoaderInitException();
            ex2.initCause(ex);
            throw (ex2);
        }
        out.init();
    }

    public void run() {
        /*try {
        //r.load(data);
        data = "";
        r.store(db);
        } catch (OntologyLoadException ex) {
        Logger.getLogger(OWLQLLoader.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    /**
     *
     */
    public void finish() {
    }
}
