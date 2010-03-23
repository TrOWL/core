/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.rdf;

/**
 *
 * @author ed
 */
public class Quad extends Triple {
    /**
     *
     * @param s
     * @param p
     * @param o
     * @param ontology
     */
    public Quad(Resource s, Resource p, Node o, Node ontology) {
            subject = s;
            predicate = p;
            object = o;
            this.ontology = ontology;
        }

    /**
     *
     */
    public Quad() {
        super();
        ontology = null;
    }
    /**
     *
     */
    protected Node ontology;

    /**
     *
     * @return
     */
    public Node getSource() {
                return ontology;
            }
        }
