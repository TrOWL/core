package eu.trowl.owl.rel.classify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import eu.trowl.owl.rel.model.Atomic;
import eu.trowl.owl.rel.model.Basic;
import eu.trowl.owl.rel.model.CardinalityEntry;
import eu.trowl.owl.rel.model.Description;
import eu.trowl.owl.rel.model.ELCQEntry;
import eu.trowl.owl.rel.model.ELCQOntology;
import eu.trowl.owl.rel.model.ERestriction;
import eu.trowl.owl.rel.model.Implies;
import eu.trowl.owl.rel.model.Individual;
import eu.trowl.owl.rel.model.Role;
import eu.trowl.owl.rel.model.RoleConcept;
import eu.trowl.owl.rel.model.Singleton;
import eu.trowl.owl.rel.model.Some;

/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class ELCClassifier {

	public ELCQOntology ontology;
	Atomic bot;

	public void tBoxCompletion()
	{
		bot = (Atomic) ontology.descriptions.get(0);
		initialize();
		boolean toprocess = true;
		int count = 0;
		Description[] tempqueue = null;
		while(toprocess && ontology.consistency)
		{
			while(toprocess && ontology.consistency)
			{
				count ++;
				toprocess = false;
				for(int i = 1;i < ontology.classNum;i ++)	// we don't process bot
				{
					Basic desc = (Basic)ontology.descriptions.get(i);
					if(desc.subsumers.contains(bot))	// we don't process bot!
						continue;
					while(!desc.subsumers.contains(bot) && desc.queue.size()>0)
					{
						tempqueue = null;
						tempqueue= new Description[desc.queue.size()];
						int index = 0;
						for(Description entry:desc.queue)
							tempqueue[index++] = entry;
						desc.queue = null;
						desc.queue = new HashSet<Description>();
						for(int j = 0;j < tempqueue.length;j ++)
						{
							if(tempqueue[j] instanceof Basic)
							{
								Basic basic = (Basic) tempqueue[j];
								toprocess = process(desc, basic.entry)?true:toprocess;
							}
							for(ELCQEntry X:tempqueue[j].Ohat)
								toprocess = process(desc,X)? true:toprocess;
						}
					}
					tempqueue = null;
				}
			}

			// nominals
			for(Individual indi:ontology.individuals.values())
			{
				Singleton single = indi.singleton;
				if(single != null)
				{
					for(Basic B:single.synonyms)
					{
						if(B.hasModel)
						{
							for(Basic A:single.synonyms)
							{
								if(!A.subsumers.contains(B))
								{
									toprocess = true;
									Implies implies = B.entry;
									A.Ohat.add(implies);
									A.queue.add(B);
								}
							}
						}
						else
						{
							for(Basic A:single.synonyms)
							{
								if(!A.subsumers.contains(B) && reach(A,B))
								{
									toprocess = true;
									Implies implies = B.entry;
									A.Ohat.add(implies);
									A.queue.add(B);								
								}
							}
						}
					}
				}
			}
			// end for nominal
		}
		tBoxPostprocessing();
	}

	public void initialize()
	{
		boolean roleclosure = true;
		while(roleclosure)
		{
			roleclosure = false;
			for(int i = 0;i < ontology.roleNum;i ++)
			{
				Role r = ontology.roles.get(i);
				HashSet<Role> tempset1 = new HashSet<Role>(r.subsumers);
				tempset1.remove(r);
				for(Role j:tempset1)
				{
					roleclosure = r.subsumers.addAll(j.subsumers)? true:roleclosure;
					j.transitive = j.transitive || r.transitive;
				}
			}
		}
		Atomic top = (Atomic) ontology.descriptions.get(1);
		for(int i = 2;i < ontology.classNum;i ++)
		{
			Basic desc = (Basic) ontology.descriptions.get(i);
			addAll2queue(desc, desc);
			addAll2queue(desc, top);
		}
		addAll2queue(top, top);
		for(int i = 1;i < ontology.classNum;i ++)
		{
			Basic desc = (Basic) ontology.descriptions.get(i);
			Basic concept = (Basic) desc;
			concept.subsumers.add(concept);
			concept.subsumers.add(top);
		}
		bot.subsumers.add(bot);		
	}

	public boolean process(Basic A, ELCQEntry X)
	{
		boolean toprocess = false;
		if(X instanceof Implies)
		{
			Implies newX = (Implies) X;
			Basic B = newX.rhs;
			HashSet<Basic> SA = A.subsumers;
			ArrayList<Basic> Bs = newX.lhs;
			if(! SA.contains(B))
			{
				if(Bs == null || SA.containsAll(Bs))
				{
					toprocess = true;
					addSubsumer(A,B);
				}

				if(Bs != null && B.subsumers.contains(bot))	// B is \bot
				{
					for(Basic candidate:Bs)
					{
						Set<Basic> newBs = new HashSet<Basic>(Bs);
						newBs.remove(candidate);
						Basic newsuper = candidate.complement;
						if(newsuper != null && SA.containsAll(newBs) && !SA.contains(newsuper))
						{
							toprocess = true;
							addSubsumer(A,newsuper);
						}
					}
				}
			}
		}
		else//(X instanceof ERestriction)
		{
			ERestriction newX = (ERestriction) X;
			Role r = newX.role;
			Basic B = newX.concept;
			if(!r.Relations.containsKey(A) || !r.Relations.get(A).contains(B))
			{
				toprocess = true;
				if(!B.subsumers.contains(bot))
					process_new_edge(A,r,B);
				else
					addSubsumer(A,bot);
			}
		}
		return toprocess;
	}

	private void addSubsumer(Basic A, Basic B) {
		// TODO Auto-generated method stub
		if(A.subsumers.contains(B))
			return;
		A.subsumers.add(B);
		Basic nB = B.complement;
		if(nB != null && A.complement != null)
		{
			Implies implies = (Implies) A.complement.entry;
			nB.queue.add(A.complement);
			nB.Ohat.add(implies);
		}
		if(!B.subsumers.contains(bot) && !A.subsumers.contains(nB))	// when A is a subconcept of bot, terminate the processing of Queue(A).
		{
			addAll2queue(A,B);			
			for(RoleConcept rc : A.LeftConnection)
			{
				Role r = rc.role;
				Basic Aprime = rc.concept;
				if(getexist(r,B) != null)
					addAll2queue(Aprime, getexist(r,B));
			}
		}
		else
		{
			A.subsumers.add(bot);
			if(A instanceof Singleton)
			{
				ontology.consistency = false;
				return;
			}
			for(RoleConcept rc : A.LeftConnection)
			{
				Basic Aprime = rc.concept;
				addSubsumer(Aprime, bot);
			}

		}
		CardinalityEntry[] AEntries = A.cardins;
		CardinalityEntry[] BEntries = B.cardins;

		// cardins
		if(AEntries != null && BEntries != null)
		{
			int Ahead = AEntries.length - 1;
			int Bhead = BEntries.length - 1;
			for(int i = Ahead - 1;i >= 0;i --)
			{
				int j = Bhead - 1;
				for(;j >= 0;j --)
				{
					if(BEntries[j].n > AEntries[i].n)
						break;
				}
				if(j + 1 < Bhead)
				{
					Basic An = (Basic) AEntries[i].basen;
					Basic Bn = (Basic) BEntries[j+1].basen;
					Implies implies = Bn.entry;
					An.Ohat.add(implies);
					An.queue.add(Bn);
				}
				Bhead = j+1;
			}
		}

		// to handle nominal
		if(B instanceof Singleton)
		{
			Singleton single = (Singleton) B;
			single.synonyms.add(A);
		}

	}

	private void process_new_edge(Basic A, Role r, Basic B) {
		// TODO Auto-generated method stub

		// for nominal
		B.ichain.add(A);
		B.hasModel = B.hasModel || A.hasModel;
		// end for nominal

		for(Role s : r.subsumers)
		{
			s.addrelation(A,B);
			B.addleftconnection(s, A);
			for(Basic Bprime: B.subsumers)
				if(getexist(s,Bprime) != null)
					addAll2queue(A,getexist(s,Bprime));
			for(RoleConcept rc: A.LeftConnection)
			{
				Role t = rc.role;
				Basic Aprime = rc.concept;
				HashSet<Role> set = t.RightComposition.get(s);
				if(set != null)
					for(Role u :set)
						if(!u.Relations.containsKey(Aprime) || !u.Relations.get(Aprime).contains(B))
							process_new_edge(Aprime,u,B);
			}
			for(Role t:s.RightComposition.keySet())
			{
				HashSet<Role> set = s.RightComposition.get(t);
				if(set != null)
				{
					for(Role u:set)
					{
						HashSet<Basic> bprimes = t.Relations.get(B);
						if(bprimes != null)
							for(Basic Bprime:t.Relations.get(B))
								if(!u.Relations.containsKey(A) || !u.Relations.get(A).contains(Bprime))
									process_new_edge(A,u,Bprime);
					}
				}
			}
		}
	}

	private boolean reach(Basic A, Basic B) {
		// TODO Auto-generated method stub
		if(B.ichain.contains(A))
			return true;
		for(Basic Bi:B.ichain)
			if(reach(A,Bi))
				return true;
		return false;
	}

	private void tBoxPostprocessing() {
		// TODO Auto-generated method stub
		// handle subsumers of top
		bot.equivalence.add(bot);
		Atomic Top = (Atomic) ontology.descriptions.get(1);
		for(Basic concept:ontology.allconcepts)
		{
			concept.equivalence.add(concept);
			if(concept.subsumers.contains(bot))
			{
				concept.equivalence.add(bot);
				bot.equivalence.add(concept);
			}
			else
				concept.subsumers.addAll(Top.subsumers);
		}


		// equivalence
		for(Basic concept:ontology.allconcepts)
		{
			for(Basic subsumer:concept.subsumers)
				if(subsumer.original && subsumer.subsumers.contains(concept))
					concept.equivalence.add(subsumer);
		}
		for(Role role:ontology.roles.values())
		{
			role.equivalence.add(role);
			for(Role subsumer:role.subsumers)
				if(subsumer.subsumers.contains(role))
					role.equivalence.add(subsumer);
		}
	}

	private void addAll2queue(Basic desc, Description entry)
	{
		desc.queue.add(entry);
	}


	private Some getexist(Role role, Basic concept) {
		// TODO Auto-generated method stub
		return role.somes.get(concept);
	}



}
