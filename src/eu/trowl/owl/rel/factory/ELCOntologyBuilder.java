package eu.trowl.owl.rel.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLObjectAllRestriction;
import org.semanticweb.owl.model.OWLObjectComplementOf;
import org.semanticweb.owl.model.OWLObjectExactCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectMaxCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectMinCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectOneOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLObjectPropertyInverse;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectUnionOf;
import org.semanticweb.owl.model.OWLObjectValueRestriction;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;

import eu.trowl.owl.rel.model.And;
import eu.trowl.owl.rel.model.Atomic;
import eu.trowl.owl.rel.model.Basic;
import eu.trowl.owl.rel.model.Description;
import eu.trowl.owl.rel.model.ELCQOntology;
import eu.trowl.owl.rel.model.Implies;
import eu.trowl.owl.rel.model.Individual;
import eu.trowl.owl.rel.model.Role;
import eu.trowl.owl.rel.model.Singleton;
import eu.trowl.owl.rel.model.Some;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
abstract class ELCOntologyBuilder extends OWL2DLAxiomVisitor {

	protected static final String CLASS_PREFIX = "RELAPPROX#RELAPPROXC";
	protected static final String ROLE_PREFIX = "RELAPPROX#RELAPPROXR";
	protected static final String CARDIN_PREFIX = "RELAPPROXQ";

	protected ELCQOntology elcontology;

	protected HashMap<OWLDescription, Integer> classID;
	protected HashMap<OWLObjectPropertyExpression, Integer> propertyID;
	protected HashMap<OWLIndividual, Integer> individualID;

	protected HashMap<Integer, Description> descriptions;
	protected HashMap<Integer, Role> roles;
	protected HashMap<Integer, Individual> individuals;

	protected HashSet<Basic> allconcepts;

	protected HashMap<ArrayList<Role>, Role> chainName = new HashMap<ArrayList<Role>, Role>();
	private final Map<Description, Atomic> normalisationNames = new HashMap<Description, Atomic>();

	public HashMap<Atomic, HashMap<Integer, Atomic>> CardinalityTable = new HashMap<Atomic, HashMap<Integer, Atomic>>();


	protected int classindex = 0;
	protected int nonbasicindex = -2;
	protected int propertyindex = 0;
	protected int individualindex = 0;
	protected int impliesID;

	Atomic top = null;
	Atomic bot = null;

	protected OWLDataFactory factory = null;

	public ELCOntologyBuilder(OWLOntologyManager manager, ELCQOntology ELContology) {
		// TODO Auto-generated constructor stub
		super(true);
		factory = manager.getOWLDataFactory();
		elcontology = ELContology;

		classID = elcontology.classID;
		propertyID = elcontology.roleID;
		individualID = elcontology.individualID;
		descriptions = elcontology.descriptions;
		roles = elcontology.roles;
		individuals = elcontology.individuals;
		allconcepts = elcontology.allconcepts;

	}

	public ELCQOntology getELOntology() {
		return elcontology;
	}

	public void visit(OWLSubClassAxiom axiom) {
		OWLDescription sub = axiom.getSubClass();
		OWLDescription sup = axiom.getSuperClass();
		approximate(getNNF(sub), getNNF(sup));
	}

	protected void approximate(OWLDescription sub, OWLDescription sup)
	{
		Description lhs = getDescription(sub);
		Description rhs = getDescription(sup);
		normalise(lhs, rhs);
	}

	protected Description getDescription(OWLDescription concept) {
		// TODO Auto-generated method stub
		Description NC = null;

		if(classID.get(concept) != null)
			NC = descriptions.get(classID.get(concept));
		else if(concept instanceof OWLObjectComplementOf)	// in NNF only Basic has Complement
		{
			OWLObjectComplementOf comp = (OWLObjectComplementOf) concept;
			OWLDescription des = comp.getOperand();
			Basic nNCA = (Basic) getDescription(des);
			NC = nNCA.complement;
			if(NC == null)
			{
				Atomic NCA = new Atomic();
				NCA.id = classindex;
				NCA.original = false;
				NCA.uri = URI.create(CLASS_PREFIX+(classindex++));
				classID.put(concept, NCA.id);
				descriptions.put(NCA.id, NCA);
				imply(NCA);
				NCA.complement = nNCA;
				nNCA.complement = NCA;
				NC = NCA;
			}
		}
		else if(concept instanceof OWLObjectIntersectionOf)
		{
			OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) concept;
			HashSet<Description> interapprox = new HashSet<Description>();
			for(OWLDescription des : intersection.getOperands())
			{
				Description desapprox = getDescription(des);
				interapprox.add(desapprox);
			}
			NC = new And(interapprox);
			NC.id = nonbasicindex--;
			classID.put(concept, NC.id);
			descriptions.put(NC.id, NC);
		}
		else if(concept instanceof OWLObjectUnionOf)
		{
			OWLObjectUnionOf union = (OWLObjectUnionOf) concept;
			OWLDescription complement = getNNF(factory.getOWLObjectComplementOf(union));
			Atomic NCA = new Atomic();
			NCA.id = classindex;
			NCA.original = false;
			NCA.uri = URI.create(CLASS_PREFIX+(classindex++));
			classID.put(concept, NCA.id);
			descriptions.put(NCA.id, NCA);
			imply(NCA);

			Atomic nNCA = new Atomic();
			nNCA.id = classindex;
			nNCA.original = false;
			nNCA.uri = URI.create(CLASS_PREFIX+(classindex++));

			Description complementapprox = getDescription(complement);
			classID.put(complement, nNCA.id);
			descriptions.put(nNCA.id, nNCA);
			imply(nNCA);

			NCA.complement = nNCA;
			nNCA.complement = NCA;

			normalise(nNCA, complementapprox);
			normalise(complementapprox, nNCA);
			NC = NCA;
		}
		else if(concept instanceof OWLObjectSomeRestriction)
		{
			OWLObjectSomeRestriction some = (OWLObjectSomeRestriction) concept;
			OWLObjectPropertyExpression property = some.getProperty();
			Role role = getRole(property);
			Some someA = new Some(role, getDescription(some.getFiller()));
			NC = someA;
			NC.id = nonbasicindex--;
			classID.put(concept, NC.id);
			descriptions.put(NC.id, NC);
		}
		else if(concept instanceof OWLObjectAllRestriction)
		{
			OWLObjectAllRestriction all = (OWLObjectAllRestriction) concept;
			OWLDescription complement = getNNF(factory.getOWLObjectComplementOf(all));

			Atomic NCA = new Atomic();
			NCA.id = classindex;
			NCA.original = false;
			NCA.uri = URI.create(CLASS_PREFIX+(classindex++));
			classID.put(concept, NCA.id);
			descriptions.put(NCA.id, NCA);
			imply(NCA);

			Atomic nNCA = new Atomic();
			nNCA.id = classindex;
			nNCA.original = false;
			nNCA.uri = URI.create(CLASS_PREFIX+(classindex++));

			Some complementapprox = (Some) getDescription(complement);
			classID.put(complement, nNCA.id);
			descriptions.put(nNCA.id, nNCA);
			imply(nNCA);
			if(complementapprox.concept instanceof Basic)
				complementapprox.role.somes.put((Basic) complementapprox.concept,complementapprox);

			NCA.complement = nNCA;
			nNCA.complement = NCA;

			normalise(nNCA, complementapprox);
			normalise(complementapprox, nNCA);

			NC = NCA;

		}

		else if(concept instanceof OWLObjectMinCardinalityRestriction)
		{
			OWLObjectMinCardinalityRestriction min = (OWLObjectMinCardinalityRestriction) concept;
			int n = min.getCardinality();
			OWLObjectPropertyExpression property = min.getProperty();
			Role role = getRole(property);
			OWLDescription filler = min.getFiller();
			Description fillerapprox = getDescription(filler);
			if(!(fillerapprox instanceof Atomic))
			{
				OWLDescription nfiller = getNNF(factory.getOWLObjectComplementOf(filler));
				Atomic nfillerapporx = (Atomic) getDescription(nfiller);
				fillerapprox = nfillerapporx.complement;
			}
			Atomic fillerA = (Atomic) fillerapprox;
			if(CardinalityTable.get(fillerA) == null)
			{
				HashMap<Integer, Atomic> fillerapproxentry = new HashMap<Integer, Atomic>();
				fillerapproxentry.put(1, fillerA);
				CardinalityTable.put(fillerA, fillerapproxentry);
			}
			Atomic newfiller = CardinalityTable.get(fillerA).get(n);
			if(newfiller == null)
			{
				String newuri = fillerA.uri.toString()+CARDIN_PREFIX+n;
				newfiller = new Atomic();
				newfiller.id = classindex++;
				newfiller.uri = URI.create(newuri);
				newfiller.original = false;
				CardinalityTable.get(fillerA).put(n, newfiller);
				descriptions.put(newfiller.id, newfiller);
				imply(newfiller);		
			}
			NC = new Some(role,newfiller);
			NC.id = nonbasicindex--;
			classID.put(concept, NC.id);
			descriptions.put(NC.id, NC);
		}
		else if(concept instanceof OWLObjectMaxCardinalityRestriction)
		{
			OWLObjectMaxCardinalityRestriction max = (OWLObjectMaxCardinalityRestriction) concept;
			OWLDescription complement = getNNF(factory.getOWLObjectComplementOf(max));
			Atomic NCA = new Atomic();
			NCA.id = classindex;
			NCA.original = false;
			NCA.uri = URI.create(CLASS_PREFIX+(classindex++));
			classID.put(concept, NCA.id);
			descriptions.put(NCA.id, NCA);
			imply(NCA);

			Atomic nNCA = new Atomic();
			nNCA.id = classindex;
			nNCA.original = false;
			nNCA.uri = URI.create(CLASS_PREFIX+(classindex++));

			Some complementapprox = (Some) getDescription(complement);
			classID.put(complement, nNCA.id);
			descriptions.put(nNCA.id, nNCA);
			imply(nNCA);
			if(complementapprox.concept instanceof Basic)
				complementapprox.role.somes.put((Basic) complementapprox.concept,complementapprox);

			NCA.complement = nNCA;
			nNCA.complement = NCA;

			normalise(nNCA,complementapprox);
			normalise(complementapprox,nNCA);

			NC = NCA;
		}
		else if(concept instanceof OWLObjectOneOf)
		{
			OWLObjectOneOf oneof = (OWLObjectOneOf)concept;
			Set<OWLIndividual> indis= oneof.getIndividuals();
			Set<OWLObjectOneOf> concepts = new HashSet<OWLObjectOneOf>();
			for(OWLIndividual individual:indis)
			{
				OWLObjectOneOf singleton = factory.getOWLObjectOneOf(individual);
				concepts.add(singleton);
			}
			if(indis.size() == 1)
			{
				OWLObjectOneOf singleton = concepts.iterator().next();
				NC = getSingleton(singleton.getIndividuals().iterator().next());
			}
			else
			{
				OWLObjectUnionOf union = factory.getOWLObjectUnionOf(concepts);
				NC = getDescription(union);
			}
		}
		else
		{
			Atomic NCA = new Atomic();
			NCA.uri = URI.create(CLASS_PREFIX+classindex);
			NCA.id = classindex++;
			NCA.original = false;
			classID.put(concept, NCA.id);
			descriptions.put(NCA.id, NCA);
			imply(NCA);
			NC = NCA;
//			System.out.println("Can not approximate "+concept);
		}
		return NC;
	}

	public Role getRole(OWLObjectPropertyExpression property){
		Role RN = null;
		if(propertyID.get(property)!=null)
			RN = roles.get(propertyID.get(property));
		else if(property instanceof OWLObjectProperty)
		{
			OWLObjectProperty atomicrole = (OWLObjectProperty) property;
			RN = new Role(atomicrole);
			RN.id = propertyindex++;
			propertyID.put(property, RN.id);
			roles.put(RN.id, RN);
		} else if(property instanceof OWLObjectPropertyInverse) {
			OWLObjectPropertyInverse inverse = (OWLObjectPropertyInverse) property;
			Role iRN = getRole(inverse.getInverse());
			RN = iRN.inverse;
			if(RN == null)
			{
				RN = new Role();
				RN.uri = URI.create(ROLE_PREFIX+propertyindex);
				RN.id = propertyindex++;
				RN.original = false;
				propertyID.put(property, RN.id);
				roles.put(RN.id, RN);

				RN.inverse = iRN;
				iRN.inverse = RN;
			}
		}
		return RN;
	}


	public Individual getIndividual(OWLIndividual individual){
		Individual IN = null;
		if(individualID.get(individual)!=null)
			IN = individuals.get(individualID.get(individual));
		else
		{
			IN = new Individual(individual);
			IN.id = individualindex++;
			individualID.put(individual, IN.id);
			individuals.put(IN.id, IN);
			// create singleton for the individual
//			IN.singleton = getSingleton(individual);
		}
		return IN;
	}

	public Singleton getSingleton(OWLIndividual individual){
		Individual indi = getIndividual(individual);
		Singleton single = indi.singleton;
		if(single == null)
		{
			single = new Singleton(indi);
			single.id = classindex++;
			single.original = false;
			descriptions.put(single.id, single);
			imply(single);
		}
		return single;
	}

	private void normaliseRoleChain(ArrayList<Role> lhs, Role rhs){

		if(lhs.size() > 2)
		{
			// Remove last item from chain.
			Role roleR2 = lhs.remove(lhs.size() - 1);

			// Add named property for chain.
			Role roleR1 = getChainName(lhs);

			HashSet<Role> superroles = roleR1.RightComposition.get(roleR2);
			if(superroles == null)
			{
				superroles = new HashSet<Role>();
				roleR1.RightComposition.put(roleR2, superroles);
			}
			superroles.add(rhs);
			normaliseRoleChain(lhs, roleR1);
		}
		else{
			Role roleR1 = lhs.get(0);
			Role roleR2 = lhs.get(1);
			HashSet<Role> superroles = roleR1.RightComposition.get(roleR2);
			if(superroles == null)
			{
				superroles = new HashSet<Role>();
				roleR1.RightComposition.put(roleR2, superroles);
			}
			superroles.add(rhs);
		}
	}

	private Role getChainName(ArrayList<Role> chain)
	{
		Role name = chainName.get(chain);
		if(name == null)
		{
			name = new Role();
			name.id = propertyindex;
			name.uri = URI.create(ROLE_PREFIX+propertyindex);
			name.original = false;
			chainName.put(chain, name);
			roles.put(name.id, name);
			propertyindex++;
		}
		return name;

	}

	abstract public void OrderingCardinality();

	@Override
	public void visit(OWLObjectSubPropertyAxiom axiom) {
		getRole(axiom.getSubProperty()).subsumers.add(getRole(axiom.getSuperProperty()));
	}

	@Override
	public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {

		ArrayList<Role> lhs = new ArrayList<Role>();
		for(OWLObjectPropertyExpression role:axiom.getPropertyChain())
			lhs.add(getRole(role));

		Role rhs = getRole(axiom.getSuperProperty());
		
		if(lhs.size() == 1)
			visit(factory.getOWLSubObjectPropertyAxiom(axiom.getPropertyChain().get(0), axiom.getSuperProperty()));
		else{
			normaliseRoleChain(lhs, rhs);
			if(lhs.size() == 2)
			{
				boolean transitive = true;
				for(Role l:lhs)
					if(!l.equals(rhs))
					{
						transitive = false;
						break;
					}
				rhs.transitive = rhs.transitive || transitive;
			}
		}
	}

	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		OWLIndividual sub = axiom.getSubject();
		OWLIndividual obj = axiom.getObject();		
		OWLObjectPropertyExpression property = axiom.getProperty();
		Role role = getRole(property);

		// treat ABox as TBox
		Singleton sig1 = getSingleton(sub);
		Singleton sig2 = getSingleton(obj);
		initialise(sig1, role, sig2);
		Role invRole = getRole(factory.getOWLObjectPropertyInverse(axiom.getProperty()));
		initialise(sig2, invRole, sig1);
	}

	@Override
	public void visit(OWLClassAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		Description classification = getDescription(getNNF(axiom.getDescription()));
		normalise(getSingleton(axiom.getIndividual()),classification);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyDomainAxiom axiom) {
		// TODO Auto-generated method stub
		OWLDescription exist = factory.getOWLObjectSomeRestriction(axiom.getProperty(), factory.getOWLThing());
		Description lhs = getDescription(getNNF(exist));
		Description rhs = getDescription(getNNF(axiom.getDomain()));
		normalise(lhs, rhs);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		// TODO Auto-generated method stub
		OWLDescription range = axiom.getRange();
		OWLObjectPropertyExpression property = axiom.getProperty();
		OWLDescription some = factory.getOWLObjectSomeRestriction(property, factory.getOWLObjectComplementOf(range));
		normalise(getDescription(getNNF(some)), bot);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLEquivalentClassesAxiom)
	 */

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		// TODO Auto-generated method stub
		for (OWLDescription c : axiom.getDescriptions()) {
			for (OWLDescription d : axiom.getDescriptions()) {
				if(c.equals(d))
					continue;
				Description cdes = getDescription(getNNF(c));
				Description ddes = getDescription(getNNF(d));
				normalise(cdes, ddes);
			}
		}   	

	}

	// equivalent property 
	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		// RW2: property equivalence.
		for (OWLObjectPropertyExpression r : axiom.getProperties()) {
			for (OWLObjectPropertyExpression s : axiom.getProperties()) {
				if(r.equals(s))
					continue;
				Role roler = getRole(r);
				Role roles = getRole(s);
				roler.subsumers.add(roles);
			}
		}
	}

	// transitive property
	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		// RW3: transative using role composition.
		OWLObjectPropertyChainSubPropertyAxiom rewritten = factory.getOWLObjectPropertyChainSubPropertyAxiom(Arrays.asList(axiom.getProperty(), axiom.getProperty()), axiom.getProperty());
		visit(rewritten);
		Role role = getRole(axiom.getProperty());
		role.transitive = true;
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDifferentIndividualsAxiom)
	 */
	@Override
	public void visit(OWLDifferentIndividualsAxiom axiom) {
		// TODO Auto-generated method stub
		Set<OWLIndividual> individuals = axiom.getIndividuals();
		Set<OWLObjectOneOf> singltons = new HashSet<OWLObjectOneOf>();
		for(OWLIndividual individual:individuals)
		{
			OWLObjectOneOf singlton = factory.getOWLObjectOneOf(individual);
			singltons.add(singlton);
		}
		visit(factory.getOWLDisjointClassesAxiom(singltons));
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLSameIndividualsAxiom)
	 */
	@Override
	public void visit(OWLSameIndividualsAxiom axiom) {
		// TODO Auto-generated method stub
		Set<OWLIndividual> individuals = axiom.getIndividuals();
		Set<OWLObjectOneOf> singltons = new HashSet<OWLObjectOneOf>();
		for(OWLIndividual individual:individuals)
		{
			OWLObjectOneOf singlton = factory.getOWLObjectOneOf(individual);
			singltons.add(singlton);
		}
		visit(factory.getOWLEquivalentClassesAxiom(singltons));
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		OWLObjectPropertyExpression property = axiom.getProperty();
		Description lhs = getDescription(factory.getOWLObjectMinCardinalityRestriction(property, 2, factory.getOWLThing()));
		getRole(property).functional = true;
		normalise(lhs,bot);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		OWLObjectPropertyExpression property = axiom.getProperty();
		Description lhs = getDescription(factory.getOWLObjectMinCardinalityRestriction(factory.getOWLObjectPropertyInverse(property), 2, factory.getOWLThing()));
		getRole(factory.getOWLObjectPropertyInverse(property)).functional = true;		
		normalise(lhs,bot);
	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		// TODO Auto-generated method stub
		Set<OWLDescription> dess = new HashSet<OWLDescription>();
		for(OWLDescription des1:axiom.getDescriptions())
		{
			for(OWLDescription desc:dess)
			{
				OWLDescription des2 = getNNF(factory.getOWLObjectComplementOf(desc));
				Description lhs = getDescription(getNNF(des1));
				Description rhs = getDescription(des2);
				normalise(lhs,rhs);
			}
			dess.add(des1);
		}

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		Role role1 = getRole(axiom.getFirstProperty());
		Role role2 = getRole(axiom.getSecondProperty());
		role1.inverse = role2;
		role2.inverse = role1;
	}

	protected OWLDescription getNNF(OWLDescription concept)
	{
		OWLDescription nnf = null;
		if(concept.isOWLThing() || concept.isOWLNothing() || concept instanceof OWLClass || concept instanceof OWLObjectOneOf)
			nnf = concept;
		else if(concept instanceof OWLObjectComplementOf)
		{
			OWLObjectComplementOf comp = (OWLObjectComplementOf) concept;
			OWLDescription des = comp.getOperand();
			if(des.isOWLThing())
				nnf = factory.getOWLNothing();
			else if(des.isOWLNothing())
				nnf = factory.getOWLThing();
			else if(des instanceof OWLClass)
				nnf = concept;
			else if(des instanceof OWLObjectComplementOf)
			{
				OWLObjectComplementOf doublecomp = (OWLObjectComplementOf) des;
				nnf = doublecomp.getOperand();
			}
			else if(des instanceof OWLObjectIntersectionOf)
			{
				OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) des;
				Set<OWLDescription> interNNF = new HashSet<OWLDescription>();
				for(OWLDescription inter : intersection.getOperands())
					interNNF.add(getNNF(factory.getOWLObjectComplementOf(inter)));
				nnf = factory.getOWLObjectUnionOf(interNNF);
			}
			else if(des instanceof OWLObjectUnionOf)
			{
				OWLObjectUnionOf union = (OWLObjectUnionOf) des;
				Set<OWLDescription> uniNNF = new HashSet<OWLDescription>();
				for(OWLDescription uni:union.getOperands())
					uniNNF.add(getNNF(factory.getOWLObjectComplementOf(uni)));
				nnf = factory.getOWLObjectIntersectionOf(uniNNF);
			}
			else if(des instanceof OWLObjectSomeRestriction)
			{
				OWLObjectSomeRestriction some = (OWLObjectSomeRestriction) des;
				OWLObjectPropertyExpression property = some.getProperty();
				OWLDescription filler = some.getFiller();
				nnf = factory.getOWLObjectAllRestriction(property, getNNF(factory.getOWLObjectComplementOf(filler)));
			}
			else if(des instanceof OWLObjectAllRestriction)
			{
				OWLObjectAllRestriction all = (OWLObjectAllRestriction) des;
				OWLObjectPropertyExpression property = all.getProperty();
				OWLDescription filler = all.getFiller();
				nnf = factory.getOWLObjectSomeRestriction(property, getNNF(factory.getOWLObjectComplementOf(filler)));
			}
			else if(des instanceof OWLObjectMinCardinalityRestriction)
			{
				OWLObjectMinCardinalityRestriction min = (OWLObjectMinCardinalityRestriction) des;
				OWLObjectPropertyExpression property = min.getProperty();
				OWLDescription filler = min.getFiller();
				int card = min.getCardinality();
				if(card >= 1)
					nnf = factory.getOWLObjectMaxCardinalityRestriction(property, card-1, getNNF(filler));
				else
					nnf = factory.getOWLNothing();
			}
			else if(des instanceof OWLObjectMaxCardinalityRestriction)
			{
				OWLObjectMaxCardinalityRestriction max = (OWLObjectMaxCardinalityRestriction) des;
				OWLObjectPropertyExpression property = max.getProperty();
				OWLDescription filler = max.getFiller();
				int card = max.getCardinality();
				nnf = factory.getOWLObjectMinCardinalityRestriction(property, card+1, getNNF(filler));
			}
			else if(des instanceof OWLObjectExactCardinalityRestriction)
			{
				OWLObjectExactCardinalityRestriction exact = (OWLObjectExactCardinalityRestriction) des;
				if(exact.getCardinality() >= 1)
				{
					OWLDescription low = factory.getOWLObjectMaxCardinalityRestriction(exact.getProperty(), exact.getCardinality()-1, getNNF(exact.getFiller()));
					OWLDescription high = factory.getOWLObjectMinCardinalityRestriction(exact.getProperty(), exact.getCardinality()+1, getNNF(exact.getFiller()));
					nnf = factory.getOWLObjectUnionOf(low,high);
				}
				else
					nnf = factory.getOWLObjectSomeRestriction(exact.getProperty(), getNNF(exact.getFiller()));
			}
			else if(des instanceof OWLObjectOneOf)
				nnf = concept;
			else if(des instanceof OWLObjectValueRestriction)
			{
				OWLObjectValueRestriction value = (OWLObjectValueRestriction) des;
				nnf = factory.getOWLObjectAllRestriction(value.getProperty(), factory.getOWLObjectComplementOf(factory.getOWLObjectOneOf(value.getValue())));
			}
			else nnf = concept;

		}
		else if(concept instanceof OWLObjectIntersectionOf)
		{
			OWLObjectIntersectionOf inter = (OWLObjectIntersectionOf) concept;
			Set<OWLDescription> interNNF = new HashSet<OWLDescription>();
			for(OWLDescription des:inter.getOperands())
				interNNF.add(getNNF(des));
			nnf = factory.getOWLObjectIntersectionOf(interNNF);
		}
		else if(concept instanceof OWLObjectUnionOf)
		{
			OWLObjectUnionOf union = (OWLObjectUnionOf) concept;
			Set<OWLDescription> unionNNF = new HashSet<OWLDescription>();
			for(OWLDescription des:union.getOperands())
				unionNNF.add(getNNF(des));
			nnf = factory.getOWLObjectUnionOf(unionNNF);
		}
		else if(concept instanceof OWLObjectSomeRestriction)
		{
			OWLObjectSomeRestriction some = (OWLObjectSomeRestriction) concept;
			OWLObjectPropertyExpression property = some.getProperty();
			nnf = factory.getOWLObjectSomeRestriction(property, getNNF(some.getFiller()));
		}
		else if(concept instanceof OWLObjectAllRestriction)
		{
			OWLObjectAllRestriction all = (OWLObjectAllRestriction) concept;
			nnf = factory.getOWLObjectAllRestriction(all.getProperty(), getNNF(all.getFiller()));
		}
		else if(concept instanceof OWLObjectMinCardinalityRestriction)
		{
			OWLObjectMinCardinalityRestriction min = (OWLObjectMinCardinalityRestriction) concept;
			OWLObjectPropertyExpression property = min.getProperty();
			OWLDescription filler = min.getFiller();
			int card = min.getCardinality();
			if(card == 0)
				nnf = factory.getOWLThing();
			if(card == 1)
				nnf = factory.getOWLObjectSomeRestriction(property, getNNF(filler));
			else
				nnf = factory.getOWLObjectMinCardinalityRestriction(property, card, getNNF(filler));
		}
		else if(concept instanceof OWLObjectMaxCardinalityRestriction)
		{
			OWLObjectMaxCardinalityRestriction max = (OWLObjectMaxCardinalityRestriction) concept;
			OWLObjectPropertyExpression property = max.getProperty();
			OWLDescription filler = max.getFiller();
			int card = max.getCardinality();
			if(card < 1)
				nnf = factory.getOWLObjectAllRestriction(property, getNNF(factory.getOWLObjectComplementOf(filler)));
			else
				nnf = factory.getOWLObjectMaxCardinalityRestriction(property, card, getNNF(filler));
		}
		else if(concept instanceof OWLObjectExactCardinalityRestriction)
		{
			OWLObjectExactCardinalityRestriction exact = (OWLObjectExactCardinalityRestriction) concept;
			if(exact.getCardinality() >= 1)
			{
				OWLDescription low = factory.getOWLObjectMaxCardinalityRestriction(exact.getProperty(), exact.getCardinality(), getNNF(exact.getFiller()));
				OWLDescription high = factory.getOWLObjectMinCardinalityRestriction(exact.getProperty(), exact.getCardinality(), getNNF(exact.getFiller()));
				nnf = factory.getOWLObjectIntersectionOf(getNNF(low),getNNF(high));
			}
			else
				nnf = factory.getOWLObjectAllRestriction(exact.getProperty(), getNNF(factory.getOWLObjectComplementOf(exact.getFiller())));
		}
		else if(concept instanceof OWLObjectValueRestriction)
		{
			OWLObjectValueRestriction value = (OWLObjectValueRestriction) concept;
			OWLObjectPropertyExpression property = value.getProperty();
			OWLIndividual indi = value.getValue();
			nnf = factory.getOWLObjectSomeRestriction(property, factory.getOWLObjectOneOf(indi));
		}
		else
		{
//			System.out.println("Can't get NNF for "+concept);
			nnf = concept;
		}
		return nnf;
	}

	public void normalise(Description lhs, Description rhs)
	{
		if(lhs instanceof And)
		{
			And and = (And) lhs;
			HashSet<Description> descriptions = and.operands;
			HashSet<Basic> normalised = new HashSet<Basic>();
			for(Description CHat:descriptions)
			{
				if(!(CHat instanceof Basic))
				{
					Atomic A = getNamedClass(CHat);
					normalised.add(A);
					normalise(CHat, A);
				}
				else
					normalised.add((Basic)CHat);
			}
			Basic B = null;
			if(rhs instanceof Basic)
				B = (Basic)rhs;
			else
			{
				B = getNamedClass(rhs);
				normalise(B, rhs);
			}
			initialise(normalised, B);
		}
		else if(lhs instanceof Some)
		{
			Some some = (Some) lhs;
			Role role = some.role;
			Description filler = some.concept;
			Basic A = null;
			if(!(filler instanceof Basic))
			{
				A = getNamedClass(filler);
				normalise(filler, A);
			}
			else
				A = (Basic) filler;
			if(role.somes.get(A) == null)
				role.somes.put(A, some);
			if(rhs instanceof Basic)
				initialise(role, A, (Basic)rhs);
			else
			{
				Atomic B = getNamedClass(rhs);
				initialise(role, A, B);
				normalise(B, rhs);
			}
		}
		else if(lhs instanceof Basic)
		{
			if(rhs instanceof Some)
			{
				Some some = (Some) rhs;
				Role role = some.role;
				Description filler = some.concept;
				Basic B = null;
				if(!(filler instanceof Basic))
				{
					B = getNamedClass(filler);
					normalise(B, filler);
				}
				else
					B = (Basic)filler;
				initialise((Basic)lhs, role, B);
			}
			else if(rhs instanceof And)
			{
				And and = (And) rhs;
				for(Description operand:and.operands)
				{
					if(operand instanceof Basic)
						initialise((Basic)lhs, (Basic)operand);
					else
						normalise(lhs, operand);
				}
			}
			else
				initialise((Basic)lhs, (Basic)rhs);
		}
	}	

	abstract protected void initialise(Basic lhs, Basic rhs);

	abstract protected void initialise(HashSet<Basic> lhs, Basic rhs);

	abstract protected void initialise(Basic A, Role role, Basic B);

	abstract protected void initialise(Role role, Basic A, Basic B);

	public Atomic getNamedClass(Description desc) {
		Atomic A = normalisationNames.get(desc);
		if(A == null)
		{
			A = new Atomic();
			A.id = classindex;
			A.uri = URI.create(CLASS_PREFIX+classindex);
			A.original = false;
			classindex++;
			descriptions.put(A.id, A);
			normalisationNames.put(desc, A);
			imply(A);			
		}
		return A;
	}

	protected void imply(Basic A)
	{
		Implies simpleimply = new Implies();
		simpleimply.id = impliesID++;
		simpleimply.lhs = null;
		simpleimply.rhs = A;
		A.entry = simpleimply;
	}

	public void createInverseRoles() {
		// TODO Auto-generated method stub
	}
}

