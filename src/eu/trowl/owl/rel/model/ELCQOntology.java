package eu.trowl.owl.rel.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;


/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class ELCQOntology {
	
	public int classNum = 0;
	public int permanentclassNum = 0;
	public int roleNum = 0;
	public int individualNum = 0;
	
	public HashMap<OWLDescription, Integer> classID = new HashMap<OWLDescription, Integer>();
	public HashMap<Integer, Description> descriptions = new HashMap<Integer, Description>();

	public HashMap<OWLObjectPropertyExpression, Integer> roleID = new HashMap<OWLObjectPropertyExpression, Integer>();
	public HashMap<Integer, Role> roles = new HashMap<Integer, Role>();
	
	public HashMap<OWLIndividual, Integer> individualID = new HashMap<OWLIndividual, Integer>();
	public HashMap<Integer, Individual> individuals = new HashMap<Integer, Individual>();

	public HashMap<Role, HashMap<Basic, ERestriction>> simpleExistEntries = new HashMap<Role, HashMap<Basic, ERestriction>>();
	
	public HashSet<Basic> allconcepts = new HashSet<Basic>();
	
	public boolean consistency = true;
	
	public boolean classified = false;

	public void write(BufferedWriter bw) {
		// TODO Auto-generated method stub
		Atomic bot = (Atomic) descriptions.get(0);
		try {
			bw.write("classes as follows:\n");
			for(int i = 1;i<classNum;i++)
			{
				if(descriptions.get(i) instanceof Singleton)
					continue;
				Atomic concept = (Atomic) descriptions.get(i);
				if(!concept.original)
					continue;
				bw.write(i+" "+concept.uri.getFragment()+": S(");
				if(concept.subsumers.contains(bot))
				{
					bw.write("Nothing, ");
					for(Basic subsumer:allconcepts)
					{
						if(subsumer instanceof Atomic && subsumer.original)
						{
							bw.write(((Atomic)subsumer).uri.getFragment()+", ");
						}
					}
				}
				else
				{
				for(Basic subsumer:concept.subsumers)
				{
					if(subsumer instanceof Atomic && subsumer.original)
					{
						bw.write(((Atomic)subsumer).uri.getFragment()+", ");
					}
					
				}
				}
				bw.write(")\n");
			}
			bw.write("\n");
			bw.write("roles as follows:\n");
			for(int i = 0;i<roleNum;i++)
			{
				Role role = roles.get(i);
				if(!role.original)
					continue;
				bw.write(i+" "+role.uri.getFragment()+": S(");
				for(Role subsumer:role.subsumers)
				{
					if(subsumer.original)
					{
						bw.write(subsumer.uri.getFragment()+", ");
					}
				}
				bw.write(")\n"+" R(");
				for(Entry<Basic, HashSet<Basic>> relation:role.Relations.entrySet())
				{
					Basic classA = relation.getKey();
					if(!classA.original || classA instanceof Singleton)
						continue;
					for(Basic classB:relation.getValue())
					{
						if(classB.original && classB instanceof Atomic)
							bw.write("("+((Atomic)classA).uri.getFragment()+", "+((Atomic)classB).uri.getFragment()+"), ");
					}
				}
				bw.write(")\n");
			}
			bw.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public int countsubsumers() {
		// TODO Auto-generated method stub
		int num = 0;
		Atomic bot = (Atomic) descriptions.get(0);
		int allnum = 0;
		allnum = allconcepts.size()+1;

		int botnum = 0;
		for(int i = 1;i<classNum;i++)
		{
			Basic concept = (Basic) descriptions.get(i);
			if(!concept.original || concept instanceof Singleton)
				continue;
			if(concept.subsumers.contains(bot))
			{
				num+=allnum;
				botnum++;
				continue;
			}
			for(Basic subsumer:concept.subsumers)
			{
					if(subsumer.original && subsumer instanceof Atomic)
						num++;
			}
		}
		System.out.println(num);
		return num;
		
	}
	
	
	public void getunsatisfiableconcepts()
	{
		int num = 0;
		Atomic bot = (Atomic) descriptions.get(0);
		ArrayList<URI> unsatisfiables = new ArrayList<URI>();
		for(int i = 2;i<classNum;i++)
		{
			Atomic concept = (Atomic) descriptions.get(i);
			if(!concept.original)
				continue;
			if(concept.subsumers.contains(bot))
			{
				num++;
				unsatisfiables.add(concept.uri);
			}
	}
		System.out.println("There are "+num+" unsatisfiable concepts");
		for(URI uri:unsatisfiables)
			System.out.println(uri.getFragment());
	}

}
