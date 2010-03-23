/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.rdf;

import java.net.URI;
import java.util.regex.*;

/**
 *
 * @author ed
 */
public class NQuad extends Quad {
    private static final String regex = "(<.*?>|_:.*?) +(<.*?>|_:.*?) +(<.*?>|_:.*?) +(<.*?>)";
    private static Pattern patt = Pattern.compile(regex);

    /**
     *
     * @param src
     * @throws MalformedQuadException
     */
    public NQuad(String src) throws MalformedQuadException {
        // parse here
        try {
            Matcher m = patt.matcher(src);
            m.find();
            this.subject = Resource.fromURI(m.group(1));
            this.predicate = Resource.fromURI(m.group(2));
            this.object = Node.fromString(m.group(3));
            this.ontology = Node.fromURI(new URI(m.group(4)));
        } catch (Exception e) {
            System.out.println("Error parsing: " + src);
            throw new MalformedQuadException();
        }
    }
}
