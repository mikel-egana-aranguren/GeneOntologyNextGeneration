/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The Reasoner.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The Reasoner.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */

package uk.ac.manchester.gong.bong.reasoner;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLClass;

import uk.ac.manchester.cs.owl.inference.dig11.DIGReasoner;
import uk.ac.manchester.gong.bong.hypersonic.SQLDealer;
import uk.ac.manchester.gong.bong.owl.OWLDealer;
import uk.ac.manchester.gong.bong.settings.BONGSettings;

/**
 * This deals with the reasoner, it does the inference, checks the interesting 
 * relationships and puts them in the database.
 *
 */

public class ReasonerConnector {

	public static void connect (OWLDealer owldealer, BONGSettings bongsettings, ArrayList dissected_classes, String dbtablename) {
		try {
			// Send to the reasoner only the classes that have ben modified 
			// (the classes that have matched a regexp)
			System.out.println("[BONG] Connecting to reasoner.");
			DIGReasoner digreasoner = new DIGReasoner (owldealer.getManager());
			OWLOntology centralOntology = owldealer.getManager().getOntology(URI.create(bongsettings.getCentralontologyURI()));
			digreasoner.setOntology(centralOntology);
			System.out.println("[BONG] Opening database.");
			
			SQLDealer sqldealer = new SQLDealer (dbtablename);
			sqldealer.connect();
			sqldealer.createTable();
			
			int sqlindex= 0;
			Iterator dissectedIterator = dissected_classes.iterator();
			while(dissectedIterator.hasNext()){
				OWLClass clazz = (OWLClass)dissectedIterator.next();
				HashSet inferredsuperClasses = (HashSet) digreasoner.getSuperClasses(clazz);
				HashSet assertedSuperClasses = (HashSet) clazz.getSuperClasses(owldealer.getOntology(bongsettings.getCentralontologyURI()));
				ArrayList reasoning_results = compareSets (inferredsuperClasses,assertedSuperClasses);
				if(!reasoning_results.isEmpty()){
					Iterator parentsIterator = reasoning_results.iterator();
					while(parentsIterator.hasNext()){
						OWLClass parent = (OWLClass)parentsIterator.next();
						System.out.println("[BONG] New relationship: " + clazz + " is_a " + parent);
						sqlindex = sqlindex + 1;
//						System.out.println(sqlindex + clazz.getURI().getFragment() + "is_a" + parent.getURI().getFragment());
						sqldealer.injectResult(sqlindex, clazz.getURI().getFragment(), "is_a", parent.getURI().getFragment());
					}
				}
			}
			sqldealer.disconnect();
			System.out.println("[BONG] Reasoning finnished, all the results in the database.");
		} 
		catch (Exception e) {e.printStackTrace();}
	}
	
	static ArrayList compareSets (HashSet inferredsuperClasses, HashSet assertedSuperClasses){
		ArrayList result = new ArrayList ();
		Iterator inferredsuperClassesIterator = inferredsuperClasses.iterator();
		while(inferredsuperClassesIterator.hasNext()){
			HashSet inferredParents = (HashSet)inferredsuperClassesIterator.next();
			Iterator inferredParentsIterator = inferredParents.iterator();
			while(inferredParentsIterator.hasNext()){
				OWLClass inferredParent = (OWLClass)inferredParentsIterator.next();
				if(!assertedSuperClasses.contains(inferredParent)){
					result.add(inferredParent);
				}
			}
		}
		return result;
	}
}
