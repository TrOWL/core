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
package eu.trowl.owl.quill;

import eu.trowl.owl.*;
import eu.trowl.util.*;
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
    protected Set<Node<OWLObjectProperty>> symmetricProperties;
    protected Set<Node<OWLObjectProperty>> asymmetricProperties;
    protected Set<Node<OWLObjectProperty>> reflexiveProperties;
    protected Set<Node<OWLObjectProperty>> irreflexiveProperties;

    protected Set<OWLDataProperty> dataProperties;
//    protected Set<OWLAnnotationProperty> annotationProperties;
    protected Set<OWLIndividual> individuals;

    protected Map<OWLIndividual, Set<OWLIndividual>> sameMap;
    protected Map<OWLIndividual, Set<OWLIndividual>> differentMap;
    protected Node<OWLClass> thing;
    protected Node<OWLClass> nothing;
    protected Node<OWLObjectProperty> topObjectProperty;
    protected Node<OWLDataProperty> topDataProperty;
    protected Map<OWLClass, Node<OWLClass>> classNodeMap;
    protected Map<OWLObjectProperty, Node<OWLObjectProperty>> objectPropertyNodeMap;
    protected Map<Node<OWLObjectProperty>, Set<Node<OWLObjectProperty>>> objectPropertyInverse;
    protected Map<OWLDataProperty, Node<OWLDataProperty>> dataPropertyNodeMap;
    protected Set<Node<OWLClass>> classNodes;
    protected Set<Node<OWLObjectProperty>> objectPropertyNodes;
    protected Set<Node<OWLDataProperty>> dataPropertyNodes;

    protected Map<Node<OWLClass>, Set<Node<OWLClass>>> classDisjunctions;
    protected Map<Node<OWLObjectProperty>, Set<Node<OWLObjectProperty>>> objectPropertyDisjunctions;
    protected Map<Node<OWLDataProperty>, Set<Node<OWLDataProperty>>> dataPropertyDisjunctions;

    protected Map<Node<OWLObjectProperty>, Set<Node<OWLClass>>> opDomainMap;
    protected Map<Node<OWLObjectProperty>, Set<Node<OWLClass>>> opRangeMap;
    protected Map<Node<OWLDataProperty>, Set<Node<OWLClass>>> dpDomainMap;
    protected Map<Node<OWLDataProperty>, Set<OWLDataRange>> dpRangeMap;

    public QLBase() {
        super();
    }

    protected void setupDataStructures() {
        classNodeMap = Types.newMap();
        objectPropertyNodeMap = Types.newMap();
        dataPropertyNodeMap = Types.newMap();
        abox = Types.newMap();
        types  = Types.newMap();
        classDisjunctions = Types.newMap();
        classNodes = Types.newSet();
        objectPropertyNodes = Types.newSet();
        dataPropertyNodes = Types.newSet();
        sameMap = Types.newMap();
        objectProperties = Types.newSet();
        reflexiveProperties = Types.newSet();
        symmetricProperties = Types.newSet();
        irreflexiveProperties = Types.newSet();
        asymmetricProperties = Types.newSet();
    }

    protected <T> Set<Set<T>> toSetSet (Set<Node<T>> in) {
        Set<Set<T>> out = Types.newSet();
        for (Node<T> n: in) {
            out.add(n); // stupid lousy type inference...
        }
        return out;
    }

    protected Set<Set<OWLDescription>> castInnerSetToDescription(Set<Set<OWLClass>> in) {
        Set<Set<OWLDescription>> out = Types.newSet();
        for (Set<OWLClass> inner: in) {
            Set<OWLDescription> newinner = Types.newSet();
            for (OWLClass obj: inner) {
                newinner.add((OWLDescription) obj);
            }
        }
        return out;
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
        Set<OWLIndividual> bag = Types.newSet();
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
            m.put(a, b);
        } else {
            m.get(a).addAll(b);
        }
    }

    protected void addDisjunction(OWLClass a, OWLClass b) {
        Node an = getNode(a);
        Node bn = getNode(b);

        if (objectPropertyDisjunctions.containsKey(an)) {
            objectPropertyDisjunctions.get(an).add(bn);
            objectPropertyDisjunctions.get(bn).addAll(objectPropertyDisjunctions.get(an));
        } else if (objectPropertyDisjunctions.containsKey(bn)) {
            objectPropertyDisjunctions.get(bn).add(an);
            objectPropertyDisjunctions.get(an).addAll(objectPropertyDisjunctions.get(an));
        } else {
            Set<Node<OWLObjectProperty>> d = Types.newSet();
            d.add(bn);
            d.add(an);
            objectPropertyDisjunctions.put(an, d);
            objectPropertyDisjunctions.put(bn, d);
        }
    }

    protected void addDisjunction(OWLDataProperty a, OWLDataProperty b) {
        Node an = getNode(a);
        Node bn = getNode(b);

        if (dataPropertyDisjunctions.containsKey(an)) {
            dataPropertyDisjunctions.get(an).add(bn);
            dataPropertyDisjunctions.get(bn).addAll(dataPropertyDisjunctions.get(an));
        } else if (objectPropertyDisjunctions.containsKey(bn)) {
            dataPropertyDisjunctions.get(bn).add(an);
            dataPropertyDisjunctions.get(an).addAll(dataPropertyDisjunctions.get(an));
        } else {
            Set<Node<OWLDataProperty>> d = Types.newSet();
            d.add(bn);
            d.add(an);
            dataPropertyDisjunctions.put(an, d);
            dataPropertyDisjunctions.put(bn, d);
        }
    }

    protected void addDisjunction(OWLObjectProperty a, OWLObjectProperty b) {
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

    
}
