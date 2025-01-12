/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The RegexpMatcher.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The RegexpMatcher.java 
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;

import uk.ac.manchester.gong.bong.owl.OWLDealer;
import uk.ac.manchester.gong.bong.settings.BONGOntologyClassIterator;
import uk.ac.manchester.gong.bong.settings.BONGSettings;

public class RegexpMatcher {

	/**
	 * Tries to match the rdfs:label of a given class against a set of regexps
	 * defined in the BONG ontology. If there is a match it returns the 
	 * regexp OWLclass with the matcher, everything in a BONGMatchResult
	 * @throws OWLException 
	 */

	public static BONGMatchResult match (OWLClass classtomatch, OWLDealer owldealer, BONGSettings bongsettings) throws OWLException{
		OWLClass matchingregexpowlclass = null;
		Matcher matcher = null;
		OWLOntology ontology=owldealer.getOntology();
		// Try the term for each regexp
		BONGOntologyClassIterator iterator = new BONGOntologyClassIterator (ontology, "Regexp");
		while(iterator.hasNext()){
				//	Get the regexp
				OWLClass regexpowlclass = (OWLClass)iterator.next();
				String regexpstring = owldealer.getAnnotation("RegexpString", regexpowlclass, ontology);
				String properregexp = (String) regexpstring.substring(1,regexpstring.length()-9); 
				
				// Get the rdfs:label value
				String label = owldealer.getAnnotation("label", classtomatch, owldealer.getOntology(bongsettings.getCentralontologyURI()));
				String properlabel = (String) label.substring(0,label.length()-3); 
				
				// Do the matching
				Pattern pattern = Pattern.compile(properregexp);
		  		matcher = pattern.matcher(properlabel);
		  		boolean matchFound = matcher.find();
				if(matchFound){
					matchingregexpowlclass = regexpowlclass;
					break;
				}
		}
		BONGMatchResult bongmatchresult = new BONGMatchResult(matchingregexpowlclass,matcher);	
		return bongmatchresult;
	}
}
