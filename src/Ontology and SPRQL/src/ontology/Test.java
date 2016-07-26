package ontology;

public class Test {
	public static void main(String args[]){
		PizzaOntology pizzaOntology = new PizzaOntology();
		pizzaOntology.getPizzaBaseList();
		pizzaOntology.getPizzaToppingList();
		pizzaOntology.getPizzaList();
		pizzaOntology.getPropertyDomain();
		pizzaOntology.getSuperClass();
		pizzaOntology.checkConsistent();
	}
}	
