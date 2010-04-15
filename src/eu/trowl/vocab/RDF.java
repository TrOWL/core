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

package eu.trowl.vocab;

import java.net.URI;

/**
 *
 * @author ethomas
 */
public class RDF {
    /**
     *
     */
    public static final URI NAMESPACE = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    /**
     *
     */
    public static final URI TYPE = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
/**
 *
 */
public static final URI PROPERTY = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#property");
/**
 *
 */
public static final URI STATEMENT = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement");
/**
 *
 */
public static final URI SUBJECT = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject");
/**
 *
 */
public static final URI PREDICATE = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate");
/**
 *
 */
public static final URI OBJECT = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#object");
/**
 *
 */
public static final URI BAG = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#Bag");
/**
 *
 */
public static final URI SEQ = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#Seq");
/**
 *
 */
public static final URI ALT = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#Alt");
/**
 *
 */
public static final URI VALUE = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#value");
/**
 *
 */
public static final URI LIST = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#List");
/**
 *
 */
public static final URI NIL = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#nil");
/**
 *
 */
public static final URI FIRST = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#first");
/**
 *
 */
public static final URI REST = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#rest");
/**
 *
 */
public static final URI XMLLITERAL = URI.create("http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral");
}
