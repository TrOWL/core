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
 * along with TrOWL .  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 University of Aberdeen
 */

package eu.trowl.owl.rel.factory;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLAxiomAnnotationAxiom;
import org.semanticweb.owl.model.OWLAxiomVisitor;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owl.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLDataSubPropertyAxiom;
import org.semanticweb.owl.model.OWLDeclarationAxiom;
import org.semanticweb.owl.model.OWLDisjointClassesAxiom;
import org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLDisjointUnionAxiom;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom;
import org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owl.model.OWLImportsDeclaration;
import org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom;
import org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom;
import org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owl.model.OWLObjectSubPropertyAxiom;
import org.semanticweb.owl.model.OWLOntologyAnnotationAxiom;
import org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owl.model.SWRLRule;

import eu.trowl.owl.rel.util.UnsupportedFeatureException;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public abstract class OWL2DLAxiomVisitor implements OWLAxiomVisitor {

	public final Set<OWLAxiom> unsupportedAxioms = new HashSet<OWLAxiom>();

	protected boolean ignoreUnsupportedAxioms;

	protected OWL2DLAxiomVisitor(boolean ignoreUnsupportedAxioms) {
		this.ignoreUnsupportedAxioms = ignoreUnsupportedAxioms;
	}

	/**
	 * @return all unsupported axioms in the ontology
	 */
	public Set<OWLAxiom> getUnsupportedAxioms() {
		return unsupportedAxioms;
	}

	protected void unsupportedAxiom(OWLAxiom axiom) {
		if (ignoreUnsupportedAxioms)
			unsupportedAxioms.add(axiom);
		else
			throw new UnsupportedFeatureException("Unsupported axiom: " + axiom);
	}


	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLNegativeObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLAntiSymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLAntiSymmetricObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLReflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLReflexiveObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}


	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDataPropertyDomainAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyDomainAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLImportsDeclaration)
	 */
	@Override
	public void visit(OWLImportsDeclaration axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLAxiomAnnotationAxiom)
	 */
	@Override
	public void visit(OWLAxiomAnnotationAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLEquivalentObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentObjectPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLNegativeDataPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}


	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDisjointDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointDataPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDisjointObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLDisjointObjectPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLObjectPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyRangeAxiom axiom) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLObjectPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyAssertionAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLObjectSubPropertyAxiom)
	 */
	@Override
	public void visit(OWLObjectSubPropertyAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDisjointUnionAxiom)
	 */
	@Override
	public void visit(OWLDisjointUnionAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDeclarationAxiom)
	 */
	@Override
	public void visit(OWLDeclarationAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLEntityAnnotationAxiom)
	 */
	@Override
	public void visit(OWLEntityAnnotationAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLOntologyAnnotationAxiom)
	 */
	@Override
	public void visit(OWLOntologyAnnotationAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLSymmetricObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLSymmetricObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDataPropertyRangeAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyRangeAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLFunctionalDataPropertyAxiom)
	 */
	@Override
	public void visit(OWLFunctionalDataPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLEquivalentDataPropertiesAxiom)
	 */
	@Override
	public void visit(OWLEquivalentDataPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLClassAssertionAxiom)
	 */
	@Override
	public void visit(OWLClassAssertionAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);
	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDataPropertyAssertionAxiom)
	 */
	@Override
	public void visit(OWLDataPropertyAssertionAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLTransitiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLTransitiveObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLIrreflexiveObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLDataSubPropertyAxiom)
	 */
	@Override
	public void visit(OWLDataSubPropertyAxiom axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLInverseFunctionalObjectPropertyAxiom)
	 */
	@Override
	public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);

	}


	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLObjectPropertyChainSubPropertyAxiom)
	 */
	@Override
	public void visit(OWLObjectPropertyChainSubPropertyAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.OWLInverseObjectPropertiesAxiom)
	 */
	@Override
	public void visit(OWLInverseObjectPropertiesAxiom axiom) {
		// TODO Auto-generated method stub
//		unsupportedAxiom(axiom);

	}

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.model.OWLAxiomVisitor#visit(org.semanticweb.owl.model.SWRLRule)
	 */
	@Override
	public void visit(SWRLRule axiom) {
		// TODO Auto-generated method stub
		unsupportedAxiom(axiom);

	}

	@Override
	public void visit(OWLEquivalentClassesAxiom axiom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(OWLDisjointClassesAxiom axiom) {
		// TODO Auto-generated method stub

	}
	
	

}
