/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.owl;

import com.clarkparsia.owlapi.OWL;
import eu.trowl.db.DB;
import eu.trowl.hashing.FNV;
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
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectComplementOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
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
        thing = getNode(manager.getOWLDataFactory().getOWLClass(OWLRDF.THING));
        nothing = getNode(manager.getOWLDataFactory().getOWLClass(OWLRDF.NOTHING));

        for (OWLOntology ont : manager.getOntologies()) {
            for (OWLSubClassAxiom ax : ont.getAxioms(AxiomType.SUBCLASS)) {
                OWLDescription lhs = ax.getSubClass();
                OWLDescription rhs = ax.getSuperClass();

                Set<OWLClass> subclasses;
                OWLClass superclass;

                try {
                    if (!lhs.isAnonymous()) {
                        superclass = lhs.asOWLClass();
                    } else if (lhs.getClass().isAssignableFrom(OWLObjectSomeRestriction.class)) {
                        superclass = name(lhs);
                    } else {
                        continue;
                    }

                    Node<OWLClass> superNode = getNode(superclass);

                    if (isQL(rhs)) {
                        // rhs has to be intersection of some classes, negation of some class, or equivalent object property
                        if (rhs.getClass().isAssignableFrom(OWLObjectIntersectionOf.class)) {
                            for (OWLClass c : flattenIntersection((OWLObjectIntersectionOf) rhs)) {
                                Node<OWLClass> subNode = getNode(c);
                                superNode.addChild(subNode);
                            }
                        } else if (rhs.getClass().isAssignableFrom(OWLObjectSomeRestriction.class)) {
                            Node<OWLClass> subNode = getNode(name(rhs));
                            superNode.addChild(subNode);
                        } else if (rhs.getClass().isAssignableFrom(OWLObjectComplementOf.class)) {
                            OWLObjectComplementOf comp = (OWLObjectComplementOf) rhs;
                            OWLDescription of = comp.getOperand();

                            if (of.isAnonymous()) {
                                if (of.getClass().isAssignableFrom(OWLObjectSomeRestriction.class)) {
                                    addDisjunction(superclass, name((OWLObjectSomeRestriction) of));
                                } else {
                                    //System.out.println("Dropped a disjoint axiom");
                                }
                            } else {
                                addDisjunction(superclass, of.asOWLClass());
                            }
                        } else if (!rhs.isAnonymous()) {
                            Node<OWLClass> subNode = getNode(rhs.asOWLClass());
                            superNode.addChild(subNode);
                        }
                    }
                } catch (NotExpressibleException ex) {
                    log.info(ex.getMessage());
                }
            }
        }

        for (Node<OWLClass> node : classNodes) {
            if (node.getParents().isEmpty()) {
                thing.addChild(node);
            }
        }
    }

    private void loadRBox() {
        topObjectProperty = getNode(manager.getOWLDataFactory().getOWLObjectProperty(OWLRDF.TOP_OBJECT_PROPERTY));
        topDataProperty = getNode(manager.getOWLDataFactory().getOWLDataProperty(OWLRDF.TOP_DATA_PROPERTY));

        for (OWLOntology ont : manager.getOntologies()) {
            for (OWLSubPropertyAxiom ax : ont.getAxioms(AxiomType.SUB_OBJECT_PROPERTY)) {
                OWLObjectProperty superprop;
                OWLObjectProperty subprop;
                if (!ax.getSubProperty().isAnonymous() &&
                        !ax.getSuperProperty().isAnonymous()) {
                    subprop = (OWLObjectProperty) ax.getSubProperty();
                    superprop = (OWLObjectProperty) ax.getSuperProperty();
                    getNode(superprop).addChild(getNode(subprop));
                }
            }

            for (OWLSubPropertyAxiom ax : ont.getAxioms(AxiomType.SUB_DATA_PROPERTY)) {
                OWLDataProperty superprop;
                OWLDataProperty subprop;
                if (!ax.getSubProperty().isAnonymous() &&
                        !ax.getSuperProperty().isAnonymous()) {
                    subprop = (OWLDataProperty) ax.getSubProperty();
                    superprop = (OWLDataProperty) ax.getSuperProperty();
                    getNode(superprop).addChild(getNode(subprop));
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
        for (OWLOntology ont : manager.getOntologies()) {
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
        for (OWLOntology ont : manager.getOntologies()) {
            for (OWLEquivalentClassesAxiom ax : ont.getAxioms(AxiomType.EQUIVALENT_CLASSES)) {
                ont.getClassAxioms();

            }

        }
    }

    private void normaliseEquivalentClasses() throws OWLOntologyChangeException {
        for (OWLOntology ont : manager.getOntologies()) {
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
        for (OWLOntology ont : manager.getOntologies()) {
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
                    flattenIntersection((OWLObjectIntersectionOf) c, bag);
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
    public OWLClass name(OWLDescription d) throws NotExpressibleException {
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

    public void store() {
    }

    public void store(String repository) {
    }

    public void store(DB repository) {
    }

    public boolean consistent() {
        if (consistent == null) {
            consistent = new Boolean(consistencyCheck());
        }

        return consistent.booleanValue();
    }

    private boolean consistencyCheck() {
        //boolean result = true;

        for (Node c1 : classDisjunctions.keySet()) {
            for (Node c2 : classDisjunctions.get(c1)) {
                if (c1 != c2) {
                    if (!Collections.disjoint(getInstances(c1), getInstances(c2))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean overlap(Collection a, Collection b) {
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
    }

    public boolean allSatisfiable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLClass> getUnsatisfiable() {
        try {
            return flattenSetOfSets(getSubClasses(factory.getOWLNothing()));
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        try {
            this.manager = input;
            this.factory = manager.getOWLDataFactory();
            this.onts = manager.getOntologies();
            for (OWLOntology o : onts) {
                this.onts.addAll(manager.getImportsClosure(o));
            }
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

    public OWLOntology getOntology() {
        return onts.iterator().next();
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

    public boolean isConsistent(OWLOntology arg0) throws OWLReasonerException {
        //why oh why??
        return false;
    }

    public void loadOntologies(Set<OWLOntology> arg0) throws OWLReasonerException {
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLClass>> getTypes(OWLIndividual arg0, boolean arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLIndividual> getIndividuals(OWLDescription arg0, boolean arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(OWLIndividual arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasType(OWLIndividual arg0, OWLDescription arg1, boolean arg2) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasObjectPropertyRelationship(OWLIndividual i1, OWLObjectPropertyExpression ope, OWLIndividual i2) throws OWLReasonerException {
        OWLObjectProperty op = ope.asOWLObjectProperty();
        return (opInstances.get(op).contains(new Pair<OWLIndividual, OWLIndividual>(i1, i2)));
    }

    public boolean hasDataPropertyRelationship(OWLIndividual i, OWLDataPropertyExpression dpe, OWLConstant val) throws OWLReasonerException {
        OWLDataProperty dp = dpe.asOWLDataProperty();
        return (dpInstances.get(dp).contains(new Pair<OWLIndividual, OWLConstant>(i, val)));
    }

    public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual arg0, OWLObjectPropertyExpression arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLConstant> getRelatedValues(OWLIndividual arg0, OWLDataPropertyExpression arg1) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getAncestorProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getDescendantProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLObjectProperty>> getInverseProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDescription>> getDomains(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDescription> getRanges(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        return false;
    }

    public boolean isInverseFunctional(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTransitive(OWLObjectProperty arg0) throws OWLReasonerException {
        return false; // we do not support transitive properties
    }

    public boolean isReflexive(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isIrreflexive(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isAntiSymmetric(OWLObjectProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDataProperty>> getDescendantProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<Set<OWLDescription>> getDomains(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<OWLDataRange> getRanges(OWLDataProperty arg0) throws OWLReasonerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isFunctional(OWLDataProperty arg0) throws OWLReasonerException {
        return false; // functional properties not supported in QL
    }
}
