/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trowl;

import java.io.File;
import java.util.zip.GZIPInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.tools.bzip2.CBZip2InputStream;

import eu.trowl.loader.*;
import com.martiansoftware.jsap.*;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

/**
 *
 * @author ed
 */
public class LoadTool {

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // This will do LOADS of clever stuff eventully, including detecting what
        // type of transformation to apply to load data. For now, it loads NQuads
        // from a filename given as first argument
        JSAPResult config = getArgs(args);
        String baseURI = null;

        //FileOutputStream fos = new FileOutputStream("errors.txt");
        //PrintStream ps = new PrintStream(fos);
        //System.setErr(ps);

        System.out.println("Creating loader factory");
        LoaderFactory loaderFactory = new LoaderFactory();
        if (config.contains("uri")) {
            baseURI = config.getURL("uri").toString();
            loaderFactory.setBase(baseURI);
        }

        loaderFactory.setLoader(NTripleLoader.class);

        for (String filename : config.getStringArray("files")) {
            File f = new File(filename);
            Reader r;

            if (filename.endsWith(".zip")) {
            } else {

                if (filename.endsWith(".gz")) {
                    System.out.println ("Opening GZip compressed stream");
                    InputStream in = new FileInputStream(f);
                    InputStream gz = new GZIPInputStream(in);
                    r = new InputStreamReader(gz);
                }
                if (filename.endsWith(".bz2")) {
                    System.out.println ("Opening BZip2 compressed stream");
                    InputStream in = new FileInputStream(f);
                    InputStream gz = new CBZip2InputStream(in);
                    r = new InputStreamReader(gz);
                } else {
                    System.out.println ("Opening uncompressed stream");

                    r = new FileReader(f);
                }

                String currentURI = null;
                            System.out.println("Creating loader...");

                if (baseURI == null) {
                    currentURI = f.toURI().toString();
//String n3;
//                    StringReader reader = new StringReader(n3);
                    loaderFactory.createLoader(r, config.getShort("workers"), config.getString("repo"), currentURI);
                } else {
                    loaderFactory.createLoader(r, config.getShort("workers"), config.getString("repo"), baseURI);
                }
                            System.out.println("done!");

                loaderFactory.waitAll();
            }
        }
    //}
    }

    private static JSAPResult getArgs(String[] args) throws Exception {
        SimpleJSAP jsap = new SimpleJSAP(
                "trowl.Load",
                "Loads knowledge from RDF or OWL files into a TrOWL knowledge base",
                new Parameter[]{
                    new FlaggedOption("workers", JSAP.SHORT_PARSER, "1", JSAP.REQUIRED, 'w', "workers",
                    "The number of worker threads to initialise, may be overridden by non-thread-safe parsers."),
                    new FlaggedOption("repo", JSAP.STRING_PARSER, "default", JSAP.REQUIRED, 'r', "repository",
                    "The name of the TrOWL repository to use."),
                    new FlaggedOption("uri", JSAP.URL_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_REQUIRED, 'u', "uri",
                    "The base URI to use, default is file:/// URI, or will be taken from RDFXML base: if present."),
                    new UnflaggedOption("files", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.REQUIRED, JSAP.GREEDY,
                    "One or more names of files you would like to load.")
                });

        JSAPResult result = jsap.parse(args);
        if (jsap.messagePrinted()) {
            //System.err.println("For more information use --help");
            System.exit(1);
        }
        return result;
    }
}
