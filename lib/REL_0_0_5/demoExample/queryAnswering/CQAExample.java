package queryAnswering;

import java.io.File;
import java.net.URI;
import java.util.HashSet;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.OWLOntologyMerger;

import uk.ac.abdn.REL.CQA.model.BinaryRole;
import uk.ac.abdn.REL.CQA.model.Query;
import uk.ac.abdn.REL.CQA.model.Solution;
import uk.ac.abdn.REL.reasoner.RELReasoner;


public class CQAExample {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws OWLOntologyChangeException 
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyChangeException, CloneNotSupportedException {
		// TODO Auto-generated method stub

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		manager.loadOntologyFromPhysicalURI(URI.create("http://www.abdn.ac.uk/~csc303/ontology/lubm-elp.owl"));
		
		String dir = "E:\\EclipseWorkspace\\REL_0_0_5\\";
		String dir2 = "E:/EclipseWorkspace/REL_0_0_5/";
		int nUniv = 1;
		int count = 0;
		
		for (int univ = 0; univ < nUniv; univ++) {
			for (int dept = 0; dept < Integer.MAX_VALUE; dept++) {
				String filename = "University" + univ + "_" + dept + ".owl";

				if (!(new File(dir + filename).exists()))
					break;
				
				manager.loadOntologyFromPhysicalURI(URI.create("file:"+dir2+filename));

				count++;
//				break;
			}
//			break;
		}
		
		OWLOntologyMerger merger = new OWLOntologyMerger(manager);
		URI uri = URI.create("http://www.abdn.ac.uk/~yren/elpluscqa.owl");
		OWLOntology ontology = merger.createMergedOntology(manager, uri);

		RELReasoner reasoner = new RELReasoner(manager);
		reasoner.generatePseudoModel(ontology);
		
		Query test = new Query();
		String base = "http://www.abdn.ac.uk/~csc303/ontology/lubm-elp.owl#";
		test.addVariable("?x");
//		test.addVariable("?y");
		test.addConceptQuery(base+"ResearchAssistant", "?x");
		test.addRoleQuery(base+"worksFor", "?x", "?y");
		test.addRoleQuery(base+"researchProject", "?y", "?z");
		
		HashSet<Solution> solutions = reasoner.CQA(test);
		for(Solution solution:solutions)
		{
			System.out.println("===================================");
			for(BinaryRole binding:solution.binding)
			{
				System.out.println(binding.alpha+":"+binding.beta);
			}
		}
		System.out.println("===================================");
		System.out.println("There are "+solutions.size()+" solutions.");
	}

}
