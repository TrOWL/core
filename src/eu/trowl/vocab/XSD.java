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
