/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The BONGSettingsChecker.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The BONGSettingsChecker.java 
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

/**
 * @author Mikel Egana Aranguren
 *
 */
public class BONGSettingsChecker {

	/**
	 * 
	 */
	public BONGSettingsChecker() {
		// TODO Auto-generated constructor stub
	}
	public void checkSettings () throws MalformedBONGOntologyException{
		// Include this in last version, to check integrity of BONG
		// ontology
//		try {
//			OWLClass noclass = ontology.getClass(new URI(ontology.getLogicalURI() + "#NoClass"));
//			if(noclass == null){
//				throw (new MalformedBONGOntologyException("Malformed BONG ontology: class not found"));
//			}
//		} catch (OWLException e) {
//			// TODO Auto-generated catch block
//			throw (new MalformedBONGOntologyException("Malformed BONG ontology:",e));
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			throw (new MalformedBONGOntologyException("Malformed BONG ontology:",e));
//		}
		
	}

}
