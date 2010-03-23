/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author ed
 */
public class Node {
    /**
     *
     */
    protected int type;
    /**
     *
     */
    protected String value;
    protected URI uriValue;
    /**
     *
     */
    protected String lang = "";
    /**
     *
     */
    protected String datatype;
    private static int anonCount = 0;

    /**
     *
     */
    public static final int TYPE_RESOURCE = 1;
    /**
     *
     */
    public static final int TYPE_LITERAL = 2;
    /**
     *
     */
    public static final int TYPE_VARIABLE = 4;
    /**
     *
     */
    public static final int TYPE_BNODE = 8;
    /**
     *
     */
    public static final String ANON_PREFIX = "_trowl:";

    /**
     *
     */
    public Node() {
        
    }

    /**
     *
     * @param value
     */
    public Node(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return datatype;
    }


    /**
     *
     * @param uri
     * @return
     */
    public static Node fromString(String uri) throws URISyntaxException {
        Node n = new Node();
        if (uri.startsWith("<") && uri.endsWith(">")) {
            n.setURI(new URI(uri.substring(1, uri.length() - 1)));
        } else {
            n.setLiteral(uri);
        }
        return n;
    }

    /**
     *
     * @param uri
     * @return
     */
    public static Node fromURI(URI uri) {
        Node n = new Node();
        n.setURI(uri);
        
        return n;
    }

    /**
     *
     * @param var
     * @return
     */
    public static Node fromVar(String var) {
        Node n = new Node();
        n.setVar(var);
        return n;
    }

    /**
     *
     * @param label
     * @return
     */
    public static Node bNode(String label) {
        Node n = new Node();
        n.setLabel(label);
        return n;
    }

    /**
     *
     * @return
     */
    public static Node bNode() {
        Node n = new Node();
        n.setLabel(ANON_PREFIX + String.valueOf(anonCount++));
        return n;
    }

    private void setURI(URI uri) {
        this.uriValue = uri;
        this.value = uri.toString();
        this.type = TYPE_RESOURCE;
    }

    private void setVar(String var) {
        if (var.startsWith("?") || var.startsWith("$"))
            this.value = var.substring(1);
        else
            this.value = var;
        this.type = TYPE_VARIABLE;
    }

    private void setLabel(String var) {
        this.value = var;
        this.type = TYPE_BNODE;
    }

    private void setLiteral(String var) {
        this.value = var;
        this.type = TYPE_LITERAL;
    }

    /**
     *
     * @return
     */
    public URI getURI() {
        if (type == Node.TYPE_RESOURCE || type == Node.TYPE_BNODE)
            return this.uriValue;
        return null;
    }

    /**
     *
     * @return
     */
    public String getLang() {
        if (type != Node.TYPE_LITERAL)
            return null;
        else
            return lang;
    }

    /**
     *
     * @return
     */
    public boolean isResource() {
        return (type == Node.TYPE_RESOURCE || type == Node.TYPE_BNODE);
    }

    /**
     *
     * @return
     */
    public boolean isVar() {
        return type == Node.TYPE_VARIABLE;
    }

    /**
     *
     * @return
     */
    public String getVar() {
        if (this.type == Node.TYPE_VARIABLE)
            return this.value;
        return null;
    }

    @Override
    public String toString() {
        if (this.isVar())
            return "?" + value;
        return value;
    }

    /**
     *
     * @param a
     * @return
     */
    public boolean sameAs(Object a) {
        if (a == null || value == null) {
            return false;
        }
        return (value.equals(a.toString()));
    }
}
