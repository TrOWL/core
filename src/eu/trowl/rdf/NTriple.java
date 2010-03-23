/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.rdf;

import java.util.regex.*;

/**
 *
 * @author ed
 */
public class NTriple extends Triple {

    private static final String regex = "<(.*?)> +<(.*?)> +[<\"_](.*?)[>\"]?";
    private static Pattern patt = Pattern.compile(regex);

    /**
     *
     * @param s
     * @param p
     * @param o
     */
    public NTriple(Resource s, Resource p, Node o) {
            super (s,p,o);
        }

    /**
     *
     * @param src
     * @throws MalformedTripleException
     */
    public NTriple(String src) throws MalformedTripleException {
        // parse here
        try {
            Matcher m = patt.matcher(src);
            m.find();
            this.subject = Resource.fromURI(m.group(1));
            this.predicate = Resource.fromURI(m.group(2));
            this.object = Node.fromString(m.group(3));
        } catch (Exception e) {
            System.out.println("Error parsing: " + src);
            throw new MalformedTripleException();
        }
    }
}