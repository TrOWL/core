/*
 * This file is part of TrOWL.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

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
