/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The BONGSettings.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The BONGSettings.java 
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

/**
 * Class for storing the settings from the BONG ontology, except the regexps
 */

public class BONGSettings {
	private HashMap mappings; 
	private String centralOntologyURI; 
	private String rootTermURI; 
	private String accesoryOntologyURI;

	public BONGSettings(HashMap mappings, String centralOntologyURI, String rootTermURI, String accesoryOntologyURI) {
		this.mappings = mappings; 
		this.centralOntologyURI = centralOntologyURI; 
		this.rootTermURI = rootTermURI; 
		this.accesoryOntologyURI = accesoryOntologyURI;
	}
	
	/**
	 * HashMap of mappings: key = "maps_from", value = "maps_to"
	 */
	public HashMap getMappings (){
		return mappings;
	}
	public String getCentralontologyURI (){
		return centralOntologyURI;
	}
	public String getRootTermURI (){
		return rootTermURI;
	}
	public String getAccesoryOntologyURI (){
		return accesoryOntologyURI;
	}
}
