package eu.trowl.owl.syntax;

import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.*;

/**
 * OWL 2 EL+ Fragment Syntax Checker
 * 
 * EL+ Syntax:
 *   C ::= A | exists R.C | C1 and C2 | T
 *   C1 <= C2
 *   r1 o r2 <= R3
 *   C(a), R(a,b)
 *   
 * Additionally by rewriting:
 *   Concept equivalence, Property Equivalence, 
 *   Transitivity, Object Domain, Object Range.
 * 
 * @author Stuart Taylor
 * @version 2009-02-06
 */
public class OWL2ELPlusChecker extends AbstractOWLSyntaxChecker implements OWLSyntaxChecker {
	
	private final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private final boolean allowAxiomRewriting;	
	
	public OWL2ELPlusChecker(boolean disableAxiomRewriting) {
		this.allowAxiomRewriting = !disableAxiomRewriting;
	}
	
	public OWL2ELPlusChecker() {
		this(false);
	}
	
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
		return "EL+ OWL 2 Fragment";
	}

	private class AxiomChecker extends AbstractChecker implements OWLAxiomVisitor {
		
		public void visit(OWLSubClassAxiom axiom) {
			DescriptionChecker checker = new DescriptionChecker();

			axiom.getSubClass().accept(checker);
			if (!checker.isValid()) {
				valid = false;
				return;
			}
			
			axiom.getSuperClass().accept(checker);
			if (!checker.isValid()) {
				valid = false;
				return;
			}
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
			// ignore.
		}
		public void visit(OWLAxiomAnnotationAxiom axiom) {
			// ignore.
		}
		public void visit(OWLObjectPropertyDomainAxiom axiom) {
			valid = allowAxiomRewriting;
		}
		public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
			valid = allowAxiomRewriting;
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
			if (allowAxiomRewriting) {
				PropertyExpressionChecker checker = new PropertyExpressionChecker();
				axiom.getProperty().accept(checker);

				if (!checker.isValid())
					valid = false;

			} else {
				valid = false;
			}
		}
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			if (axiom.getObject().isAnonymous()) {
				valid = false;
				return;
			}
			
			PropertyExpressionChecker propertyChecker = new PropertyExpressionChecker();
			axiom.getProperty().accept(propertyChecker);
			
			if (!propertyChecker.isValid())
				valid = false;

		}
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectSubPropertyAxiom axiom) {
			PropertyExpressionChecker propertyChecker = new PropertyExpressionChecker();
			
			axiom.getSubProperty().accept(propertyChecker);
			if (!propertyChecker.isValid()) {
				valid = false;
				return;
			}
			
			axiom.getSuperProperty().accept(propertyChecker);
			if (!propertyChecker.isValid()) {
				valid = false;
				return;
			}
		}
		public void visit(OWLDisjointUnionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDeclarationAxiom axiom) {
			// what?
		}
		public void visit(OWLEntityAnnotationAxiom axiom) {
			// ignore.
		}
		public void visit(OWLOntologyAnnotationAxiom axiom) {
			// ignore.
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
			if (axiom.getIndividual().isAnonymous()) {
				valid = false;
				return;
			}
			
			DescriptionChecker checker = new DescriptionChecker();
			axiom.getDescription().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLEquivalentClassesAxiom axiom) {
			if (allowAxiomRewriting) {
				DescriptionChecker checker = new DescriptionChecker();

				for (OWLDescription desc : axiom.getDescriptions()) {
					desc.accept(checker);
					if (!checker.isValid()) {
						valid = false;
						break;
					}
				}
			} else {
				valid = false;
			}
		}
		public void visit(OWLDataPropertyAssertionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
			valid = allowAxiomRewriting;
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
			valid = false;
		}
		public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
			if (axiom.getSuperProperty().isAnonymous())
				valid = false;	
		}
		public void visit(OWLInverseObjectPropertiesAxiom axiom) {
			//valid = allowAxiomRewriting;
			valid = false;
		}
		public void visit(SWRLRule rule) {
			valid = false;
		}
	
	}
	
	private class DescriptionChecker extends AbstractChecker implements OWLDescriptionVisitor {
		
		public void visit(OWLClass desc) {
			// valid.
		}
		public void visit(OWLObjectIntersectionOf desc) {
			DescriptionChecker checker = new DescriptionChecker();
			
			for (OWLDescription c : desc.getOperands()) {
				c.accept(checker);
				
				if (!checker.isValid()) {
					valid = false;
					break;
				}
			}
				
		}
		public void visit(OWLObjectUnionOf desc) {
			valid = false;
		}
		public void visit(OWLObjectComplementOf desc) {
			valid = false;
		}
		public void visit(OWLObjectSomeRestriction desc) {
			if (desc.getProperty().isAnonymous()) {
				valid = false;
				return;
			}
			
			DescriptionChecker checker = new DescriptionChecker();
			desc.getFiller().accept(checker);
			
			if (!checker.isValid())
				valid = false;
			
		}
		public void visit(OWLObjectAllRestriction desc) {
			valid = false;
		}
		public void visit(OWLObjectValueRestriction desc) {
			valid = false;
		}
		public void visit(OWLObjectMinCardinalityRestriction desc) {
			valid = false;
		}
		public void visit(OWLObjectExactCardinalityRestriction desc) {
			valid = false;
		}
		public void visit(OWLObjectMaxCardinalityRestriction desc) {
			valid = false;
		}
		public void visit(OWLObjectSelfRestriction desc) {
			valid = false;
		}
		public void visit(OWLObjectOneOf desc) {
			valid = false;
		}
		public void visit(OWLDataSomeRestriction desc) {
			valid = false;
		}
		public void visit(OWLDataAllRestriction desc) {
			valid = false;
		}
		public void visit(OWLDataValueRestriction desc) {
			valid = false;
		}
		public void visit(OWLDataMinCardinalityRestriction desc) {
			valid = false;
		}
		public void visit(OWLDataExactCardinalityRestriction desc) {
			valid = false;
		}
		public void visit(OWLDataMaxCardinalityRestriction desc) {
			valid = false;
		}
	}
	
	private class PropertyExpressionChecker extends AbstractChecker implements OWLPropertyExpressionVisitor {

		public void visit(OWLObjectProperty property) {
			// valid.
		}

		public void visit(OWLObjectPropertyInverse property) {
			//valid = allowAxiomRewriting;
			valid = false;
		}

		public void visit(OWLDataProperty property) {
			valid = false;
		}
		
	}
	
}