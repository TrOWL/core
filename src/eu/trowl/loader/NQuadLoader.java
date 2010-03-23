/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.loader;

import eu.trowl.rdf.*;
import eu.trowl.vocab.*;

/**
 *
 * @author ed
 */
public class NQuadLoader extends NTripleLoader {
    /**
     *
     */
    public final static boolean IS_THREAD_SAFE = true;

    /**
     *
     */
    public NQuadLoader() {
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
        NQuadReader reader = new NQuadReader();
        reader.read(this, in);
        out.close();
    }

    

    /**
     *
     */
    @Override
    public void finish() {
        System.out.println("All threads finished, storing paths...");
        out.storePaths();
        out.rebuildIndices();
        out.close();
        System.out.println("All done!");
    }
}
