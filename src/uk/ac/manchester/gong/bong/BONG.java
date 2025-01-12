/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The BONG.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The BONG.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */

package uk.ac.manchester.gong.bong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.util.AssertedClassHierarchyReasoner;

import uk.ac.manchester.cs.owl.OWLSubClassAxiomImpl;
import uk.ac.manchester.gong.bong.match.BONGMatchResult;
import uk.ac.manchester.gong.bong.match.RegexpMatcher;
import uk.ac.manchester.gong.bong.match.SemanticsTransfer;
import uk.ac.manchester.gong.bong.owl.OWLDealer;
import uk.ac.manchester.gong.bong.reasoner.ReasonerConnector;
import uk.ac.manchester.gong.bong.settings.BONGSettings;
import uk.ac.manchester.gong.bong.settings.BONGSettingsReader;

/**
 * This class does all the job, calls the rest.
 *
 */

public class BONG {

	public static void work(String dbtablename,String centralURL, String centralFILE, HashMap files){
		
		// ----------------- Create owldealer
		System.out.println("[BONG] Loading ontology and creating owldealer.");
		System.out.println("");
		OWLDealer owldealer = null;
		try {
			owldealer = new OWLDealer (centralURL, centralFILE, files);
		} 
		catch (OWLException e) {e.printStackTrace();}

		// -----------------  Settings checker
		// Regexp syntax (Compile)
		// Uris; class exists, ontology uri = uri
		// Classes exist: Regexp1, CentralOntology, RootTerm, ...
		
		// -----------------  Read the settings of the BONG ontology
		System.out.println(" ");
		System.out.println("[BONG] Reading the settings on the BONG ontology.");
		BONGSettings bongsettings = BONGSettingsReader.readSettings(owldealer);

		HashMap accesory_map = null;
		try {System.out.println("[BONG] Creating label2names map of the accesory ontology.");
			accesory_map = owldealer.generateLabel2Name(bongsettings.getAccesoryOntologyURI());
		} catch (OWLException e1) {
			e1.printStackTrace();
		}
		
		// ----------------- Generate the subtree and execute the regexp matching and
		// ----------------- the semantic transfer for each term 
		System.out.println("");
		System.out.println("[BONG] ::::::::::::::::::::::::::::::::::::::");
		System.out.println("[BONG] ::::::::::::::::::::::::::::::::::::::");
		// Create the array for storing the dissected classes
		ArrayList dissected_classes = new ArrayList ();
		try {
			AssertedClassHierarchyReasoner reasoner = new AssertedClassHierarchyReasoner(owldealer.getManager()); 
			reasoner.setOntology(owldealer.getOntology(bongsettings.getCentralontologyURI()));
			OWLClass rootClass = owldealer.getClass(bongsettings.getRootTermURI());
			dissected_classes = getChildren(rootClass,reasoner,owldealer,bongsettings,dissected_classes,accesory_map);	
		} 
		catch (OWLException e) {e.printStackTrace();} 

		// ----------------- Write Ontology
		try {
			System.out.println("");
			System.out.println("[BONG] ::::::::::::::::::::::::::::::::::::::");
			System.out.println("[BONG] ::::::::::::::::::::::::::::::::::::::");
			System.out.println("");
			System.out.println("[BONG] Writting new ontology to disk.");
			owldealer.writeOntology("NewCentralOntology.owl",bongsettings.getCentralontologyURI());
		} 
		catch (IOException e) {e.printStackTrace();} 
		catch (OWLException e) {e.printStackTrace();}
		
		// ----------------- Reasoning
		ReasonerConnector.connect(owldealer,bongsettings, dissected_classes, dbtablename);
	}
	public static ArrayList getChildren (OWLClass root, AssertedClassHierarchyReasoner reasoner, OWLDealer owldealer, BONGSettings bongsettings, ArrayList dissected_classes, HashMap accesory_map){
		try {
			// complete_result arraylist for adding children of is_a (subclasses) and part_of (fillers)
			ArrayList complete_result = new ArrayList();
			// get central ontology
			OWLOntology centralontology = owldealer.getOntology(bongsettings.getCentralontologyURI());
			// get part_of (or any other) fillers
			for(OWLAxiom owlaxiom : centralontology.getReferencingAxioms((OWLEntity)root)){
				if(owlaxiom.getClass().getSimpleName().equals("OWLSubClassAxiomImpl")){
					OWLSubClassAxiomImpl restrictionsubclassaxiom = (OWLSubClassAxiomImpl)owlaxiom;
					if(restrictionsubclassaxiom.getSuperClass().isAnonymous()){
						String subclassoforigin= (restrictionsubclassaxiom.toString().split("ObjectSome")[0]);
						String originclassname=subclassoforigin.substring(11,subclassoforigin.length()).trim();
						if(!originclassname.equals(root.getURI().getFragment())){
							complete_result.add((OWLClass)owldealer.getClass(bongsettings.getCentralontologyURI() + "#" + originclassname));
						}
					}
				}
			}
			
			// Get subclasses
			for(Set childset : reasoner.getSubClasses(root)){
				complete_result.add((OWLClass)childset.toArray()[0]);
			}
			
			// Do matching and semantic transfer for each, execute getChildren again recursively
			Iterator complete_result_iterator = complete_result.iterator();
			while (complete_result_iterator.hasNext()){
				OWLClass child = (OWLClass)complete_result_iterator.next();
				System.out.println(" ");
				System.out.println("[BONG] ::::::::::::::::::::::::::::::::::::::");
				System.out.println("[BONG] Processing class: " + child);
				BONGMatchResult matchresult = RegexpMatcher.match(child, owldealer,bongsettings);
				if(matchresult.getRegexpOWLClass() != null) {
					System.out.println("[BONG] Matching regexp: " + matchresult.getRegexpOWLClass());
					System.out.println("[BONG] Matching group 1: " + matchresult.getMatcher().group(1));
					// ----------------- Semantic transfer to matched class	
					SemanticsTransfer.doTransfer(child, matchresult, bongsettings, owldealer, accesory_map);
					dissected_classes.add(child);
				}
				else{
					System.out.println("[BONG] Not matched");
				}
				dissected_classes = getChildren(child,reasoner,owldealer,bongsettings,dissected_classes,accesory_map);
			}
		} 			
		catch (OWLException e) {e.printStackTrace();}
		return dissected_classes;
	}
}
