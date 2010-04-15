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
import org.semanticweb.owl.vocab.OWLDatatypeVocabulary;

/**
 * OWL 2 QL Profile Syntax Checker
 * http://www.w3.org/2007/OWL/wiki/Profiles#OWL_2_QL
 * 
 * @author Stuart Taylor
 * @version 2009-08-20
 */
public class OWL2QLChecker extends AbstractOWLSyntaxChecker implements OWLSyntaxChecker {

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
		return "OWL 2 QL";
	}
	
	private class AxiomChecker extends AbstractChecker implements OWLAxiomVisitor {
		
		public void visit(OWLSubClassAxiom axiom) {
			LHSDescriptionChecker lhsChecker = new LHSDescriptionChecker();
			RHSDescriptionChecker rhsChecker = new RHSDescriptionChecker();
			
			axiom.getSubClass().accept(lhsChecker);
			if (!lhsChecker.isValid()) {
				valid = false;
				return;
			}
			
			axiom.getSuperClass().accept(rhsChecker);
			if (!rhsChecker.isValid()) {
				valid = false;
				return;
			}
		}
		public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
			// valid.
		}
		public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
			// valid.
		}
		public void visit(OWLDisjointClassesAxiom axiom) {
			LHSDescriptionChecker checker = new LHSDescriptionChecker();
			
			for (OWLDescription desc : axiom.getDescriptions()) {
				desc.accept(checker);
				
				if (!checker.isValid()) {
					valid = false;
					break;
				}
			}
		}
		public void visit(OWLDataPropertyDomainAxiom axiom) {
			RHSDescriptionChecker checker = new RHSDescriptionChecker();
			axiom.getDomain().accept(checker);
			
			if (!checker.isValid())
				valid = false;

		}
		public void visit(OWLImportsDeclaration axiom) {
			// ignore.
		}
		public void visit(OWLAxiomAnnotationAxiom axiom) {
			// ignore.
		}
		public void visit(OWLObjectPropertyDomainAxiom axiom) {
			RHSDescriptionChecker checker = new RHSDescriptionChecker();
			axiom.getDomain().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
			// valid.
		}
		public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
			valid = false;
		}
		public void visit(OWLDifferentIndividualsAxiom axiom) {
			for (OWLIndividual ind : axiom.getIndividuals()) {
				if (ind.isAnonymous()) {
					valid = false;
					break;
				}
			}
		}
		public void visit(OWLDisjointDataPropertiesAxiom axiom) {
			// valid.
		}
		public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
			// valid.
		}
		public void visit(OWLObjectPropertyRangeAxiom axiom) {
			RHSDescriptionChecker checker = new RHSDescriptionChecker();
			axiom.getRange().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLObjectPropertyAssertionAxiom axiom) {
			if (axiom.getSubject().isAnonymous() || axiom.getObject().isAnonymous())
				valid = false;
			
		}
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLObjectSubPropertyAxiom axiom) {
			// valid.
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
			// valid.
		}
		public void visit(OWLDataPropertyRangeAxiom axiom) {
			DataRangeChecker checker = new DataRangeChecker();
			axiom.getRange().accept(checker);
			
			if (!checker.isValid())
				valid = false;
			
		}
		public void visit(OWLFunctionalDataPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
			// valid.
		}
		public void visit(OWLClassAssertionAxiom axiom) {
			if (axiom.getDescription().isAnonymous() || axiom.getIndividual().isAnonymous())
				valid = false;
		}
		public void visit(OWLEquivalentClassesAxiom axiom) {
			LHSDescriptionChecker checker = new LHSDescriptionChecker();
			
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
		}
		public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
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
			valid = false;
		}
		public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
			valid = false;
		}
		public void visit(OWLInverseObjectPropertiesAxiom axiom) {
			// valid.
		}
		public void visit(SWRLRule rule) {
			valid = false;
		}
	}
	
	private class LHSDescriptionChecker extends AbstractChecker implements OWLDescriptionVisitor {
		
		public void visit(OWLClass desc) {
			// valid.
		}
		public void visit(OWLObjectIntersectionOf desc) {
			valid = false;
		}
		public void visit(OWLObjectUnionOf desc) {
			valid = false;
		}
		public void visit(OWLObjectComplementOf desc) {
			valid = false;
		}
		public void visit(OWLObjectSomeRestriction desc) {
			if (!desc.getFiller().isOWLThing()) 
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
			DataRangeChecker checker = new DataRangeChecker();
			desc.getFiller().accept(checker);
			
			if (!checker.isValid())
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
	
	private class RHSDescriptionChecker extends AbstractChecker implements OWLDescriptionVisitor {
		
		public void visit(OWLClass desc) {
			// valid.
		}
		public void visit(OWLObjectIntersectionOf desc) {
			RHSDescriptionChecker checker = new RHSDescriptionChecker();
			
			for (OWLDescription oper : desc.getOperands()) {
				oper.accept(checker);
				
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
			LHSDescriptionChecker checker = new LHSDescriptionChecker();
			desc.getOperand().accept(checker);
			
			if (!checker.isValid())
				valid = false;
		}
		public void visit(OWLObjectSomeRestriction desc) {
			if (desc.getFiller().isAnonymous())
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
			DataRangeChecker checker = new DataRangeChecker();
			desc.getFiller().accept(checker);
			
			if (!checker.isValid())
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
	
	private class DataRangeChecker extends AbstractChecker implements OWLDataVisitor {

		// FIXME DataIntersectionOf should be valid.
		
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
			valid = false;
		}

		public void visit(OWLDataOneOf node) {
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