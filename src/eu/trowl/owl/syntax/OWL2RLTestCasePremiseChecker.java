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
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owl.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
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
 * OWL 2 RL Test Case Premise Ontology Checker.
 * 
 * Assuming that the ontology meets the OWL 2 RL syntactic restrictions
 * defined in [1], this class checks that the ontology also meets the 
 * restrictions defined for O1 from Theorem PR1 [1].
 * 
 * " - neither O1 nor O2 contains a IRI that is used for more than one type 
 *     of entity (i.e., no IRIs is used both as, say, a class and an individual);
 *   - O1 does not contain SubAnnotationPropertyOf, AnnotationPropertyDomain, 
 *     and AnnotationPropertyRange axioms;" 
 * 
 * [1] http://www.w3.org/2007/OWL/wiki/Profiles#OWL_2_RL
 * 
 * @author Stuart Taylor
 * @version 2009-07-15
 */
public class OWL2RLTestCasePremiseChecker extends AbstractOWLSyntaxChecker implements OWLSyntaxChecker {

	private final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

	public void check(OWLOntology ontology) {
		Set<OWLOntology> importsClosure = manager.getImportsClosure(ontology);
		for (OWLOntology o : importsClosure) {
			// FIXME find a version of OWL API that supports annotation property domain, range and subsumption...
            //for (OWLAxiom axiom : o.getLogicalAxioms()) {
            //	AxiomChecker checker = new AxiomChecker();
            //	axiom.accept(checker);
            //	
            //	if (!checker.isValid())
            //		unsupported(axiom);
			//
            //}
			for (OWLEntity entity : o.getReferencedEntities()) {
				if (o.isPunned(entity.getURI())) {
					valid = false;
				
					if (lazyMode)
						throw new OWLSyntaxException("Punned URI: " + entity.getURI());
				}
			}

        }
	}
	
	public String getLanguageName() {
		return "OWL 2 RL Test Case Premise Ontology";
	}
	
	private class AxiomChecker extends AbstractChecker implements OWLAxiomVisitor {
		public void visit(OWLSubClassAxiom axiom) {	}
		public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) { }
		public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) { }
		public void visit(OWLReflexiveObjectPropertyAxiom axiom) { }
		public void visit(OWLDisjointClassesAxiom axiom) { }
		public void visit(OWLDataPropertyDomainAxiom axiom) { }
		public void visit(OWLImportsDeclaration axiom) { }
		public void visit(OWLAxiomAnnotationAxiom axiom) { }
		public void visit(OWLObjectPropertyDomainAxiom axiom) { }
		public void visit(OWLEquivalentObjectPropertiesAxiom axiom) { }
		public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) { }
		public void visit(OWLDifferentIndividualsAxiom axiom) { }
		public void visit(OWLDisjointDataPropertiesAxiom axiom) { }
		public void visit(OWLDisjointObjectPropertiesAxiom axiom) { }
		public void visit(OWLObjectPropertyRangeAxiom axiom) { }
		public void visit(OWLObjectPropertyAssertionAxiom axiom) { }
		public void visit(OWLFunctionalObjectPropertyAxiom axiom) { }
		public void visit(OWLObjectSubPropertyAxiom axiom) { }
		public void visit(OWLDisjointUnionAxiom axiom) { }
		public void visit(OWLDeclarationAxiom axiom) { }
		public void visit(OWLEntityAnnotationAxiom axiom) { }
		public void visit(OWLOntologyAnnotationAxiom axiom) { }
		public void visit(OWLSymmetricObjectPropertyAxiom axiom) { }
		public void visit(OWLDataPropertyRangeAxiom axiom) { }
		public void visit(OWLFunctionalDataPropertyAxiom axiom) { }
		public void visit(OWLEquivalentDataPropertiesAxiom axiom) { }
		public void visit(OWLClassAssertionAxiom axiom) { }
		public void visit(OWLEquivalentClassesAxiom axiom) { }
		public void visit(OWLDataPropertyAssertionAxiom axiom) { }
		public void visit(OWLTransitiveObjectPropertyAxiom axiom) { }
		public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) { }
		public void visit(OWLDataSubPropertyAxiom axiom) { }
		public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) { }
		public void visit(OWLSameIndividualsAxiom axiom) { }
		public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) { }
		public void visit(OWLInverseObjectPropertiesAxiom axiom) { }
		public void visit(SWRLRule rule) { }
	}
		
}