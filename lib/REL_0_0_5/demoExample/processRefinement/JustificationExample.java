package processRefinement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLSubClassAxiom;

import quentin.rel.util.Timer;
import uk.ac.abdn.REL.model.AxiomPool;
import uk.ac.abdn.REL.reasoner.RELReasoner;

public class JustificationExample {

	public static void main(String args[]) throws OWLOntologyCreationException, OWLOntologyChangeException, IOException
	{
		Timer time1 = new Timer("Parsing");
		time1.start();
		String f = "file:invalidProcessABC.owl";

		URI physicalURI = URI.create(f);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromPhysicalURI(physicalURI);
		time1.stop();
		System.out.println(time1.toString());
		
		
		Timer timeR = new Timer("REL");
		timeR.start();
		
		Timer time2 = new Timer("Approximation");
		time2.start();
		RELReasoner reasoner = new RELReasoner(manager);
		reasoner.loadOntology(ontology);
		time2.stop();
		System.out.println(time2.toString());
		
		
		Timer timerR = new Timer("Classification");
		timerR.start();
		reasoner.TBoxClassification();
		timerR.stop();	
		System.out.println(timerR);
		
		timeR.stop();
		System.out.println(timeR);

		String output = "RELClassificationResult.txt";
		FileWriter fw = new FileWriter(output);
		BufferedWriter bw = new BufferedWriter(fw);
		
		reasoner.getOntology().write(bw);
	
		bw.close();
		fw.close();
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		
		// for "invalidProcessABC.owl" ontology
		String prefix = "http://www.most.org/guidance/processrefinement.owl#";
		// query 1
		OWLClass Invalid = factory.getOWLClass( URI.create( prefix + "Invalid" ));
		
		Timer timerJ = new Timer("REL Justification");
		timerJ.start();
		boolean invalid = false;
		for(OWLClass activity:ontology.getReferencedClasses())
		{
			if(activity.equals(Invalid))
				continue;
			if(reasoner.getsubsumer(activity).contains(Invalid))
			{
				if(invalid == false)
					System.out.println("The refinement is INVALID!");
				invalid = true;
				OWLSubClassAxiom axiom = factory.getOWLSubClassAxiom(activity, Invalid);
				ArrayList<AxiomPool> justifications = reasoner.justify(ontology, axiom); 				
				int i = 1;
				System.out.println(activity+" is invalid because:");
				for(AxiomPool justification: justifications)
				{
					System.out.println("===================="+i+"====================");
				for(OWLAxiom ax: justification.axioms)
					System.out.println(ax);
				i++;
				}
			}
			
		}
		if(invalid == false)
			System.out.println("The refinement is VALID!");
		

	}
}

