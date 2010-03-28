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

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import eu.trowl.owl.rel.model.Atomic;
import eu.trowl.owl.rel.model.Basic;
import eu.trowl.owl.rel.model.CardinalityEntry;
import eu.trowl.owl.rel.model.ELCQOntology;
import eu.trowl.owl.rel.model.ERestriction;
import eu.trowl.owl.rel.model.Implies;
import eu.trowl.owl.rel.model.Individual;
import eu.trowl.owl.rel.model.Role;
import eu.trowl.owl.rel.model.Some;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class PermanentELCOntologyBuilder extends ELCOntologyBuilder {

	public PermanentELCOntologyBuilder(Set<OWLOntology> ontologies,
			OWLOntologyManager manager, ELCQOntology ELContology) {
		super(manager, ELContology);
		// TODO Auto-generated constructor stub
		OWLClass thing = factory.getOWLThing();
		OWLClass nothing = factory.getOWLNothing();
		OWLClass namedindividual = factory.getOWLClass(URI.create("http://www.w3.org/2002/07/owl#NamedIndividual"));

		top = new Atomic(thing);
		top.id = 1;
		top.original = true;
		bot = new Atomic(nothing);
		bot.id = 0;
		bot.original = true;
		top.complement = bot;
		bot.complement = top;

		classID.put(nothing, 0);
		classID.put(thing, 1);
		allconcepts.add(top);

		classindex = 2;

		descriptions.put(0, bot);
		imply(bot);
		descriptions.put(1, top);
		imply(top);

		for(OWLOntology ontology:ontologies){
			for(OWLClass concept:ontology.getReferencedClasses())
			{
				if(concept.equals(thing) || concept.equals(nothing) || concept.equals(namedindividual))
					continue;
				Atomic newconcept = new Atomic(concept);
				newconcept.id = classindex++;
				classID.put(concept, newconcept.id);
				descriptions.put(newconcept.id, newconcept);
				imply(newconcept);
				allconcepts.add(newconcept);
			}
			for(OWLObjectProperty role:ontology.getReferencedObjectProperties())
			{
				Role newrole = new Role(role);
				newrole.id = propertyindex++;
				propertyID.put(role, newrole.id);
				roles.put(newrole.id, newrole);

			}
			for(OWLIndividual individual:ontology.getReferencedIndividuals())
			{
				Individual newindividual = new Individual(individual);
				newindividual.id = individualindex++;
				individualID.put(individual, newindividual.id);
				individuals.put(newindividual.id, newindividual);
			}
		}
	}

	protected void initialise(Basic lhs, Basic rhs)
	{
		lhs.Ohat.add(rhs.entry);
	}

	protected void initialise(HashSet<Basic> lhs, Basic rhs)
	{
		if(lhs.size() == 1)
		{
			Implies imply = rhs.entry;
			lhs.iterator().next().Ohat.add(imply);
		}
		else
		{

			for (Basic desc : lhs)
			{
				Implies implies = new Implies();
				implies.rhs = rhs;
				for (Basic company : lhs)
					if(!company.equals(desc))
						implies.lhs.add(company);
				implies.id = impliesID++;
				desc.Ohat.add(implies);
			}
		}
	}

	protected void initialise(Basic A, Role role, Basic B)
	{
		HashMap<Basic, ERestriction> restrictions = elcontology.simpleExistEntries.get(role); 
		if( restrictions == null)
		{
			restrictions = new HashMap<Basic, ERestriction>();
			elcontology.simpleExistEntries.put(role, restrictions);
		}
		ERestriction exists = restrictions.get(B);
		if(exists == null)
		{
			exists = new ERestriction();
			exists.concept = B;
			exists.role = role;
			exists.id = impliesID++;
			restrictions.put(B, exists);
		}
		A.Ohat.add(exists);
	}

	protected void initialise(Role role, Basic A, Basic B)
	{
		Some some = elcontology.roles.get(role.id).somes.get(A);
		if(some == null)
		{
			some = new Some(role, A);
			some.id = nonbasicindex;
			nonbasicindex--;
			descriptions.put(some.id, some);
			elcontology.roles.get(role.id).somes.put(A, some);			
		}
		some.Ohat.add(B.entry);
	}

	public void OrderingCardinality() {
		// TODO Auto-generated method stub
		for(Entry<Atomic, HashMap<Integer, Atomic>> entry:CardinalityTable.entrySet())
		{
			Atomic filler = entry.getKey();
			HashMap<Integer, Atomic> cardins = entry.getValue();
			CardinalityEntry[] number = new CardinalityEntry[cardins.size()];
			int size = 0;
			for(int n:cardins.keySet())
			{
				int i,j;
				for(i = 0;i < size;i++)
					if(number[i].n < n)
						break;
				for(j = size;j>i;j--)
					number[j] = number[j-1];
				CardinalityEntry newentry = new CardinalityEntry(cardins.get(n), n);
				number[i] = newentry;
				size++;
			}
			filler.cardins = number;
			for(int i = 0;i<size-1;i++)
				normalise(number[i].basen, number[i+1].basen);
		}

		// another important task, initialise the value of concept number, role number, etc.
		elcontology.classNum = classindex;
		elcontology.permanentclassNum = classindex;
		elcontology.roleNum = propertyindex;
		elcontology.individualNum = individualindex;
	}
}
