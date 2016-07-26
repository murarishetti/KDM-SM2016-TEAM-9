package ontology;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import uk.ac.manchester.cs.jfact.JFactFactory;

public class PizzaOntology {
	private  OWLOntology ontology;
	private  OWLReasoner reasoner;
	private  OWLOntologyManager manager;
	private  OWLDataFactory df;
	public  Preferences PREFERENCES = null;
	private String IRI = "https://www.kdm.com/OWL/pizza#";
	public PizzaOntology(){
		 loadOntology();
	     setupReasoner();
	}
	
	public void loadOntology(){
		manager = OWLManager.createOWLOntologyManager();
        df = manager.getOWLDataFactory();
        PREFERENCES = new Preferences();
		File file = new File("Data/OwlPizza.owl");
		try {
			ontology = manager.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  
	public void setupReasoner(){
	    try {
            JFactFactory factory = new JFactFactory();
            reasoner = factory.createReasoner(ontology);
        } catch (Exception e) {
           e.printStackTrace();
        }
	}
	
	 public void getPizzaToppingList(){
		 OWLClass PizzaToppingCls = df.getOWLClass(PREFERENCES.getPizzaToppingClassName());
	        Collection<OWLClass> c = reasoner.getSubClasses(PizzaToppingCls, true).getFlattened();
	        int counter=0;
	        System.out.println("Subclasses in Pizza Topping");
			for(Iterator it = c.iterator(); it.hasNext(); counter++) {
				OWLClass cls = (OWLClass) it.next();
				System.out.println(cls.toString());
			}
			System.out.println("---------------------------------");
	 }
	 
	 public void getPizzaBaseList(){
		 OWLClass PizzaBaseCls = df.getOWLClass(PREFERENCES.getPizzaBaseClassName());
	        Collection<OWLClass> c = reasoner.getSubClasses(PizzaBaseCls, true).getFlattened();
	        int counter=0;
	        System.out.println("Subclasses in Pizza Base");
			for(Iterator it = c.iterator(); it.hasNext(); counter++) {
				OWLClass cls = (OWLClass) it.next();
				System.out.println(cls.toString());
			}
			System.out.println("---------------------------------");
	 }
	 public void getPizzaList(){
		 OWLClass PizzaCls = df.getOWLClass(PREFERENCES.getPizzaClassName());
	        Collection<OWLClass> c = reasoner.getSubClasses(PizzaCls, false).getFlattened();
	        int counter=0;
			for(Iterator it = c.iterator(); it.hasNext(); counter++) {	
				OWLClass cls = (OWLClass) it.next();
				System.out.println(cls.toString());
			}
			System.out.println("---------------------------------");
	 }
	 
	 public void getPropertyDomain(){
		 OWLDataProperty hasVarieties = df.getOWLDataProperty(PREFERENCES.gethasVarietiesProp());
		 Collection<OWLClass> c = reasoner.getDataPropertyDomains(hasVarieties, true).getFlattened();		 
		 System.out.println("hasVarieties Property Domain Name");
		 int counter=0;
			for(Iterator it = c.iterator(); it.hasNext(); counter++) {	
				OWLClass cls = (OWLClass) it.next();
			System.out.println(cls.toString());
		}
		 System.out.println("---------------------------------");
	 }
	 
	 public void getSuperClass(){
		 OWLClass MargheritaClass = df.getOWLClass(PREFERENCES.getMargheritaPizzaClassName());
		  Collection<OWLClass> MargheritaSuperClasses =  reasoner.getSuperClasses(MargheritaClass, true).getFlattened();
		  int counter = 0;
		  System.out.println("Margherita Super Classes");
		  for(Iterator it = MargheritaSuperClasses.iterator(); it.hasNext(); counter++) {
				OWLClass cls = (OWLClass) it.next();
				System.out.println(cls.toString());
			}
			System.out.println("---------------------------------");
	 }

	 
	 public void checkConsistent(){
		 System.out.print("Is Ontology Consistent? ");
		 System.out.println(reasoner.isConsistent());
	 }
	 
	 
	 
}
