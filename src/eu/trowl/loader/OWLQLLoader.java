/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.loader;

import eu.trowl.owl.*;

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
        r.store(db);
    }

    /**
     *
     */
    public void finish() {
    }
}
