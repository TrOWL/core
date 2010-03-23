package physicalDevice;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDescription;
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
//		String f = "file:PD_Approach1_Q5_nodatatype.owl";
		String f = "file:sample.owl";

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
		
		if(!reasoner.getOntology().consistency)
			System.out.println("Ontology is inconsistent!");

		String output = "RELClassificationResult.txt";
		FileWriter fw = new FileWriter(output);
		BufferedWriter bw = new BufferedWriter(fw);
		
		reasoner.getOntology().write(bw);
	
		bw.close();
		fw.close();
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		
		// for "PD_Approach1_Q5_nodatatype.owl" ontology
		// query 1
//		String prefix = "http://www.comarch.com/oss/pd.owl#";
//		OWLClass Supervisor = factory.getOWLClass(URI.create(prefix+"Supervisors"));
//		OWLClass Test3 = factory.getOWLClass(URI.create(prefix+"Slot_Test_3"));
//		OWLSubClassAxiom axiom = factory.getOWLSubClassAxiom(Supervisor, Test3);
		
//		if(reasoner.entail(axiom))
//		{
//			Timer timerJ = new Timer("REL Justification");
//			timerJ.start();
//			ArrayList<AxiomPool> justifications = reasoner.justify(ontology, axiom); 
			// an additional argument can be specified to restrict the maximal number of justifications
////			ArrayList<Justification> justifications = reasoner.justify(ontology, axiom, 1); 
//			timerJ.stop();
//			System.out.println(timerJ);
//			System.out.println(axiom+" because:");
//			int i = 1;
//			for(AxiomPool justification: justifications)
//			{
//				System.out.println("===================="+i+"====================");
//			for(OWLAxiom ax: justification.axioms)
//				System.out.println(ax);
//			i++;
//			}
//		}
//		else
//			System.out.println("REL can not infer "+axiom);

		// for "PD_Approach1_Q5_nodatatype.owl" ontology or "sample" ontology
		// query 2
		Timer timerJ = new Timer("REL Justification");
		timerJ.start();
		if(!reasoner.getOntology().consistency)
		{
			ArrayList<AxiomPool> justifications = reasoner.inconsistencyJustification(ontology,1);
			// an additional argument can be specified to restrict the maximal number of justifications
//			ArrayList<AxiomPool> justifications = reasoner.inconsistencyJustification(ontology, 1);
			timerJ.stop();
			System.out.println(timerJ);
			System.out.println("Ontoloty is inconsistent because:");
			for(AxiomPool justification: justifications)
			{
				System.out.println("========================================");
				for(OWLAxiom ax: justification.axioms)
					System.out.println(ax);
				}
			}
		else
			System.out.println("REL believes the ontology is consistent");

	}
}

