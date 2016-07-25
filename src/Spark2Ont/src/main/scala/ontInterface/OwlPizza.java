package ontInterface;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class OwlPizza {
    OWLOntology ont;
    PrefixManager pm;
    OWLOntologyManager manager;
    OWLDataFactory df;

    public OwlPizza() {
        try {
            pm = new DefaultPrefixManager(null, null, "https://www.kdm.com/OWL/Phone#");
            manager = OWLManager.createOWLOntologyManager();
            df = manager.getOWLDataFactory();
            ont = initialzation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createSubClass(String className, String subClassName) {
        OWLClass baseClass = df.getOWLClass(className, pm);
        OWLClass subClass = df.getOWLClass(subClassName, pm);
        OWLSubClassOfAxiom declarationSubClassAxiom = df.getOWLSubClassOfAxiom(subClass, baseClass);
        manager.addAxiom(ont, declarationSubClassAxiom);
    }

    public void createClass(String className) {

        OWLClass classN = df.getOWLClass(className, pm);
        OWLDeclarationAxiom declarationAxiom = df.getOWLDeclarationAxiom(classN);
        manager.addAxiom(ont, declarationAxiom);

    }

    public void createIndividual(String individualName, String className) {
        OWLClass classN = df.getOWLClass(className, pm);
        OWLNamedIndividual ind = df.getOWLNamedIndividual(individualName, pm);
        OWLClassAssertionAxiom classAssertion = df.getOWLClassAssertionAxiom(classN, ind);
        manager.addAxiom(ont, classAssertion);

    }

    public void createObjectProperty(String domain, String propertyName, String range) {

        OWLClass domainC = df.getOWLClass(domain, pm);
        OWLClass rangeC = df.getOWLClass(range, pm);
        OWLObjectProperty isIngredientOf = df.getOWLObjectProperty(propertyName, pm);
        OWLObjectPropertyRangeAxiom rangeAxiom = df.getOWLObjectPropertyRangeAxiom(isIngredientOf, rangeC);
        OWLObjectPropertyDomainAxiom domainAxiom = df.getOWLObjectPropertyDomainAxiom(isIngredientOf, domainC);
        manager.addAxiom(ont, rangeAxiom);
        manager.addAxiom(ont, domainAxiom);

    }

    public OWLOntology initialzation() throws Exception {
        //creating ontology manager
        //In order to create objects that represent entities we need a

        ont = manager.createOntology(IRI.create("https://www.kdm.com/OWL/", "S7"));
        OWLClass Pizza = df.getOWLClass(":S7", pm);
        OWLClass PizzaTopping = df.getOWLClass(":Design", pm);
        OWLClass PizzaBase = df.getOWLClass(":Features", pm);

        OWLDeclarationAxiom declarationAxiomPizza = df.getOWLDeclarationAxiom(Pizza);
        OWLDeclarationAxiom declarationAxiomPizzaTopping = df.getOWLDeclarationAxiom(PizzaTopping);
        OWLDeclarationAxiom declarationAxiomPizzaBase = df.getOWLDeclarationAxiom(PizzaBase);

        manager.addAxiom(ont, declarationAxiomPizza);
        manager.addAxiom(ont, declarationAxiomPizzaTopping);
        manager.addAxiom(ont, declarationAxiomPizzaBase);

        //Making all classes Disjoint to each other
        OWLDisjointClassesAxiom disjointClassesAxiom = df.getOWLDisjointClassesAxiom(Pizza, PizzaTopping, PizzaBase);
        manager.addAxiom(ont, disjointClassesAxiom);

        //Creating Subclasses for PizzaBase class
        OWLClass ThinAndCrisyBase = df.getOWLClass(":Memory", pm);
        OWLClass DeepPanBase = df.getOWLClass(":Camera", pm);
        OWLSubClassOfAxiom declarationAxiomThinAndCrisyBase = df.getOWLSubClassOfAxiom(ThinAndCrisyBase, PizzaBase);
        OWLSubClassOfAxiom declarationAxiomDeepPanBase = df.getOWLSubClassOfAxiom(DeepPanBase, PizzaBase);
        manager.addAxiom(ont, declarationAxiomThinAndCrisyBase);
        manager.addAxiom(ont, declarationAxiomDeepPanBase);

        //Creating Subclasses for PizzaTopping class
        OWLClass MeatTopping = df.getOWLClass(":Date", pm);
        OWLClass VegetableTopping = df.getOWLClass(":Size", pm);
        OWLClass CheeseTopping = df.getOWLClass(":Processor", pm);
        OWLClass SeafoodTopping = df.getOWLClass(":Price", pm);
        OWLSubClassOfAxiom declarationAxiomMeatTopping = df.getOWLSubClassOfAxiom(MeatTopping, PizzaTopping);
        OWLSubClassOfAxiom declarationAxiomVegetableTopping = df.getOWLSubClassOfAxiom(VegetableTopping, PizzaTopping);
        OWLSubClassOfAxiom declarationAxiomCheeseTopping = df.getOWLSubClassOfAxiom(CheeseTopping, PizzaTopping);
        OWLSubClassOfAxiom declarationAxiomSeafoodTopping = df.getOWLSubClassOfAxiom(SeafoodTopping, PizzaTopping);
        manager.addAxiom(ont, declarationAxiomMeatTopping);
        manager.addAxiom(ont, declarationAxiomVegetableTopping);
        manager.addAxiom(ont, declarationAxiomCheeseTopping);
        manager.addAxiom(ont, declarationAxiomSeafoodTopping);


        //Creating Class Country and Individuals to classes
        OWLClass Country = df.getOWLClass(":Country", pm);
        OWLDeclarationAxiom declarationAxiomCountry = df.getOWLDeclarationAxiom(Country);
        OWLNamedIndividual India = df.getOWLNamedIndividual(":India", pm);
        OWLNamedIndividual USA = df.getOWLNamedIndividual(":USA", pm);
        OWLNamedIndividual UK = df.getOWLNamedIndividual(":UK", pm);
        //Class Assertion specifying India is member of Class Country
        OWLClassAssertionAxiom classAssertionIndia = df.getOWLClassAssertionAxiom(Country, India);
        OWLClassAssertionAxiom classAssertionUSA = df.getOWLClassAssertionAxiom(Country, USA);
        OWLClassAssertionAxiom classAssertionUK = df.getOWLClassAssertionAxiom(Country, UK);
        manager.addAxiom(ont, declarationAxiomCountry);
        manager.addAxiom(ont, classAssertionIndia);
        manager.addAxiom(ont, classAssertionUSA);
        manager.addAxiom(ont, classAssertionUK);

        //Creating Food class
        OWLClass Food = df.getOWLClass(":Phone", pm);
        OWLDeclarationAxiom declarationAxiomFood = df.getOWLDeclarationAxiom(Food);
        manager.addAxiom(ont, declarationAxiomFood);

        //Creating Object Properties
        OWLObjectProperty isIngredientOf = df.getOWLObjectProperty(":isPriceOf", pm);
        OWLObjectPropertyRangeAxiom rangeAxiomisIngredientOf = df.getOWLObjectPropertyRangeAxiom(isIngredientOf, Food);
        OWLObjectPropertyDomainAxiom domainAxiomisIngredientOf = df.getOWLObjectPropertyDomainAxiom(isIngredientOf, Food);
        manager.addAxiom(ont, rangeAxiomisIngredientOf);
        manager.addAxiom(ont, domainAxiomisIngredientOf);

        OWLObjectProperty hasIngredient = df.getOWLObjectProperty(":hasDate", pm);
        OWLObjectPropertyRangeAxiom rangeAxiomhasIngredient = df.getOWLObjectPropertyRangeAxiom(hasIngredient, Food);
        OWLObjectPropertyDomainAxiom domainAxiomhasIngredient = df.getOWLObjectPropertyDomainAxiom(hasIngredient, Food);
        manager.addAxiom(ont, rangeAxiomhasIngredient);
        manager.addAxiom(ont, domainAxiomhasIngredient);

        //Making isIngredientOf and hasIngredient inverse properties
        manager.addAxiom(ont, df.getOWLInverseObjectPropertiesAxiom(isIngredientOf, hasIngredient));

        //Creating hasTopping, hasBase Properties
        OWLObjectProperty hasTopping = df.getOWLObjectProperty(":hasMemory", pm);
        OWLObjectPropertyDomainAxiom domainAxiomhasTopping = df.getOWLObjectPropertyDomainAxiom(hasTopping, Pizza);
        OWLObjectPropertyRangeAxiom rangeAxiomhasTopping = df.getOWLObjectPropertyRangeAxiom(hasTopping, PizzaTopping);
        manager.addAxiom(ont, rangeAxiomhasTopping);
        manager.addAxiom(ont, domainAxiomhasTopping);
        manager.addAxiom(ont, df.getOWLSubObjectPropertyOfAxiom(hasTopping, hasIngredient));

        OWLObjectProperty hasBase = df.getOWLObjectProperty(":hasSize", pm);
        OWLObjectPropertyDomainAxiom domainAxiomhasBase = df.getOWLObjectPropertyDomainAxiom(hasBase, Pizza);
        OWLObjectPropertyRangeAxiom rangeAxiomhasBase = df.getOWLObjectPropertyRangeAxiom(hasBase, PizzaBase);
        manager.addAxiom(ont, rangeAxiomhasBase);
        manager.addAxiom(ont, domainAxiomhasBase);
        manager.addAxiom(ont, df.getOWLSubObjectPropertyOfAxiom(hasBase, hasIngredient));

        //Making hasBase property as Functional
        manager.addAxiom(ont, df.getOWLFunctionalObjectPropertyAxiom(hasBase));

        //Making hasBase property as Transitive
        manager.addAxiom(ont, df.getOWLTransitiveObjectPropertyAxiom(hasIngredient));

        //Creating isToppingOf, isBaseOf Properties
        OWLObjectProperty isToppingOf = df.getOWLObjectProperty(":isSizeOf", pm);
        OWLObjectPropertyDomainAxiom domainAxiomisToppingOf = df.getOWLObjectPropertyDomainAxiom(isToppingOf, PizzaTopping);
        OWLObjectPropertyRangeAxiom rangeAxiomisToppingOf = df.getOWLObjectPropertyRangeAxiom(isToppingOf, Pizza);
        manager.addAxiom(ont, domainAxiomisToppingOf);
        manager.addAxiom(ont, rangeAxiomisToppingOf);
        manager.addAxiom(ont, df.getOWLSubObjectPropertyOfAxiom(isToppingOf, isIngredientOf));

        OWLObjectProperty isBaseOf = df.getOWLObjectProperty(":isDateOf", pm);
        OWLObjectPropertyDomainAxiom domainAxiomisBaseOf = df.getOWLObjectPropertyDomainAxiom(isBaseOf, PizzaBase);
        OWLObjectPropertyRangeAxiom rangeAxiomisBaseOf = df.getOWLObjectPropertyRangeAxiom(isBaseOf, Pizza);
        manager.addAxiom(ont, domainAxiomisBaseOf);
        manager.addAxiom(ont, rangeAxiomisBaseOf);
        manager.addAxiom(ont, df.getOWLSubObjectPropertyOfAxiom(isBaseOf, isIngredientOf));


        //Making isToppingOf and hasTopping inverse properties
        manager.addAxiom(ont, df.getOWLInverseObjectPropertiesAxiom(isToppingOf, hasTopping));

        //Making isBaseOf and hasBase inverse properties
        manager.addAxiom(ont, df.getOWLInverseObjectPropertiesAxiom(isBaseOf, hasBase));


        //Creating Data property
        OWLDataProperty hasVarieties = df.getOWLDataProperty(":hasVariants", pm);
        OWLDatatype integerDatatype = df.getIntegerOWLDatatype();
        OWLDataPropertyDomainAxiom domainAxiomhasVarieties = df.getOWLDataPropertyDomainAxiom(hasVarieties, Country);
        OWLDataPropertyRangeAxiom rangeAxiomhasVarieties = df.getOWLDataPropertyRangeAxiom(hasVarieties, integerDatatype);
        manager.addAxiom(ont, domainAxiomhasVarieties);
        manager.addAxiom(ont, rangeAxiomhasVarieties);

        //Some values from Restriction
        OWLClassExpression hasBaseRestriction = df.getOWLObjectSomeValuesFrom(hasBase, PizzaBase);
        OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(Pizza, hasBaseRestriction);
        manager.addAxiom(ont, ax);

        //Creating different kind of pizzas
        OWLClass NamedPizza = df.getOWLClass(":White", pm);
        OWLSubClassOfAxiom declarationAxiomNamedPizza = df.getOWLSubClassOfAxiom(NamedPizza, Pizza);
        manager.addAxiom(ont, declarationAxiomNamedPizza);

        OWLClass MargheritaPizza = df.getOWLClass(":Black", pm);
        OWLSubClassOfAxiom declarationAxiomMargheritaPizza = df.getOWLSubClassOfAxiom(MargheritaPizza, NamedPizza);
        manager.addAxiom(ont, declarationAxiomMargheritaPizza);

        OWLAnnotation commentAnno = df.getRDFSComment(df.getOWLLiteral("Samsung will sell unlocked versions of its flagship Galaxy S7 phones in the U.S. ", "en"));
        OWLAxiom commentMargheritaPizza = df.getOWLAnnotationAssertionAxiom(MargheritaPizza.getIRI(), commentAnno);
        manager.addAxiom(ont, commentMargheritaPizza);

        return ont;
    }

    public void saveOntology() {
        try {
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OutputStream os = new FileOutputStream("data/news.owl");
            OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
            manager.saveOntology(ont, owlxmlFormat, os);
            System.out.println("Ontology Created");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
