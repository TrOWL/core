/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
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
