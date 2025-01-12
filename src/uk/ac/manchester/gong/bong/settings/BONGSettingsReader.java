/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The SettingsReader.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The SettingsReader.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */

package uk.ac.manchester.gong.bong.settings;

import java.util.HashMap;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;

import uk.ac.manchester.gong.bong.owl.OWLDealer;

/**
 * This class reads most of the settings (except regexps) from
 * the BONG ontology and creates a BONGSettings object. Regexps
 * are directly read from the BONG ontology, as they have some 
 * semantics to be used
 */

public class BONGSettingsReader {

	public static BONGSettings readSettings (OWLDealer owldealer)  {
		BONGSettings bongsettings = null;
		HashMap mappings = new HashMap();
		OWLOntology bongOntology = owldealer.getOntology();
		// Get the mappings
		BONGOntologyClassIterator iterator = new BONGOntologyClassIterator (bongOntology, "Mapping");
		while(iterator.hasNext()){
				OWLClass mapping = (OWLClass)iterator.next();
				String mapsfromresult = owldealer.getAnnotation ("MapsFrom", mapping, bongOntology);
				String mapstoresult = owldealer.getAnnotation ("MapsTo", mapping, bongOntology);
				mappings.put(mapsfromresult.substring(1,mapsfromresult.length()-9),mapstoresult);
		}
		try {
			// Get the central ontology URI
			String centralontologyURI = owldealer.getAnnotation ("OntologyURI", owldealer.getClass(bongOntology.getURI() + "#CentralOntology"), bongOntology);
			// Get the root term URI
			String roottermURI = owldealer.getAnnotation ("ClassURI", owldealer.getClass(bongOntology.getURI() + "#RootTerm"), bongOntology);
			String accesoryOntologyURI = owldealer.getAnnotation ("OntologyURI", owldealer.getClass(bongOntology.getURI() + "#AccesoryOntology"), bongOntology);
			bongsettings = new BONGSettings (mappings,centralontologyURI,roottermURI, accesoryOntologyURI);
		} 
		catch (OWLException e) {e.printStackTrace();} 
		return bongsettings;
	}
}



