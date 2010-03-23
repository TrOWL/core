package eu.trowl.owl.syntax;

import java.util.Set;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.vocab.OWLDatatypeVocabulary;

/**
 * OWL 2 EL Profile Syntax Checker
 * http://www.w3.org/2007/OWL/wiki/Profiles#OWL_2_EL
 * 
 * @author Stuart Taylor
 * @version 2009-08-20
 */
public class OWL2ELChecker extends AbstractOWLSyntaxChecker implements OWLSyntaxChecker {

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
		return "OWL 2 EL";
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
			if (axiom.getSubject().isAnonymous() || axiom.getObject().isAnonymous()) {
				valid = false;
				return;
			}
			
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			axiom.getProperty().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			
			axiom.getProperty().accept(checker);
			if (!checker.isValid())
				valid = false;
			
		}
		public void visit(OWLDisjointClassesAxiom axiom) {
			DescriptionChecker checker = new DescriptionChecker();
			
			for (OWLDescription desc : axiom.getDescriptions()) {
				desc.accept(checker);
				if (!checker.isValid()) {
					valid = false;
					break;
				}	
			}
		}
		public void visit(OWLDataPropertyDomainAxiom axiom) {
			// valid.
		}
		public void visit(OWLImportsDeclaration axiom) {
			// ignore.
		}
		public void visit(OWLAxiomAnnotationAxiom axiom) {
			// ignore.
		}
		public void visit(OWLObjectPropertyDomainAxiom axiom) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			
			axiom.getProperty().accept(checker);
			if (!checker.isValid())
				valid = false;
			
		}
		public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			
			for (OWLPropertyExpression p : axiom.getProperties()) {
				p.accept(checker);
				
				if (!checker.isValid()) {
					valid = false;
					break;
				}
			}
		}
		public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
			PropertyExpressionChecker expChecker = new PropertyExpressionChecker();
			axiom.getProperty().accept(expChecker);
			
			if (!expChecker.isValid()) {
				valid = false;
				return;
			}
			
			if (axiom.getObject().isTyped()) {
				DataRangeChecker dtChecker = new DataRangeChecker();
				axiom.getObject().asOWLTypedConstant().getDataType().accept(dtChecker);
				
				if (!dtChecker.isValid()) {
					valid = false;
					return;
				}
			}
		}
		public void visit(OWLDifferentIndividualsAxiom axiom) {
			for (OWLIndividual ind : axiom.getIndividuals()) {
				if (ind.isAnonymous()) {
					valid = false;
					return;
				}
			}
		}
		public void visit(OWLDisjointDataPropertiesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectPropertyRangeAxiom axiom) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			
			axiom.getProperty().accept(checker);
			if (!checker.isValid())
				valid = false;
			
		}
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			//if (axiom.getSubject().isAnonymous() || axiom.getObject().isAnonymous()) {
			//	valid = false;
			//	return;
			//}
			
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			axiom.getProperty().accept(checker);
			
			if (!checker.isValid())
				valid = false;
			
		}
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectSubPropertyAxiom axiom) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			
			axiom.getSubProperty().accept(checker);
			if (!checker.isValid()) {
				valid = false;
				return;
			}
			
			axiom.getSuperProperty().accept(checker);
			if (!checker.isValid()) {
				valid = false;
				return;
			}
		}
		public void visit(OWLDisjointUnionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDeclarationAxiom axiom) {
			// TODO what's this?
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
			DataRangeChecker checker = new DataRangeChecker();
			axiom.getRange().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLFunctionalDataPropertyAxiom axiom) {
			// valid.
		}
		public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
			// valid.
		}
		public void visit(OWLClassAssertionAxiom axiom) {
			//if (axiom.getIndividual().isAnonymous()) {
			//	valid = false;
			//	return;
			//}
			
			DescriptionChecker checker = new DescriptionChecker();
			axiom.getDescription().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLEquivalentClassesAxiom axiom) {
			DescriptionChecker checker = new DescriptionChecker();
			
			for (OWLDescription desc : axiom.getDescriptions()) {
				desc.accept(checker);
				if (!checker.isValid()) {
					valid = false;
					break;
				}	
			}
		}
		public void visit(OWLDataPropertyAssertionAxiom axiom) {
			if (axiom.getSubject().isAnonymous())
				valid = false;
				
			
			if (axiom.getObject().isTyped()) {
				DataRangeChecker dtChecker = new DataRangeChecker();
				axiom.getObject().asOWLTypedConstant().getDataType().accept(dtChecker);
				
				if (!dtChecker.isValid()) {
					valid = false;
					return;
				}
			}

		}
		public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			
			axiom.getProperty().accept(checker);
			if (!checker.isValid())
				valid = false;
			
		}
		public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDataSubPropertyAxiom axiom) {
			// valid.
		}
		public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLSameIndividualsAxiom axiom) {
			for (OWLIndividual ind : axiom.getIndividuals()) {
				if (ind.isAnonymous() || ind.isOWLClass()) {
					valid = false;
					break;
				}
			}
		}
		public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			
			axiom.getSuperProperty().accept(checker);
			if (!checker.isValid()) {
				valid = false;
				return;
			}
			
			for (OWLPropertyExpression p : axiom.getPropertyChain()) {
				p.accept(checker);
				
				if (!checker.isValid()) {
					valid = false;
					break;
				}
			}
		}
		public void visit(OWLInverseObjectPropertiesAxiom axiom) {
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
			DescriptionChecker descChecker = new DescriptionChecker();
			desc.getFiller().accept(descChecker);
			
			if (!descChecker.isValid()) {
				valid = false;
				return;
			}
			
			PropertyExpressionChecker propChecker = new PropertyExpressionChecker();
			desc.getProperty().accept(propChecker);
			
			if (!propChecker.isValid()) {
				valid = false;
				return;
			}
		}
		public void visit(OWLObjectAllRestriction desc) {
			valid = false;
		}
		public void visit(OWLObjectValueRestriction desc) {
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			desc.getProperty().accept(checker);
			
			if (!checker.isValid())
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
			PropertyExpressionChecker checker = new PropertyExpressionChecker();
			desc.getProperty().accept(checker);
			
			if (!checker.isValid())
				valid = false;

		}
		public void visit(OWLObjectOneOf desc) {
			if (desc.getIndividuals().size() != 1) 
				valid = false;
		}
		public void visit(OWLDataSomeRestriction desc) {
			DataRangeChecker checker = new DataRangeChecker();
			desc.getFiller().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLDataAllRestriction desc) {
			DataRangeChecker checker = new DataRangeChecker();
			desc.getFiller().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLDataValueRestriction desc) {
			if (desc.getValue().isTyped()) {
				DataRangeChecker checker = new DataRangeChecker();
				desc.getValue().asOWLTypedConstant().getDataType().accept(checker);
			
				if (!checker.isValid())
					valid = false;
			}
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
			valid = true;
		}
		public void visit(OWLObjectPropertyInverse property) {
			valid = false;
		}
		public void visit(OWLDataProperty property) {
			valid = true;
		}
		
	}
	
	private class DataRangeChecker extends AbstractChecker implements OWLDataVisitor {
		
		// TODO support DataIntersectionOf
		
		public void visit(OWLDataType node) {
			if (!OWLDatatypeVocabulary.isBuiltIn(node.getURI())) {
				valid = false;
				return;
			}
			
			switch (node.getBuiltInDatatype()) {
				case XSD_DOUBLE:
				case XSD_FLOAT:
				case XSD_NON_POSITIVE_INTEGER:
				case XSD_POSITIVE_INTEGER:
				case XSD_NEGATIVE_INTEGER:
				case XSD_LONG:
				case XSD_INT:
				case XSD_SHORT:
				case XSD_BYTE:
				case XSD_UNSIGNED_LONG:
				case XSD_UNSIGNED_INT:
				case XSD_UNSIGNED_SHORT:
				case XSD_UNSIGNED_BYTE:
				case XSD_LANGUAGE:
				case XSD_BOOLEAN:
					valid = false;
			}
			
		}
		public void visit(OWLDataComplementOf node) {
			DataRangeChecker checker = new DataRangeChecker();
			node.getDataRange().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLDataOneOf node) {
			if (node.getValues().size() != 1)
				valid = false;
		}
		public void visit(OWLDataRangeRestriction node) {
			valid = false;
		}
		public void visit(OWLTypedConstant node) {
			valid = false;
		}
		public void visit(OWLUntypedConstant node) {
			valid = false;
		}
		public void visit(OWLDataRangeFacetRestriction node) {
			valid = false;
		}
		
	}
	
}