/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.owl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.semanticweb.owl.model.*;

/**
 *
 * @author ed
 */
public abstract class QLBase extends ReasonerBase {

    /**
     *
     */
    protected Map<OWLObjectProperty, Set<OWLObjectProperty>> orbox;
    /**
     *
     */
    protected Map<OWLDataProperty, Set<OWLDataProperty>> drbox;
    /**
     *
     */
    protected Map<OWLClass, Set<OWLClass>> subsumptions;
    /**
     *
     */
    protected Map<Node<OWLClass>, Set<Set<OWLIndividual>>> abox;
    protected Map<Set<OWLIndividual>, Set<Node<OWLClass>>> types;

    /**
     *
     */
    protected Map<Node<OWLObjectProperty>, Set<Pair<Set<OWLIndividual>,Set<OWLIndividual>>>> opInstances;
    /**
     *
     */
    protected Map<Node<OWLDataProperty>, Set<Pair<Set<OWLIndividual>,OWLConstant>>> dpInstances;
    protected Set<OWLClass> classes;
    protected Set<OWLObjectProperty> objectProperties;
    protected Set<OWLDataProperty> dataProperties;
//    protected Set<OWLAnnotationProperty> annotationProperties;
    protected Set<OWLIndividual> individuals;

    protected Map<OWLIndividual, Set<OWLIndividual>> sameMap;
    protected Node<OWLClass> thing;
    protected Node<OWLClass> nothing;
    protected Node<OWLObjectProperty> topObjectProperty;
    protected Node<OWLDataProperty> topDataProperty;
    protected Map<OWLClass, Node<OWLClass>> classNodeMap;
    protected Map<OWLObjectProperty, Node<OWLObjectProperty>> objectPropertyNodeMap;
    protected Map<OWLDataProperty, Node<OWLDataProperty>> dataPropertyNodeMap;
    protected Set<Node<OWLClass>> classNodes;
    protected Set<Node<OWLObjectProperty>> objectPropertyNodes;
    protected Set<Node<OWLDataProperty>> dataPropertyNodes;

    protected Map<Node<OWLClass>, Set<Node<OWLClass>>> classDisjunctions;

    public QLBase() {
        super();

        classNodeMap = new HashMap<OWLClass, Node<OWLClass>>();
        objectPropertyNodeMap = new HashMap<OWLObjectProperty, Node<OWLObjectProperty>>();
        dataPropertyNodeMap = new HashMap<OWLDataProperty, Node<OWLDataProperty>>();
        abox = new HashMap<Node<OWLClass>, Set<Set<OWLIndividual>>>();
        types  = new HashMap<Set<OWLIndividual>, Set<Node<OWLClass>>>();
        classDisjunctions = new HashMap<Node<OWLClass>, Set<Node<OWLClass>>>();
        classNodes = new HashSet<Node<OWLClass>>();
        objectPropertyNodes = new HashSet<Node<OWLObjectProperty>>();
        dataPropertyNodes = new HashSet<Node<OWLDataProperty>>();
        sameMap = new HashMap<OWLIndividual, Set<OWLIndividual>>();
        
    }

    protected Node<OWLClass> getNode(OWLClass c) {
        if (classNodeMap.containsKey(c))
            return classNodeMap.get(c);
        Node<OWLClass> node = new Node<OWLClass>();
        node.add(c);
        classNodeMap.put(c, node);
        classNodes.add(node);
        return (node);
    }

    protected Set<OWLIndividual> getBag(OWLIndividual i) {
        if (sameMap.containsKey(i))
            return sameMap.get(i);
        Set<OWLIndividual> bag = new HashSet<OWLIndividual>();
        bag.add(i);
        sameMap.put(i, bag);
        //classNodes.add(bag);
        return (bag);
    }

    protected Node<OWLObjectProperty> getNode(OWLObjectProperty c) {
        if (objectPropertyNodeMap.containsKey(c))
            return objectPropertyNodeMap.get(c);
        Node<OWLObjectProperty> node = new Node<OWLObjectProperty>();
        node.add(c);
        objectPropertyNodeMap.put(c, node);
                objectPropertyNodes.add(node);
        return (node);
    }

    protected Node<OWLDataProperty> getNode(OWLDataProperty c) {
        if (dataPropertyNodeMap.containsKey(c))
            return dataPropertyNodeMap.get(c);
        Node<OWLDataProperty> node = new Node<OWLDataProperty>();
        node.add(c);
        dataPropertyNodeMap.put(c, node);
        dataPropertyNodes.add(node);
        return (node);
    }

    //protected Set<OWLAnnotationProperty> dataProperties;
    protected <A, B> void addToMapList(Map<A, Set<B>> m, A a, B b) {
        if (!m.containsKey(a)) {
            m.put(a, new HashSet<B>());
        }
        m.get(a).add((b));
    }

    protected <A, B> void addToMapList(Map<A, Set<B>> m, A a, Set<B> b) {
        if (!m.containsKey(a)) {
            m.put(a, new HashSet<B>());
        }
        m.get(a).addAll(b);
    }

    protected void addDisjunction(OWLClass a, OWLClass b) {
        Node an = getNode(a);
        Node bn = getNode(b);

        if (classDisjunctions.containsKey(an)) {
            classDisjunctions.get(an).add(bn);
            classDisjunctions.get(bn).addAll(classDisjunctions.get(an));
        } else if (classDisjunctions.containsKey(bn)) {
            classDisjunctions.get(bn).add(an);
            classDisjunctions.get(an).addAll(classDisjunctions.get(an));
        } else {
            Set<Node<OWLClass>> d = new HashSet<Node<OWLClass>>();
            d.add(bn);
            d.add(an);
            classDisjunctions.put(an, d);
            classDisjunctions.put(bn, d);
        }
    }

    class Pair<T1, T2> {

        T1 first;
        T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() {
            return first;
        }

        public void setFirst(T1 first) {
            this.first = first;
        }

        public T2 getSecond() {
            return second;
        }

        public void setSecond(T2 second) {
            this.second = second;
        }

        public boolean contains(T1 candidate) {
            return (candidate == first || candidate == second);
        }

        @Override
        public String toString() {
            return first.toString() + ", " + second.toString();
        }
    }
}
