/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trowl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import eu.trowl.loader.*;
import com.martiansoftware.jsap.*;

import eu.trowl.query.QueryFactory;
import eu.trowl.query.TextFormatter;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

/**
 *
 * @author ed
 */
public class Query {

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        FileOutputStream fos = new FileOutputStream("errors.txt");
        PrintStream ps = new PrintStream(fos);
        System.setErr(ps);
        
        JSAPResult config = getArgs(args);
        String query = "";

        if (config.getString("query") != null) {
            // parse query from arg
            query = config.getString("query");
        } else {
            query = readFileToString(new File(config.getString("file")));
        }

        eu.trowl.query.Query q = QueryFactory.create(query);
        eu.trowl.query.ResultSet rs = q.execute(config.getString("repo"));

        eu.trowl.query.ResultSetFormatter f = new TextFormatter(rs);
        System.out.println(f.format());
    //}
    }

    private static String readFileToString(File in) throws FileNotFoundException, IOException {
        StringBuffer out = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
        try {
            String buf;
            while ((buf = reader.readLine()) != null) {
                out.append (buf);
            }
        } finally {
            reader.close();
        }

        return out.toString();
    }

    private static JSAPResult getArgs(String[] args) throws Exception {
        SimpleJSAP jsap = new SimpleJSAP(
                "trowl.Query",
                "Performs queries on a TrOWL knowledge base",
                new Parameter[]{
                    new FlaggedOption("repo", JSAP.STRING_PARSER, "default", JSAP.NOT_REQUIRED, 'r', "repo",
                    "The repository you wish to query"),
                    new FlaggedOption("file", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_REQUIRED, 'f', "file",
                    "A file containing the query you wish to execute"),
                    new UnflaggedOption("query", JSAP.STRING_PARSER, JSAP.NO_DEFAULT, JSAP.NOT_REQUIRED, JSAP.NOT_GREEDY,
                    "A SPARQL query to execute across the repository")
                });

        JSAPResult result = jsap.parse(args);
        
        if (jsap.messagePrinted()) {
            //System.err.println("For more information use --help");
            System.exit(1);
        }
        return result;
    }
}
