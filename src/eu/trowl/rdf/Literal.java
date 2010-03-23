/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

/**
 *
 * @author ed
 */
public class Literal extends Node {
    /**
     *
     */
    public static final String DEFAULT_TYPE = "XMLLiteral";

    /**
     *
     * @param value
     */
    public Literal(String value) {
        this.value = value;
        this.datatype = DEFAULT_TYPE;
        this.type = Node.TYPE_LITERAL;
    }

    /**
     *
     * @param value
     * @return
     */
    public static Literal create(String value) {
        return new Literal(value);
    }

    /**
     *
     * @param value
     * @param wellFormed
     * @return
     */
    public static Literal create(String value, boolean wellFormed) {
        return new Literal(value);
    }

    /**
     *
     * @param value
     * @param dataType
     * @return
     */
    public static Literal createTyped(String value, String dataType) {
        Literal l =  new Literal(value);
        l.setType (dataType);
        return l;
    }

    /**
     *
     * @param value
     * @param language
     * @return
     */
    public static Literal create(String value, String language) {
        Literal l =  new Literal(value);
        l.setLang (language);
        return l;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.datatype = type;
    }

    /**
     *
     * @return
     */
    @Override
    public String getLang() {
        return lang;
    }

    /**
     *
     * @param lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return value;
        /*
        if (lang != null) {
            // make a languagey string
            return value + "@en";
        } else if (type != null) {
            // make a typed string
            return value + "^^<" + type + ">";
        } else {
            return value + "^^<" + DEFAULT_TYPE + ">";
        }*/
    }
}
