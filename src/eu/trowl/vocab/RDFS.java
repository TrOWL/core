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
public class RDFS {
    /**
     *
     */
    public static final URI NAMESPACE = URI.create("http://www.w3.org/2000/01/rdf-schema#");
    /**
     *
     */
    public static final URI DOMAIN = URI.create("http://www.w3.org/2000/01/rdf-schema#domain");
    /**
     *
     */
    public static final URI RANGE = URI.create("http://www.w3.org/2000/01/rdf-schema#range");
    /**
     *
     */
    public static final URI CLASS = URI.create("http://www.w3.org/2000/01/rdf-schema#Class");
    /**
     *
     */
    public static final URI SUB_CLASS_OF = URI.create("http://www.w3.org/2000/01/rdf-schema#subClassOf");
    /**
     *
     */
    public static final URI SUB_PROPERTY_OF = URI.create("http://www.w3.org/2000/01/rdf-schema#subPropertyOf");
    /**
     *
     */
    public static final URI IS_DEFINED_BY = URI.create("http://www.w3.org/2000/01/rdf-schema#isDefinedBy");
    /**
     *
     */
    public static final URI SEE_ALSO = URI.create("http://www.w3.org/2000/01/rdf-schema#seeAlso");
    /**
     *
     */
    public static final URI LITERAL = URI.create("http://www.w3.org/2000/01/rdf-schema#Literal");
    /**
     *
     */
    public static final URI DATATYPE = URI.create("http://www.w3.org/2000/01/rdf-schema#Datatype");
    /**
     *
     */
    public static final URI CONTAINER = URI.create("http://www.w3.org/2000/01/rdf-schema#Container");
    /**
     *
     */
    public static final URI CONTAINER_MEMBERSHIP_PROPERTY = URI.create("http://www.w3.org/2000/01/rdf-schema#ContainerMembershipProperty");
    /**
     *
     */
    public static final URI MEMBER = URI.create("http://www.w3.org/2000/01/rdf-schema#member");
}
