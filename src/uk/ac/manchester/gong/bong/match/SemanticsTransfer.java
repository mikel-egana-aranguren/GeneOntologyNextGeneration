/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The SemanticsTransfer.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The SemanticsTransfer.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */

package uk.ac.manchester.gong.bong.match;

import java.util.HashMap;
import java.util.HashSet;

import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLObjectIntersectionOf;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectSomeRestriction;

import uk.ac.manchester.cs.owl.OWLClassImpl;
import uk.ac.manchester.cs.owl.OWLObjectIntersectionOfImpl;
import uk.ac.manchester.cs.owl.OWLObjectSomeRestrictionImpl;
import uk.ac.manchester.gong.bong.owl.OWLDealer;
import uk.ac.manchester.gong.bong.settings.BONGSettings;

public class SemanticsTransfer {

	/**
	 * This class gets a matchresult and transfers the semantics
	 * from the regexp class to the target class, checking the 
	 * mappings or mere existence of the filler
	 * @throws OWLException  
	 */

	public static void doTransfer (OWLClass matchedClass, BONGMatchResult matchresult, BONGSettings bongsettings, OWLDealer owldealer, HashMap accesory_map) throws OWLException {
		
		OWLClass regexpclass = matchresult.getRegexpOWLClass(); 

		for(OWLDescription owldescription : regexpclass.getEquivalentClasses(owldealer.getOntology())){
			// The equivalent class is an intersection
			if(owldescription.getClass().getSimpleName().equals("OWLObjectIntersectionOfImpl")){
				// Create the hashset for the conditions, it will be then converted to an intersection
				// and added to the matched class
				HashSet equivset = new HashSet ();
				
				for(OWLDescription op : ((OWLObjectIntersectionOfImpl)owldescription).getOperands()){
					
					// The condition is a class
					if(op.getClass().getSimpleName().equals("OWLClassImpl")){
						equivset.add(op);
					}
					
					// The condition is an existential restriction
					if(op.getClass().getSimpleName().equals("OWLObjectSomeRestrictionImpl")){

						OWLObjectProperty prop =  (OWLObjectProperty) ((OWLObjectSomeRestrictionImpl)op).getProperty();
						String filler = ((OWLClassImpl) ((OWLObjectSomeRestrictionImpl)op).getFiller()).getURI().getFragment();
						if(filler.startsWith("RegexpMatchGroup")){
							String group_number = filler.substring(filler.length()-1, filler.length());
							String unmapped_filler = matchresult.getMatcher().group(Integer.parseInt(group_number));
							
							OWLClass accesory_map_filler = (OWLClass) accesory_map.get(unmapped_filler);
							if(accesory_map_filler !=null){
								System.out.println("[BONG] Mapped term found in accesory ontology: " +unmapped_filler + " maps to "+ accesory_map_filler);
								OWLObjectSomeRestriction new_restriction = owldealer.getFactory().getOWLObjectSomeRestriction(prop, owldealer.getClass(accesory_map_filler.getURI()));
								equivset.add(new_restriction);
							}
							else{
								String mapped_filler = (String) (bongsettings.getMappings()).get(unmapped_filler);
								if(mapped_filler!=null){
									System.out.println("[BONG] Mapped term found with the BONG mapping: " +unmapped_filler + " maps to "+ mapped_filler);
									OWLObjectSomeRestriction new_restriction = owldealer.getFactory().getOWLObjectSomeRestriction(prop, owldealer.getClass(mapped_filler));
									equivset.add(new_restriction);
								}
							}
						}
					}
					// TODO: it could be that conditions are universal restrictions, restrictions with intersection as filler, ... 
					
					// Add the axioms to the matched class only if the intersection has more 
					// than one element, otherwise we get loops ...
					if(equivset.size()>1){
						System.out.println("[BONG] Adding neccesary and sufficient conditions: " + equivset);
						OWLObjectIntersectionOf intersection = owldealer.getFactory().getOWLIntersectionOf(equivset);
						HashSet matchedclassaxioms = new HashSet();
						matchedclassaxioms.add(matchedClass);
						matchedclassaxioms.add(intersection);
						OWLEquivalentClassesAxiom equivaxiom = owldealer.getFactory().getOWLEquivalentClassesAxiom(matchedclassaxioms);
						AddAxiom addaxiom = new AddAxiom (owldealer.getOntology(bongsettings.getCentralontologyURI()),equivaxiom);
						owldealer.getManager().applyChange(addaxiom);
					}
				}
			}
		}	
	}
}
