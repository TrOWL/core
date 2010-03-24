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

package eu.trowl.vocab;

import java.net.URI;

/**
 *
 * @author ethomas
 */
public class XSD {
    /**
     *
     */
    public final static URI NAMESPACE = URI.create("http://www.w3.org/2001/XMLSchema-datatypes");
    /**
     *
     */
    public final static URI INTEGER = URI.create("http://www.w3.org/2001/XMLSchema-datatypes#Integer");
    /**
     *
     */
    public final static URI DECIMAL = URI.create("http://www.w3.org/2002/07/owl#ObjectProperty");
    /**
     *
     */
    public final static URI DOUBLE = URI.create("http://www.w3.org/2002/07/owl#DatatypeProperty");
    /**
     *
     */
    public final static URI TRUE = URI.create("http://www.w3.org/2002/07/owl#Thing");
    /**
     *
     */
    public final static URI FALSE = URI.create("http://www.w3.org/2002/07/owl#Nothing");
    
    /**
     *
     */
    public final static URI FUNCTIONAL_PROPERTY = URI.create("http://www.w3.org/2002/07/owl#FunctionalProperty");
    /**
     *
     */
    public final static URI INVERSE_FUNCTIONAL_PROPERTY = URI.create("http://www.w3.org/2002/07/owl#InverseFunctionalProperty");
    /**
     *
     */
    public final static URI REFLEXIVE_PROPERTY = URI.create("http://www.w3.org/2002/07/owl#ReflexiveProperty");
    /**
     *
     */
    public final static URI TRANSITIVE_PROPERTY = URI.create("http://www.w3.org/2002/07/owl#TransitiveProperty");
    /**
     *
     */
    public final static URI SYMMETRIC_PROPERTY = URI.create("http://www.w3.org/2002/07/owl#SymmetricProperty");
    
}
