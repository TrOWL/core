package eu.trowl.owl.syntax;

import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLAxiomVisitor;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLDataAllRestriction;
import org.semanticweb.owl.model.OWLDataComplementOf;
import org.semanticweb.owl.model.OWLDataExactCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataMaxCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataMinCardinalityRestriction;
import org.semanticweb.owl.model.OWLDataOneOf;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLDataRangeFacetRestriction;
import org.semanticweb.owl.model.OWLDataRangeRestriction;
import org.semanticweb.owl.model.OWLDataSomeRestriction;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDataType;
import org.semanticweb.owl.model.OWLDataValueRestriction;
import org.semanticweb.owl.model.OWLDataVisitor;
import org.semanticweb.owl.model.OWLDeclarationAxiom;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLDescriptionVisitor;
import org.semanticweb.owl.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointUnionAxiom;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectAllRestriction;
import org.semanticweb.owl.model.OWLObjectComplementOf;
import org.semanticweb.owl.model.OWLObjectExactCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectMaxCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectMinCardinalityRestriction;
import org.semanticweb.owl.model.OWLObjectOneOf;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSelfRestriction;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectUnionOf;
import org.semanticweb.owl.model.OWLObjectValueRestriction;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyAnnotationAxiom;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLSameIndividualsAxiom;
import org.semanticweb.owl.model.OWLSubClassAxiom;
import org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTypedConstant;
import org.semanticweb.owl.model.OWLUntypedConstant;
import org.semanticweb.owl.model.SWRLRule;

/**
 * OWL 2 RL Test Case Conclusion Ontology Checker.
 * 
 * Assuming that the ontology meets the OWL 2 RL syntactic restrictions
 * defined in [1], this class checks that the ontology also meets the 
 * restrictions defined for O2 from Theorem PR1 [1].
 * 
 * "each axiom in O2 is an assertion of the form as specified below, 
 *  for a, a1, ..., an named individuals:
 *    - ClassAssertion( C a ) where C is a class,
 *    - ObjectPropertyAssertion( OP a1 a2 ) where OP is an object property,
 *    - DataPropertyAssertion( DP a v ) where DP is a data property, or
*       SameIndividual( a1 ... an ). 
 * 
 * [1] http://www.w3.org/2007/OWL/wiki/Profiles#OWL_2_RL
 * 
 * @author Stuart Taylor
 * @version 2009-07-15
 */
public class OWL2RLTestCaseConclusionChecker extends AbstractOWLSyntaxChecker implements OWLSyntaxChecker {

	private final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

	public void check(OWLOntology ontology) {
		Set<OWLOntology> importsClosure = manager.getImportsClosure(ontology);
		for (OWLOntology o : importsClosure) {
            for (OWLAxiom axiom : o.getLogicalAxioms()) {
            	AxiomChecker checker = new AxiomChecker();
            	axiom.accept(checker);
            	
            	if (!checker.isValid())
            		unsupported(axiom);

            }

        }
	}
	
	public String getLanguageName() {
		return "OWL 2 RL Test Case Conclusion Ontology";
	}
	
	private class AxiomChecker extends AbstractChecker implements OWLAxiomVisitor {
		public void visit(OWLSubClassAxiom axiom) {
			valid = false;
		}
		public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDisjointClassesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDataPropertyDomainAxiom axiom) {
			valid = false;
		}
		public void visit(OWLImportsDeclaration axiom) {
			valid = false;
		}
		public void visit(OWLAxiomAnnotationAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectPropertyDomainAxiom axiom) {
			valid = false;
		}
		public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDifferentIndividualsAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDisjointDataPropertiesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectPropertyRangeAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectPropertyAssertionAxiom axiom) { 
			if (axiom.getProperty().isAnonymous() || axiom.getSubject().isAnonymous() || axiom.getObject().isAnonymous())
				valid = false;
		}
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectSubPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDisjointUnionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDeclarationAxiom axiom) {
			valid = false;
		}
		public void visit(OWLEntityAnnotationAxiom axiom) {
			valid = false;
		}
		public void visit(OWLOntologyAnnotationAxiom axiom) {
			valid = false;
		}
		public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDataPropertyRangeAxiom axiom) {
			valid = false;
		}
		public void visit(OWLFunctionalDataPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLClassAssertionAxiom axiom) { 
			if (axiom.getDescription().isAnonymous() || axiom.getIndividual().isAnonymous())
				valid = false;			
		}
		public void visit(OWLEquivalentClassesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDataPropertyAssertionAxiom axiom) { 
			if (axiom.getProperty().isAnonymous() || axiom.getSubject().isAnonymous())
				valid = false;
		}
		public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDataSubPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLSameIndividualsAxiom axiom) { 
			for (OWLIndividual individual : axiom.getIndividuals()) {
				if (individual.isAnonymous()) {
					valid = false;
					break;
				}
			}
		}
		public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLInverseObjectPropertiesAxiom axiom) {
			valid = false;
		}
		public void visit(SWRLRule rule) {
			valid = false;
		}
	}
	
	
}