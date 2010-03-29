/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.trowl.owl;

import eu.trowl.db.DB;
import eu.trowl.db.OntologyMeta;
import eu.trowl.db.SQLBuilder;

/**
 *
 * @author ed
 */
public class Approximator {
    // Semantic Approximation functions follow
    /**
     *
     * @param db
     */
/*    public void go(DB db, Reasoner r) {
        OntologyMeta meta = new OntologyMeta();
        SQLBuilder out = new SQLBuilder(db, ont.getURI(), meta);


        getClasses(out, manager.getOWLDataFactory().getOWLThing());
        getObjectProperties(out);
        getDatatypeProperties(out);

        getInstances(out);
        getObjectPropertyInstances(out);
        getDatatypePropertyInstances(out);

        out.storePaths();
        out.rebuildIndices();
        out.close();
    }

    /**
     *
     * @throws URISyntaxException
     * @throws OWLOntologyChangeException
     */
/*    public void nameRestrictions() throws URISyntaxException, OWLOntologyChangeException {
        //OWLObjectSomeRestriction r;
        OWLDataFactory factory = manager.getOWLDataFactory();

        for (OWLObjectProperty o : ont.getReferencedObjectProperties()) {
            URI newClassURI = new URI("urn:class:" + FNV.hash32(o.getURI()));
            OWLClass newClass = factory.getOWLClass(newClassURI);
            manager.addAxiom(ont, factory.getOWLEquivalentClassesAxiom(factory.getOWLObjectSomeRestriction(o, factory.getOWLThing()), newClass));

            newClassURI = new URI("urn:class:" + FNV.hash32("neg:" + o.getURI()));
            newClass = factory.getOWLClass(newClassURI);

            manager.addAxiom(ont, factory.getOWLEquivalentClassesAxiom(factory.getOWLObjectSomeRestriction(o.getInverseProperty(), factory.getOWLThing()), newClass));
        }
    }

    /**
     *
     * @param sql
     * @param cur
     */
/*    public void getClasses(SQLBuilder sql, OWLClass cur) {

//        for (OWLClass c : pellet.getClasses()) {
        sql.createClass(cur.getURI());
        for (Set<OWLClass> subs : pellet.getSubClasses(cur)) {
            for (OWLClass sub : subs) {
                if (!sub.equals(cur) && !sub.equals(manager.getOWLDataFactory().getOWLNothing())) {
                    sql.setSubClassOf(cur.getURI(), sub.getURI());
                    getClasses(sql, sub);
                }
            }
        }
    }

    /**
     *
     * @param sql
     */
/*    public void getObjectProperties(SQLBuilder sql) {
        for (OWLObjectProperty p : ont.getReferencedObjectProperties()) {
            sql.createProperty(p.getURI());
            for (Set<OWLObjectProperty> subs : pellet.getSubProperties(p)) {
                for (OWLObjectProperty sub : subs) {
                    sql.setSubPropertyOf(p.getURI(), sub.getURI());
                }
            }
        }
    }

    /**
     *
     * @param sql
     */
/*    public void getDatatypeProperties(SQLBuilder sql) {
        for (OWLDataProperty p : ont.getReferencedDataProperties()) {
            sql.createProperty(p.getURI());
            for (Set<OWLDataProperty> subs : pellet.getSubProperties(p)) {
                for (OWLDataProperty sub : subs) {
                    sql.setSubPropertyOf(p.getURI(), sub.getURI());
                }
            }
        }
    }

    private void getInstances(SQLBuilder sql) {
        for (OWLClass c : pellet.getClasses()) {
            for (OWLIndividual i : pellet.getIndividuals(c, true)) {
                sql.createIndividual(i.getURI(), c.getURI());
            }
        }
    }

    private void getObjectPropertyInstances(SQLBuilder sql) {
        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
            Map<OWLIndividual, Set<OWLIndividual>> propertyMap = pellet.getObjectPropertyAssertions(predicate);

            for (Map.Entry<OWLIndividual, Set<OWLIndividual>> line : propertyMap.entrySet()) {
                OWLIndividual subject = line.getKey();
                for (OWLIndividual object : line.getValue()) {
                    sql.createObjectPropertyInstance(subject.getURI(), predicate.getURI(), object.getURI());
                }
            }
        }
    }

    private void getObjectPropertyInstancesNoReasoner(SQLBuilder sql) throws Exception {
        for (OWLIndividual subject : ont.getReferencedIndividuals()) {
            for (OWLObjectPropertyAssertionAxiom a : ont.getObjectPropertyAssertionAxioms(subject)) {
                OWLObjectProperty property = a.getProperty().getNamedProperty();
                OWLIndividual object = a.getObject();

                sql.createObjectPropertyInstance(subject.getURI(), property.getURI(), object.getURI());
            }
        }
    }

    private void getDatatypePropertyInstances(SQLBuilder sql) {
        for (OWLDataProperty predicate : pellet.getDataProperties()) {
            Map<OWLIndividual, Set<OWLConstant>> propertyMap = pellet.getDataPropertyAssertions(predicate);

            for (Map.Entry<OWLIndividual, Set<OWLConstant>> line : propertyMap.entrySet()) {
                OWLIndividual subject = line.getKey();
                for (OWLConstant object : line.getValue()) {
                    sql.createDatatypePropertyInstance(subject.getURI(), predicate.getURI(), object.getLiteral(), "");
                }
            }
        }
    }

    private void getCompleteness(int level, SQLBuilder out) {
        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
            System.out.println("checking :" + predicate.getURI());
            followRole(level, 0, predicate, manager.getOWLDataFactory().getOWLThing(), "T", out);
        }

        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
            System.out.println("checking :" + predicate.getURI());
            followRole(level, 0, manager.getOWLDataFactory().getOWLObjectPropertyInverse(predicate), manager.getOWLDataFactory().getOWLThing(), "T", out);
        }
    }

    private void followRole(int maxLevel, int level, OWLObjectPropertyExpression role, OWLDescription stem, String name, SQLBuilder out) {
        level = level + 1;
        if (level <= maxLevel) {
            OWLDescription newStem = manager.getOWLDataFactory().getOWLObjectSomeRestriction(role, stem);
            OWLObjectProperty namedRole = null;
            try {
                namedRole = role.asOWLObjectProperty();
            } catch (Exception e) {
                try {
                    namedRole = ((OWLObjectPropertyInverse) role).getInverse().asOWLObjectProperty();
                } catch (Exception ee) {
                    // this just means there is no inverse possible
                }
            }

            Set<OWLIndividual> is;
            try {
                if (pellet.isSatisfiable(newStem)) {
                    is = pellet.getIndividuals(newStem, false);
                    String fname = namedRole.getURI().getFragment() + "." + name;
                    System.out.println(fname);
                    if (!is.isEmpty()) {
                        for (OWLIndividual i : is) {
//                            out.createIndividual(i.getURI(), fname);
                        }

                        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
                            followRole(maxLevel, level, predicate, newStem, fname, out);
                        }
                    }
                }

                newStem = manager.getOWLDataFactory().getOWLObjectSomeRestriction(manager.getOWLDataFactory().getOWLObjectPropertyInverse(role), stem);

                if (pellet.isSatisfiable(newStem)) {
                    is = pellet.getIndividuals(newStem, false);
                    String fname = namedRole.getURI().getFragment() + "-." + name;
                    System.out.println(fname);
                    if (!is.isEmpty()) {
                        for (OWLIndividual i : is) {
                            System.out.println("  : " + i.getURI());
                        }

                        for (OWLObjectProperty predicate : pellet.getObjectProperties()) {
                            followRole(maxLevel, level, predicate, newStem, fname, out);
                        }
                    }
                }
            } catch (Exception e) {
                // this happens on the Wine ontology for reasons I cannot explain
                // I suspect a bug in pellet
                System.out.println("Pellet choked, continuing...");
            }
        }
    }
*/
}
