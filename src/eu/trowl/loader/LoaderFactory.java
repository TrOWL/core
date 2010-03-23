/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.loader;

import java.util.ArrayList;
import java.util.List;
import java.io.Reader;
import java.io.BufferedReader;
import eu.trowl.db.SQLBuilder;
import eu.trowl.db.DBFactory;

import eu.trowl.db.OntologyMeta;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ed
 */
public class LoaderFactory {

    private List<Thread> workers;
    private Loader last;
    private Long startTime;
    private String base;
    private Set<String> oldBases;
    private Class loaderType = null;
    private static final Class DEFAULT_LOADER_TYPE = NTripleLoader.class;

    /**
     *
     */
    public LoaderFactory() {
        init();
    }

    /**
     *
     * @param base
     */
    public LoaderFactory(String base) {
        this.base = base;
        init();
    }

    private void init() {
        workers = new ArrayList<Thread>();
        oldBases = new HashSet<String>();
    }

    /**
     *
     * @param base
     */
    public void setBase(String base) {
        this.base = base;
    }

    /**
     *
     * @param loaderClass
     */
    public void setLoader(Class loaderClass) {
        if (loaderClass.isAssignableFrom(Loader.class)) {
            this.loaderType = loaderClass;
        }
    }

    /**
     *
     * @param input
     * @param number
     * @param repository
     * @param uri
     */
    public void createLoader(Reader input, int number, String repository, String uri) {
        base = uri;
        createLoader(input, number, repository);
    }

    /**
     *
     * @param input
     * @param number
     * @param repository
     */
    public void createLoader(Reader input, int number, String repository) {
        BufferedReader r = new BufferedReader(input);

        if (loaderType == null) {
            loaderType = DEFAULT_LOADER_TYPE;
        }

        System.out.println("1");
        OntologyMeta m = new OntologyMeta();
        Loader worker = null;
        if (!loaderType.isAssignableFrom(ThreadSafeLoader.class)) {
            number = 1; // for non thread safe loaders, force only one loader
        }

        System.out.println("2");
        for (int i = 0; i < number; i++) {
            try {
                        System.out.println("3");
                SQLBuilder builder = new SQLBuilder(DBFactory.construct(repository), new URI(base), m);
                        System.out.println("3.1");
                if (!oldBases.contains(base)) // we need to run init() to clear things out
                {
                  //  builder.init();
                } else {
                    oldBases.add(base);
                }
                        System.out.println("4");
                worker = (Loader) loaderType.newInstance();
        System.out.println("5");
                worker.setIn(r);
                worker.setOut(builder);
                worker.init();
                        System.out.println("6");
                Thread t = new Thread(worker);
                workers.add(t);
                        System.out.println("7");
            } catch (Exception ex) {
            }
        }

        last = worker;

        startTime = System.currentTimeMillis();

        for (Thread t : workers) {
            t.start();
        }

        System.out.print("Processing ontologies...");
    }

    /**
     *
     */
    public void waitAll() {
        try {
            for (Thread t : workers) {
                if (t.isAlive()) {
                    t.join();
                }
            }

            last.finish();
            System.out.println("Elapsed time: " + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException ex) {
            return;
        }
    }
}
