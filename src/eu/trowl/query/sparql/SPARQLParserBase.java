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

package eu.trowl.query.sparql;

import eu.trowl.rdf.*;
import eu.trowl.vocab.*;
import eu.trowl.query.QuerySyntaxException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ed
 */
public abstract class SPARQLParserBase {
    private SPARQLQuery query;

    /**
     *
     * @return
     */
    public SPARQLQuery getQuery() {
        if (query == null) {
            try {
                query = new SPARQLQuery();
            } catch (Exception ex) {
                Logger.getLogger(SPARQLParserBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return query;
    }


    /**
     *
     * @param name
     * @param line
     * @param col
     * @return
     */
    public Node createVariable(String name, int line, int col) {
        return Node.fromVar(name);
    }

    /**
     *
     * @param uri
     * @param line
     * @param col
     * @return
     */
    public Node createNodeFromURI(String uri, int line, int col) throws QuerySyntaxException {
        try {
            return Node.fromURI(trimURI(uri));
        } catch (URISyntaxException ex) {
            QuerySyntaxException e = new QuerySyntaxException("Invalid URI in node: line " +  line + ", col " + col);
            e.initCause(ex);
            throw (e);
        }
    }

    public URI trimURI(String in) throws URISyntaxException {
        return new URI (in.substring(1, in.length() - 1));
    }

    /**
     *
     * @param label
     * @param line
     * @param col
     * @return
     */
    public Node createBNode(String label, int line, int col) {
        return Node.bNode(label);
    }

    /**
     *
     * @return
     */
    public Node createBNode() {
        return Node.bNode();
    }

    /**
     *
     * @param lex
     * @param lang
     * @param datatype
     * @return
     */
    public Node makeNode(String lex, String lang, Node datatype) {
        return Node.fromVar(lex); // hacky, and not very nice, language support TBC
    }

    /**
     *
     * @param lex
     * @param lang
     * @param uri
     * @return
     */
    public Node makeNode(String lex, String lang, URI uri) {
        return Node.fromVar(lex); // hacky, and not very nice, language support TBC
    }
    
    /**
     *
     * @param val
     * @return
     */
    public Node makeNodeInteger(String val) {
        return makeNode(val, null, XSD.INTEGER);
    }

    /**
     *
     * @param val
     * @return
     */
    public Node makeNodeDecimal(String val) {
        return makeNode(val, null, XSD.DECIMAL);
    }

    /**
     *
     * @param val
     * @return
     */
    public Node makeNodeDouble(String val) {
        return makeNode(val, null, XSD.DOUBLE);
    }

    /**
     *
     * @param uri
     */
    public void addGraphURI(URI uri) {
        query.getFromClause().addGraphURI(uri);
    }

    /**
     *
     * @param g
     * @param s
     * @param p
     * @param o
     */
    protected void insert(GraphPattern g, Node s, Node p, Node o) {
        g.addTriple(new SPARQLTriple(s,p,o));
    }

    /**
     *
     * @param in
     * @return
     */
    protected String stripQuotes(String in) {
        return in.substring(1, in.length() - 1);
    }

    /**
     *
     * @param in
     * @return
     */
    protected String stripQuotes3(String in) {
        return in.substring(3, in.length() - 3);
    }

    /**
     *
     * @param s
     * @param line
     * @param col
     * @return
     * @throws QuerySyntaxException
     */
    protected Node createNodeFromPrefixedName(String s, int line, int col) throws QuerySyntaxException {

        String prefix = s.substring(0, s.indexOf(':'));
        String postfix = s.substring(s.indexOf(':') + 1);
        URI expanded = getQuery().getPrefix(prefix);

        if (expanded == null)
            throw new QuerySyntaxException("Unresolved prefix name: " + s);
        try {
            return Node.fromURI(new URI(expanded.toString() + postfix));

        } catch (URISyntaxException ex) {
            QuerySyntaxException e = new QuerySyntaxException("Invalid URI in node: line " +  line + ", col " + col);
            e.initCause(ex);
            throw (e);
        }
    }

    /**
     *
     * @param prefix
     * @param uri
     */
    protected void setFixedPrefix(String prefix, URI uri) {
        getQuery().setPrefix(prefix.substring(0, prefix.length() - 1), uri);
    }

    /**
     *
     * @param in
     * @param line
     * @param col
     * @return
     */
    protected String unescapeString(String in, int line, int col) {
        return in;
    }
}

