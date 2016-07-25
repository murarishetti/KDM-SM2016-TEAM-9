package ontology;

import org.semanticweb.owlapi.model.IRI;

public class Preferences {
	public static final IRI DEFAULT_PIZZA_CLASS_IRI = IRI.create("https://www.kdm.com/OWL/pizza#Pizza");
	
	public static final IRI DEFAULT_PizzaTopping_Class_IRI = IRI.create("https://www.kdm.com/OWL/pizza#PizzaTopping");

	public static final IRI DEFAULT_PizzaBase_Class_IRI = IRI.create("https://www.kdm.com/OWL/pizza#PizzaBase");
	
	public static final IRI DEFAULT_hasVarieties_Property_IRI = IRI.create("https://www.kdm.com/OWL/pizza#hasVarieties");
	
	public static final IRI DEFAULT_MargheritaPizza_Class_IRI = IRI.create("https://www.kdm.com/OWL/pizza#MargheritaPizza");
	
	public IRI getPizzaClassName() {
        return DEFAULT_PIZZA_CLASS_IRI;
    }

	public IRI getPizzaToppingClassName() {
        return DEFAULT_PizzaTopping_Class_IRI;
    }
	
	public IRI getPizzaBaseClassName() {
        return DEFAULT_PizzaBase_Class_IRI;
    }
	
	public IRI gethasVarietiesProp() {
        return DEFAULT_hasVarieties_Property_IRI;
    }
	
	public IRI getMargheritaPizzaClassName(){
		return DEFAULT_MargheritaPizza_Class_IRI;
	}
	
}
