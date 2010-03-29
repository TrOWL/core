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
import eu.trowl.db.DB;
import eu.trowl.hashing.FNV;
import eu.trowl.util.Pair;
import eu.trowl.util.Types;
import eu.trowl.vocab.OWLRDF;
import eu.trowl.vocab.OWLRDF;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Level.*;
import java.util.logging.Logger;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasonerAdapter;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.StringInputSource;
import org.semanticweb.owl.model.AxiomType;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectComplementOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyInverse;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLPropertyExpression;
import org.semanticweb.owl.model.OWLSubPropertyAxiom;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;

/**
 *
 * @author ed
 */
public class QLReasoner extends QLBase implements Reasoner {

    private OWLOntologyManager manager;
    private Set<OWLOntology> onts;
    private OWLDataFactory factory;
    private Boolean consistent = null;

    private void loadTBox() {
        thing = getNode(manager.getOWLDataFactory().getOWLThing());
        nothing = getNode(manager.getOWLDataFactory().getOWLNothing());

        for (OWLOntology ont : onts) {
            for (OWLSubClassAxiom ax : ont.getAxioms(AxiomType.SUBCLASS)) {
                OWLDescription lhs = ax.getSubClass();
                OWLDescription rhs = ax.getSuperClass();

                OWLClass subclass;
                Set<OWLClass> superclasses;

                try {
                    if (!lhs.isAnonymous()) {
                        subclass = lhs.asOWLClass();
                    } else if (lhs.getClass().isAssignableFrom(OWLObjectSomeRestriction.class)) {
                        subclass = name(lhs);
                    } else {
                        continue;
                    }

                    Node<OWLClass> subNode = getNode(subclass);

                    if (isQL(rhs)) {
                        // rhs has to be intersection of some classes, negation of some class, or equivalent object property
                        if (rhs.getClass().isAssignableFrom(OWLObjectIntersectionOf.class)) {
                            for (OWLClass c : flattenIntersection((OWLObjectIntersectionOf) rhs)) {
                                Node<OWLClass> supNode = getNode(c);
                                supNode.addChild(subNode);
                            }
                        } else if (rhs.getClass().isAssignableFrom(OWLObjectSomeRestriction.class)) {
                            Node<OWLClass> supNode = getNode(name(rhs));
                            supNode.addChild(subNode);
                        } else if (rhs.getClass().isAssignableFrom(OWLObjectComplementOf.class)) {
                            OWLObjectComplementOf comp = (OWLObjectComplementOf) rhs;
                            OWLDescription of = comp.getOperand();

                            if (of.isAnonymous()) {
                                if (of.getClass().isAssignableFrom(OWLObjectSomeRestriction.class)) {
                                    addDisjunction(subclass, name((OWLObjectSomeRestriction) of));
                                } else {
                                    //System.out.println("Dropped a disjoint axiom");
                                }
                            } else {
                                addDisjunction(subclass, of.asOWLClass());
                            }
                        } else if (!rhs.isAnonymous()) {
                            Node<OWLClass> superNode = getNode(rhs.asOWLClass());
                            superNode.addChild(subNode);
                        }
                    }
                } catch (NotExpressibleException ex) {
                    //log.info(ex.getMessage());
                }
            }
        }

        for (Node<OWLClass> node : classNodes) {
            System.out.print(node);
            System.out.println(node.getParents());
            if (node.getParents().isEmpty() && !node.contains(factory.getOWLThing())) {
                thing.addChild(node);
            }
        }
    }

    private void loadRBox() {
        topObjectProperty = getNode(manager.getOWLDataFactory().getOWLObjectProperty(OWLRDF.TOP_OBJECT_PROPERTY));
        topDataProperty = getNode(manager.getOWLDataFactory().getOWLDataProperty(OWLRDF.TOP_DATA_PROPERTY));

        for (OWLOntology ont : onts) {
            for (OWLObjectProperty p: ont.getReferencedObjectProperties()) {
                getNode(p);
            }

            for (OWLDataProperty p: ont.getReferencedDataProperties()) {
                getNode(p);
            }
            for (OWLObjectSubPropertyAxiom ax : ont.getAxioms(AxiomType.SUB_OBJECT_PROPERTY)) {
                try {
                    OWLObjectProperty superprop;
                    OWLObjectProperty subprop;
                    if (ax.getSubProperty().isAnonymous()) {
                        subprop = name(ax.getSubProperty());
                    } else {
                        subprop = (OWLObjectProperty) ax.getSubProperty();
                    }
                    if (ax.getSuperProperty().isAnonymous()) {
                        superprop = name(ax.getSubProperty());
                    } else {
                        superprop = (OWLObjectProperty) ax.getSuperProperty();
                    }
                    getNode(superprop).addChild(getNode(subprop));
                } catch (NotExpressibleException ex) {
                }
            }

            for (OWLDisjointObjectPropertiesAxiom ax : ont.getAxioms(AxiomType.DISJOINT_OBJECT_PROPERTIES)) {
                try {
                    Set<OWLObjectProperty> disjunction = Types.newSet();
                    for (OWLObjectPropertyExpression ope : ax.getProperties()) {
                        OWLObjectProperty op = name(ope);
                        disjunction.add(op);
                    }
                    for (OWLObjectProperty op : disjunction) {
                        for (OWLObjectProperty op1 : disjunction) {
                            if (!op.equals(op1)) {
                                addDisjunction(op, op1);
                            }
                        }
                    }
                } catch (NotExpressibleException ex) {
                    log.info(ex.getMessage());
                }
            }

            for (OWLDataSubPropertyAxiom ax : ont.getAxioms(AxiomType.SUB_DATA_PROPERTY)) {
                OWLDataProperty superprop;
                OWLDataProperty subprop;

                if (!ax.getSubProperty().isAnonymous()
                        && !ax.getSuperProperty().isAnonymous()) {
                    try {
                        subprop = name(ax.getSubProperty());
                        superprop = name(ax.getSuperProperty());
                        getNode(superprop).addChild(getNode(subprop));
                    } catch (NotExpressibleException ex) {
                        // technically unreachable with current vesion of OWL, but hey, future proof ;)
                        log.info(ex.getMessage());
                    }
                }
            }

            for (OWLDisjointDataPropertiesAxiom ax : ont.getAxioms(AxiomType.DISJOINT_DATA_PROPERTIES)) {
                try {
                    Set<OWLDataProperty> disjunction = Types.newSet();
                    for (OWLDataPropertyExpression dpe : ax.getProperties()) {
                        OWLDataProperty dp = name(dpe);
                        disjunction.add(dp);
                    }
                    for (OWLDataProperty dp : disjunction) {
                        for (OWLDataProperty dp1 : disjunction) {
                            if (!dp.equals(dp1)) {
                                addDisjunction(dp, dp1);
                            }
                        }
                    }
                } catch (NotExpressibleException ex) {
                    log.info(ex.getMessage());
                }
            }
        }
        for (Node<OWLDataProperty> node : dataPropertyNodes) {
            if (node.getParents().isEmpty()) {
                topDataProperty.addChild(node);
            }

        }

        for (Node<OWLObjectProperty> node : objectPropertyNodes) {
            if (node.getParents().isEmpty()) {
                topObjectProperty.addChild(node);
            }

        }
    }

    private void loadABox() {
        for (OWLOntology ont : onts) {
            for (OWLClass c : ont.getReferencedClasses()) {
                for (OWLClassAssertionAxiom ax : ont.getClassAssertionAxioms(c)) {
                    OWLIndividual i = ax.getIndividual();
                    addToMapList(abox, getNode(c), getBag(i));
                    addToMapList(types, getBag(i), getNode(c));
                }

            }

            for (OWLIndividual subject : ont.getReferencedIndividuals()) {
                for (OWLObjectPropertyAssertionAxiom ax : ont.getObjectPropertyAssertionAxioms(subject)) {
                    OWLIndividual object = ax.getObject();
                    OWLObjectPropertyExpression predicate = ax.getProperty();
                    if (!predicate.isAnonymous()) {
                        OWLObjectProperty p = predicate.asOWLObjectProperty();
                    }

                }

                // might as well grab out data properties for this individual as well
                for (OWLDataPropertyAssertionAxiom ax : ont.getDataPropertyAssertionAxioms(subject)) {
                    OWLConstant object = ax.getObject();
                    OWLDataPropertyExpression predicate = ax.getProperty();
                    if (!predicate.isAnonymous()) {
                        OWLDataProperty p = predicate.asOWLDataProperty();
                    }

                }
            }
        }
    }

    private void normaliseIntersections() throws OWLOntologyChangeException {
        for (OWLOntology ont : onts) {
            for (OWLEquivalentClassesAxiom ax : ont.getAxioms(AxiomType.EQUIVALENT_CLASSES)) {
                ont.getClassAxioms();

            }

        }
    }

    private void normaliseEquivalentClasses() throws OWLOntologyChangeException {
        for (OWLOntology ont : onts) {
            for (OWLEquivalentClassesAxiom ax : ont.getAxioms(AxiomType.EQUIVALENT_CLASSES)) {
                Node<OWLClass> n = new Node<OWLClass>();
                for (OWLDescription e1 : ax.getDescriptions()) {
                    try {
                        OWLClass c1 = name(e1);
                        n.add(c1);
                        classNodeMap.put(c1, n);
                        for (OWLDescription e2 : ax.getDescriptions()) {
                            OWLClass c2 = name(e2);
                            if (c2 != null && !c1.equals(c2)) {
                                classNodeMap.put(c1, n);
                            }

                        }
                    } catch (NotExpressibleException ex) {
                        log.info(ex.getMessage());
                    }

                }
            }
        }
    }

    private void normaliseSameAs() throws OWLOntologyChangeException {
        for (OWLOntology ont : onts) {
            for (OWLSameIndividualsAxiom ax : ont.getAxioms(AxiomType.SAME_INDIVIDUAL)) {
                Set<OWLIndividual> sameBag = new HashSet<OWLIndividual>();
                sameBag.addAll(ax.getIndividuals());
                for (OWLIndividual ind : ax.getIndividuals()) {
                    getBag(ind).addAll(sameBag);
                }

            }
        }
    }

    private void findAllEquivalentClasses() {
        for (Node<OWLClass> a : classNodes) {
            for (Node<OWLClass> b : classNodes) {
                if (!a.equals(b)) {
                    if (a.hasDescendant(b) && b.hasDescendant(a)) {
                        Node.mergeNodes(a, b);
                    }

                }
            }
        }
    }

    private void findAllEquivalentObjectProperties() {
        for (Node<OWLObjectProperty> a : objectPropertyNodes) {
            for (Node<OWLObjectProperty> b : objectPropertyNodes) {
                if (!a.equals(b)) {
                    if (a.hasDescendant(b) && b.hasDescendant(a)) {
                        Node.mergeNodes(a, b);
                    }
                }
            }
        }
    }

    private void findAllEquivalentDataProperties() {
        for (Node<OWLDataProperty> a : dataPropertyNodes) {
            for (Node<OWLDataProperty> b : dataPropertyNodes) {
                if (!a.equals(b)) {
                    if (a.hasDescendant(b) && b.hasDescendant(a)) {
                        Node.mergeNodes(a, b);
                    }
                }
            }
        }
    }

    private Set<OWLClass> flattenIntersection(OWLObjectIntersectionOf in) {
        return flattenIntersection(in, new HashSet<OWLClass>());
    }

    private Set<OWLClass> flattenIntersection(OWLObjectIntersectionOf in, Set<OWLClass> bag) {
        for (OWLDescription c : in.getOperands()) {
            if (c.isAnonymous()) {
                if (c.getClass().isAssignableFrom(OWLObjectIntersectionOf.class)) {
                    flattenIntersection(
                            (OWLObjectIntersectionOf) c, bag);
                } else if (in.getClass().isAssignableFrom(OWLObjectSomeRestriction.class)) {
                    try {
                        bag.add(name((OWLObjectSomeRestriction) in));
                    } catch (NotExpressibleException ex) {
                        log.info(ex.getMessage());
                    }
                } else {
                    // not a QL class description, lets ignore it :D
                }
            } else {
                bag.add(in.asOWLClass());
            }

        }

        return bag;
    }

    /**
     *
     */
    public QLReasoner() {
        super();
    }

    /**
     *
     * @param r
     * @return
     */
    private OWLClass name(OWLDescription d) throws NotExpressibleException {
        if (!d.isAnonymous()) {
            return d.asOWLClass();
        }

        if (d instanceof OWLObjectSomeRestriction) {
            OWLObjectSomeRestriction r = (OWLObjectSomeRestriction) d;
            if (r.getProperty().isAnonymous()) {
                throw new RuntimeException("Property in someValuesFrom is not named");
            } else {
                URI newClassURI;
                try {
                    newClassURI = new URI("urn:class:" + FNV.hash32("neg:" + r.getProperty().asOWLObjectProperty().getURI().toString()));
                } catch (URISyntaxException ex) {
                    throw new RuntimeException("Managed to create an invalid URI!");
                }

                OWLClass newClass =
                        factory.getOWLClass(newClassURI);
                return newClass;
            }

        }
        throw new NotExpressibleException("Class description not expressible in OWL QL: " + d.toString());
    }

    private OWLDataProperty name(OWLDataPropertyExpression p) throws NotExpressibleException {
        return p.asOWLDataProperty();
    }

    private OWLObjectProperty name(OWLObjectPropertyExpression p) throws NotExpressibleException {
        if (!p.isAnonymous()) {
            return (OWLObjectProperty) p;
        }

        if (p instanceof OWLObjectPropertyInverse) {
            OWLObjectSomeRestriction r = (OWLObjectSomeRestriction) p;
            if (r.getProperty().isAnonymous()) {
                throw new RuntimeException("Property in someValuesFrom is not named");
            } else {
                URI newClassURI;
                try {
                    newClassURI = new URI("urn:class:" + FNV.hash32("neg:" + r.getProperty().asOWLObjectProperty().getURI().toString()));
                } catch (URISyntaxException ex) {
                    throw new RuntimeException("Managed to create an invalid URI!");
                }

                OWLObjectProperty newProp =
                        factory.getOWLObjectProperty(newClassURI);
                return newProp;
            }

        }
        throw new NotExpressibleException("Property description not expressible in OWL QL: " + p.toString());
    }

    public void store() {
    }

    public void store(String repository) {
    }

    public void store(DB repository) {
    }

    public boolean allConsistent() {
        if (consistent == null) {
            consistent = new Boolean(consistencyCheck());
        }

        return consistent.booleanValue();
    }

    private boolean consistencyCheck() {
        //boolean result = true;

        if (getNode(factory.getOWLThing()).contains(factory.getOWLNothing())) {
            return false;
        }

        for (Node c1 : classDisjunctions.keySet()) {
            for (Node c2 : classDisjunctions.get(c1)) {
                if (c1 != c2) {
                    if (!Collections.disjoint(getInstances(c1), getInstances(c2))) {
                        return false;
                    }

                }
            }
        }

        for (Node p : asymmetricProperties) {
            // asymmetric cannot have a p b and b p a:
            Set<Pair<Set<OWLIndividual>, Set<OWLIndividual>>> content = opInstances.get(p);
            for (Pair<Set<OWLIndividual>, Set<OWLIndividual>> pair : content) {
                if (content.contains(pair.reverse())) {
                    return false;
                }

            }
        }

        for (Node p1 : objectPropertyDisjunctions.keySet()) {
            for (Node p2 : objectPropertyDisjunctions.get(p1)) {
                if (!p1.equals(p2)) {
                    if (!Collections.disjoint(getInstances(p1), getInstances(p2))) {
                        return false;
                    }

                }
            }
        }

        for (Node p1 : dataPropertyDisjunctions.keySet()) {
            for (Node p2 : dataPropertyDisjunctions.get(p1)) {
                if (!p1.equals(p2)) {
                    if (!Collections.disjoint(getInstances(p1), getInstances(p2))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /*    private boolean overlap(Collection a, Collection b) {
    Collection big;
    Collection small;

    if (a.size() > b.size()) {
    big = a;
    small =
    b;
    } else {
    big = b;
    small = a;
    }

    for (Object candidate : small) {
    if (big.contains(candidate)) {
    return true;
    }

    }

    return false;
    }*/
    public boolean allSatisfiable() {
        return getUnsatisfiable().isEmpty();
    }

    public Set<OWLClass> getUnsatisfiable() {
        try {
            Set<OWLClass> out = flattenSetOfSets(getSubClasses(factory.getOWLNothing()));
            out.add(factory.getOWLNothing());
            return out;
        } catch (OWLReasonerException ex) {
            Logger.getLogger(QLReasoner.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param c
     * @return
     */
    public Set<OWLIndividual> getDirectInstances(OWLDescription c) {
        try {
            return flattenSetOfSets(abox.get(getNode(name(c))));
        } catch (NotExpressibleException ex) {
            log.info(ex.getMessage());
            return Collections.EMPTY_SET;
        }

    }

    public Set<OWLIndividual> getInstances(OWLDescription c) {
        try {
            Set<OWLClass> subClasses = flattenSetOfSets(getSubClasses(c.asOWLClass()));
            Set<OWLIndividual> out = new HashSet<OWLIndividual>();
            for (OWLClass subc : subClasses) {
                out.addAll(getDirectInstances(c));
            }

            return out;
        } catch (OWLReasonerException ex) {
            Logger.getLogger(QLReasoner.class.getName()).log(Level.SEVERE, null, ex);









            return null;
        }
    }

    public Set<Pair<OWLIndividual, OWLIndividual>> getInstances(OWLObjectProperty p) {
        Set<Node<OWLObjectProperty>> subPropertyNodes = getNode(p).getDescendants();
        Set<Pair<OWLIndividual, OWLIndividual>> out = new HashSet<Pair<OWLIndividual, OWLIndividual>>();
        for (Node subNode : subPropertyNodes) {
            for (Pair<Set<OWLIndividual>, Set<OWLIndividual>> pair : opInstances.get(subNode)) {
                // DO SOMETHING HEREE
            }
        }
        return out;
    }

    protected Set<Pair<Set<OWLIndividual>, Set<OWLIndividual>>> getInstanceBags(OWLObjectProperty p) {
        Set<Node<OWLObjectProperty>> subPropertyNodes = getNode(p).getDescendants();
        Set<Pair<Set<OWLIndividual>, Set<OWLIndividual>>> out = new HashSet<Pair<Set<OWLIndividual>, Set<OWLIndividual>>>();
        for (Node subNode : subPropertyNodes) {
            out.addAll(opInstances.get(subNode));
        }

        return out;
    }

    protected Set<OWLIndividual> getInstances(Node<OWLClass> c) {
        Set<OWLIndividual> out = new HashSet<OWLIndividual>();

        for (Node<OWLClass> child : c.getDescendants()) {
            out.addAll(flattenSetOfSets(abox.get(child)));
        }

        return out;
    }

    public boolean subsumes(OWLClass superclass, OWLClass subclass) {
        try {
            return getDirectSubClasses(superclass).contains(subclass) || flattenSetOfSets(getSubClasses(superclass)).contains(subclass);
        } catch (OWLReasonerException ex) {
            Logger.getLogger(QLReasoner.class.getName()).log(Level.SEVERE, null, ex);









            return false;
        }
    }

    private Set<OWLClass> recurseClassTree(OWLClass c, Set<OWLClass> set) {
        Set<OWLClass> newclasses = getDirectSubClasses(c);
        for (OWLClass d : newclasses) {
            if (!set.contains(d)) {
                recurseClassTree(d, set);
            }

            set.addAll(newclasses);
        }

        return set;
    }

    public Set<OWLClass> getDirectSubClasses(OWLDescription c) {
        return subsumptions.get(c.asOWLClass());
    }

    public void reload() {
        // nothing needed here, we never change the ontology :D
    }

    /**
     *
     * @param c
     */
    public void close(OWLClass c) {
        throw new UnsupportedOperationException("Not supported in QL Reasoner, please use DL Reasoner.");
    }

    /**
     *
     * @param c
     */
    public void closeTree(OWLClass c) {
        throw new UnsupportedOperationException("Not supported in QL Reasoner, please use DL Reasoner.");
    }

    public void storeNegative() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void storeNegative(String repository) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void storeNegative(DB repository) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSatisfiable(OWLDescription c) {
        return !nothing.contains(c.asOWLClass());
    }

    /**
     *
     * @param a
     * @return
     */
    public Set<OWLAxiom> justify(OWLAxiom a) {
        return new HashSet<OWLAxiom>();
    }

    public Set<OWLAxiom> justifySatisfiable(OWLDescription c) {
        return new HashSet<OWLAxiom>();
    }

    public void load(OWLOntologyManager input) throws OntologyLoadException {
        this.manager = input;
        this.factory = manager.getOWLDataFactory();
        this.onts = manager.getOntologies();
        for (OWLOntology o : onts) {
            this.onts.addAll(manager.getImportsClosure(o));
        }

        init();
    }

    private void init() throws OntologyLoadException {
        try {
            setupDataStructures();
            normaliseEquivalentClasses();

            normaliseSameAs();

            loadTBox();

            loadRBox();

            loadABox();

            findAllEquivalentClasses();

            findAllEquivalentObjectProperties();

            findAllEquivalentDataProperties();

        } catch (OWLOntologyChangeException ex) {
            OntologyLoadException ex2 = new OntologyLoadException();
            ex.initCause(ex2);
            throw (ex2);
        }

    }

    public Class promoteTo() {
        return DLReasoner.class;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public void close(OWLObjectProperty p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeTree(OWLObjectProperty p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLAxiom>> justifySatisfiableAll(OWLDescription c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLAxiom> justifyInconsistent() {
        return new HashSet<OWLAxiom>();
    }

    public Set<Set<OWLAxiom>> justifyInconsistentAll() {
        return new HashSet<Set<OWLAxiom>>();
    }

    public Set<Set<OWLAxiom>> justifyAll(OWLAxiom ax) {
        return new HashSet<Set<OWLAxiom>>();
    }

    public OWLDataFactory getDataFactory() {
        return manager.getOWLDataFactory();
    }

    public Object getUnderlyingReasoner() {
        return this;
    }

    public Set<OWLClass> classifyIndividual(OWLIndividual i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private boolean isQL(OWLDescription d) {
        if (!d.isAnonymous()) {
            return true; // named classes are fine
        } else {
            if (d instanceof OWLObjectComplementOf) {
                return isQL((OWLObjectComplementOf) d);
            } else if (d instanceof OWLObjectIntersectionOf) {
                return isQL((OWLObjectIntersectionOf) d); //recurse and check the intersection
            } else if (d instanceof OWLObjectSomeRestriction) {
                return true; // recurse and check the existential
            }

        }

        return false; // other class descriptions are not allowed
    }

    private boolean isQLBasic(OWLDescription d) {
        if (!d.isAnonymous()) {
            return true; // named classes are fine
        } else {
            if (d instanceof OWLObjectSomeRestriction) {
                return isQL((OWLObjectSomeRestriction) d); // recurse and check the existential
            }

        }

        return false; // other class descriptions are not allowed
    }

    private boolean isQL(OWLObjectIntersectionOf d) {
        // QL requires that each of the operands in an intersection is also QL
        for (OWLDescription c : d.getOperands()) {
            if (!isQL(c)) {
                return false;
            }

        }
        return true;
    }

    private boolean isQL(OWLObjectSomeRestriction d) {
        // OWL QL only allows "property some Thing", anything else is not QL
        if (d.getFiller().isOWLThing()) {
            return true;
        }

        return false;
    }

    private boolean isQLBasic(OWLObjectIntersectionOf d) {
        for (OWLDescription c : d.getOperands()) {
            if (!isQL(c)) {
                return false;
            }

        }
        return true;
    }

    public boolean isConsistent(OWLOntology ont) throws OWLReasonerException {
        //why oh why is this passed an ontology??
        if (!onts.contains(ont)) {
            loadOntologies(Collections.singleton(ont));
        }

        return this.allConsistent();
    }

    public void loadOntologies(Set<OWLOntology> input) throws OWLReasonerException {
        for (OWLOntology in : input) {
            onts.addAll(manager.getImportsClosure(in));
        }

        try {
            init();
        } catch (OntologyLoadException ex) {
            //throw new OWLReasonerException(ex);
        }

    }

    public void classify() throws OWLReasonerException {
        // NOTHING   
    }

    public boolean isRealised() throws OWLReasonerException {
//        throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    public void realise() throws OWLReasonerException {
        // do nothing, Quill does not require this!
    }

    public boolean isDefined(OWLClass c) throws OWLReasonerException {
        return classes.contains(c);
    }

    public boolean isDefined(OWLObjectProperty op) throws OWLReasonerException {
        return this.objectProperties.contains(op);
    }

    public boolean isDefined(OWLDataProperty dp) throws OWLReasonerException {
        return this.dataProperties.contains(dp);
    }

    public boolean isDefined(OWLIndividual ind) throws OWLReasonerException {
        return individuals.contains(ind);
    }

    public Set<OWLOntology> getLoadedOntologies() {
        return onts;
    }

    public void unloadOntologies(Set<OWLOntology> onts) throws OWLReasonerException {
        for (OWLOntology ont : onts) {
            this.onts.removeAll(manager.getImportsClosure(ont));
        }

    }

    public void clearOntologies() throws OWLReasonerException {
        unloadOntologies(onts);
    }

    public void dispose() throws OWLReasonerException {
        // nothing to do here
    }

    public boolean isSubClassOf(OWLDescription a, OWLDescription b) throws OWLReasonerException {
        try {
            OWLClass aa = name(a);
            OWLClass bb = name(b);
            if (getNode(aa).hasDescendant(getNode(bb))) {
                return true;
            }

            return false;
        } catch (NotExpressibleException ex) {
            throw new ReasonerException(ex);
        }

    }

    public boolean isEquivalentClass(OWLDescription a, OWLDescription b) throws OWLReasonerException {
        OWLClass ca = name(a);
        OWLClass cb = name(b);
        return getNode(ca).contains(cb);
    }

    public Set<Set<OWLClass>> getSuperClasses(OWLDescription arg0) throws OWLReasonerException {
        Node<OWLClass> node = getNode(name(arg0));

        Set<Set<OWLClass>> out = new HashSet<Set<OWLClass>>();
        for (Node<OWLClass> parent : node.getParents()) {
            out.add(parent.asSet());
        }

        return out;
    }

    public Set<Set<OWLClass>> getAncestorClasses(OWLDescription arg0) throws OWLReasonerException {
        Node<OWLClass> node = getNode(name(arg0));

        Set<Set<OWLClass>> out = new HashSet<Set<OWLClass>>();
        for (Node<OWLClass> parent : node.getAscendants()) {
            out.add(parent.asSet());
        }

        return out;
    }

    public Set<Set<OWLClass>> getSubClasses(OWLDescription arg0) throws OWLReasonerException {
        Node<OWLClass> node = getNode(name(arg0));

        Set<Set<OWLClass>> out = new HashSet<Set<OWLClass>>();
        for (Node<OWLClass> child : node.getChildren()) {
            out.add(child.asSet());
        }

        return out;
    }

    public Set<Set<OWLClass>> getDescendantClasses(OWLDescription arg0) throws OWLReasonerException {
        Node<OWLClass> node = getNode(name(arg0));

        Set<Set<OWLClass>> out = new HashSet<Set<OWLClass>>();
        for (Node<OWLClass> child : node.getDescendants()) {
            out.add(child.asSet());
        }

        return out;
    }

    public Set<OWLClass> getEquivalentClasses(OWLDescription arg0) throws OWLReasonerException {
        OWLClass c = name(arg0);
        return getNode(c);
    }

    public Set<OWLClass> getInconsistentClasses() throws OWLReasonerException {
        return flattenSetOfSets(getDescendantClasses(factory.getOWLNothing()));
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual i, boolean direct) throws OWLReasonerException {
        Set<Set<OWLClass>> out = Types.newSet();
        if (!direct) {
            for (Set<OWLClass> set : types.get(getBag(i))) {
                out.add(set);
            }

        } else {
            for (Node<OWLClass> node : types.get(getBag(i))) {
                out.add(node);
                out.addAll(node.getDescendants());
            }

        }

        return out;
    }

    public Set<OWLIndividual> getIndividuals(OWLDescription cd, boolean direct) throws OWLReasonerException {
        Node<OWLClass> node = getNode(name(cd));
        Set<OWLIndividual> out = Types.newSet();
        out =
                flattenSetOfSets(abox.get(node));
        if (!direct) {
            for (Node<OWLClass> descendant : node.getDescendants()) {
                for (Set<OWLIndividual> bag : abox.get(descendant)) {
                    out.addAll(bag);
                }

            }
        }
        return out;
    }

    public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual i) throws OWLReasonerException {
        // rather horribly inefficient, fix!

        Set<OWLIndividual> bag = getBag(i);
        Map<OWLObjectProperty, Set<OWLIndividual>> out = Types.newMap();
        for (Node<OWLObjectProperty> node : opInstances.keySet()) {
            boolean sym = symmetricProperties.contains(node);
            for (Pair<Set<OWLIndividual>, Set<OWLIndividual>> pair : opInstances.get(node)) {
                if (pair.getFirst().equals(bag)) {
                    for (OWLObjectProperty p : node) {
                        addToMapList(out, p, pair.getSecond());
                    }

                }

                // Symmetric properties go both ways!
                if (sym && pair.getSecond().equals(bag)) {
                    for (OWLObjectProperty p : node) {
                        addToMapList(out, p, pair.getFirst());
                    }

                }
            }
        }

        return out;
    }

    public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual i) throws OWLReasonerException {
        // rather horribly inefficient, fix!

        Set<OWLIndividual> bag = getBag(i);
        Map<OWLDataProperty, Set<OWLConstant>> out = Types.newMap();
        for (Node<OWLDataProperty> node : dpInstances.keySet()) {
            for (Pair<Set<OWLIndividual>, OWLConstant> pair : dpInstances.get(node)) {
                if (pair.getFirst().equals(bag)) {
                    for (OWLDataProperty p : node) {
                        addToMapList(out, p, pair.getSecond());
                    }

                }
            }
        }

        return out;
    }

    public boolean hasType(OWLIndividual i, OWLDescription cd, boolean direct) throws OWLReasonerException {
        OWLClass c = name(cd);
        Node<OWLClass> node = getNode(c);
        Set<OWLIndividual> bag = getBag(i);
        Set<Node<OWLClass>> thistypes = types.get(bag);
        if (direct) {
            return thistypes.contains(node);
        } else {
            for (Node descendant : node.getDescendants()) {
                if (thistypes.contains(descendant)) {
                    return true;
                }

            }
            return false;
        }

    }

    public boolean hasObjectPropertyRelationship(OWLIndividual i1, OWLObjectPropertyExpression ope, OWLIndividual i2) throws OWLReasonerException {
        Set<OWLIndividual> bag1 = getBag(i1);
        Set<OWLIndividual> bag2 = getBag(i2);
        OWLObjectProperty op = name(ope);
        Node<OWLObjectProperty> node = getNode(op);

        boolean result = false;
        if (symmetricProperties.contains(node)) {
            result = getInstanceBags(op).contains(new Pair<Set<OWLIndividual>, Set<OWLIndividual>>(bag2, bag1));
        }

        result = result || getInstanceBags(op).contains(new Pair<Set<OWLIndividual>, Set<OWLIndividual>>(bag2, bag1));
        return (result);
    }

    public boolean hasDataPropertyRelationship(OWLIndividual i, OWLDataPropertyExpression dpe, OWLConstant val) throws OWLReasonerException {
        OWLDataProperty dp = dpe.asOWLDataProperty();
        Set<OWLIndividual> bag = getBag(i);
        Node<OWLDataProperty> node = getNode(dp);
        return (dpInstances.get(node).contains(new Pair<Set<OWLIndividual>, OWLConstant>(bag, val)));
    }

    public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual i, OWLObjectPropertyExpression ope) throws OWLReasonerException {
        Set<OWLIndividual> bag = getBag(i);
        OWLObjectProperty op = name(ope);
        Node<OWLObjectProperty> node = getNode(op);
        boolean symmetric = false;
        if (isSymmetric(op)) {
            symmetric = true;
        }


        Set<OWLIndividual> out = Types.newSet();
        for (Pair<Set<OWLIndividual>, Set<OWLIndividual>> tuple : opInstances.get(node)) {
            if (tuple.getFirst().equals(bag)) {
                out.addAll(tuple.getSecond());
            }
            if (symmetric) {
                if (tuple.getSecond().equals(bag)) {
                    out.addAll(tuple.getFirst());
                }
            }
        }
        return out;
    }

    public Set<OWLConstant> getRelatedValues(OWLIndividual i, OWLDataPropertyExpression dpe) throws OWLReasonerException {
        Set<OWLIndividual> bag = getBag(i);
        OWLDataProperty dp = name(dpe);
        Node<OWLDataProperty> node = getNode(dp);

        Set<OWLConstant> out = Types.newSet();
        for (Pair<Set<OWLIndividual>, OWLConstant> tuple : dpInstances.get(node)) {
            if (tuple.getFirst().equals(bag)) {
                out.add(tuple.getSecond());
            }
        }
        return out;
    }

    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);

        Set<Set<OWLObjectProperty>> out = Types.newSet();
        for (Node<OWLObjectProperty> child : node.getParents()) {
            out.add(child.asSet());
        }

        return out;
    }

    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);

        Set<Set<OWLObjectProperty>> out = Types.newSet();
        for (Node<OWLObjectProperty> child : node.getChildren()) {
            out.add(child.asSet());
        }

        return out;
    }

    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);

        Set<Set<OWLObjectProperty>> out = Types.newSet();
        for (Node<OWLObjectProperty> child : node.getAscendants()) {
            out.add(child.asSet());
        }

        return out;
    }

    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);

        Set<Set<OWLObjectProperty>> out = Types.newSet();
        for (Node<OWLObjectProperty> child : node.getDescendants()) {
            out.add(child.asSet());
        }

        return out;
    }

    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);
        if (objectPropertyInverse.containsKey(node)) {
            Set<Set<OWLObjectProperty>> out = Types.newSet();
            for (Node<OWLObjectProperty> inv : objectPropertyInverse.get(node)) {
                out.add(inv.asSet());
            }
            return out;
        }

        return Collections.EMPTY_SET;
    }

    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);
        return node.asSet();
    }

    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);
        Set<Set<OWLDescription>> out = Types.newSet();
        for (Node<OWLClass> child : opDomainMap.get(op)) {
            //out.add(child.asSet());
        }
        return out;
    }

    public Set<OWLDescription> getRanges(OWLObjectProperty op) throws OWLReasonerException {
        Node<OWLObjectProperty> node = getNode(op);
        Set<OWLDescription> out = Types.newSet();
        for (Node<OWLClass> child : opRangeMap.get(op)) {
            //out.add(child.asSet());
        }
        return out;
    }

    public boolean isFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        return false;
    }

    public boolean isInverseFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        return false;
    }

    public boolean isSymmetric(OWLObjectProperty p) throws OWLReasonerException {
        return symmetricProperties.contains(getNode(p));
    }

    public boolean isTransitive(OWLObjectProperty p) throws OWLReasonerException {
        return false; // we do not support transitive properties
    }

    public boolean isReflexive(OWLObjectProperty p) throws OWLReasonerException {
        return reflexiveProperties.contains(getNode(p));
    }

    public boolean isIrreflexive(OWLObjectProperty p) throws OWLReasonerException {
        return irreflexiveProperties.contains(getNode(p));
    }

    public boolean isAntiSymmetric(OWLObjectProperty p) throws OWLReasonerException {
        return asymmetricProperties.contains(getNode(p));
    }

    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty dp) throws OWLReasonerException {
        return toSetSet(getNode(dp).getParents());
    }

    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty dp) throws OWLReasonerException {
        return toSetSet(getNode(dp).getChildren());
    }

    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty dp) throws OWLReasonerException {
        return toSetSet(getNode(dp).getAscendants());
    }

    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty dp) throws OWLReasonerException {
        return toSetSet(getNode(dp).getDescendants());
    }

    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty dp) throws OWLReasonerException {
        return getNode(dp);
    }

    public Set<Set<OWLDescription>> getDomains(OWLDataProperty dp) throws OWLReasonerException {
        return castInnerSetToDescription(toSetSet(dpDomainMap.get(getNode(dp))));
    }

    public Set<OWLDataRange> getRanges(OWLDataProperty dp) throws OWLReasonerException {
        return dpRangeMap.get(getNode(dp));
    }

    public boolean isFunctional(OWLDataProperty arg0) throws OWLReasonerException {
        return false; // functional properties not supported in QL
    }
}
