/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

import eu.trowl.vocab.*;

/**
 *
 * @author ed
 */
public class Triple {
    /**
     *
     */
    protected Resource subject;
    /**
     *
     */
    protected Resource predicate;
        /**
         *
         */
        protected Node object;

        /**
         *
         */
        public Triple() {
            subject = null;
            predicate = null;
            object = null;
        }

        /**
         *
         * @param s
         * @param p
         * @param o
         */
        public Triple(Resource s, Resource p, Node o) {
            subject = s;
            predicate = p;
            object = o;
        }

        /**
         *
         * @param s
         * @param p
         * @param o
         */
        public Triple(Node s, Node p, Node o) {
            subject = Resource.fromNode(s);
            predicate = Resource.fromNode(p);
            object = o;
        }

        /**
         *
         * @return
         */
        public Resource getSubject() {
            return subject;
        }

        /**
         *
         * @return
         */
        public Node getObject() {
            return object;
        }

        /**
         *
         * @return
         */
        public Resource getPredicate() {
            return predicate;
        }

        /**
         *
         * @return
         */
        public boolean isType() {
            return RDF.TYPE.equals(predicate.getURI());
        }

        /**
         *
         * @return
         */
        public boolean isClass() {
            return isType() && (RDFS.CLASS.equals(object.getURI()) || OWLRDF.CLASS.equals(object.getURI()));
        }

        /**
         *
         * @return
         */
        public boolean isProperty() {
            return isType() && (RDF.PROPERTY.equals(object.getURI()) || OWLRDF.OBJECT_PROPERTY.equals(object.getURI()) || OWLRDF.DATATYPE_PROPERTY.equals(object.getURI()));
        }

        /**
         *
         * @return
         */
        public boolean isSubProperty() {
            return RDFS.SUB_PROPERTY_OF.equals(predicate.getURI());
        }

        /**
         *
         * @return
         */
        public boolean isSubClass() {
            return RDFS.SUB_CLASS_OF.equals(predicate.getURI());
        }

        public boolean isFoundAt() {
            return TrOWL.FOUNDAT.equals(predicate.getURI());
        }

        /**
         *
         * @return
         */
        public boolean isTBox() {
            return false;
        }

        @Override
        public String toString() {
            return subject.toString() + " " + predicate.toString() + " " + object.toString();
        }

        
}
