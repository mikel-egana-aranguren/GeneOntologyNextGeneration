/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The BONGMatchResult.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The BONGMatchResult.java 
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

import org.semanticweb.owl.model.OWLClass;

/**
 * This stores a mathc result, composed of the regexp OWLClass
 * and the Matcher that was used with the regexp, result, and everything.
 *
 */

public class BONGMatchResult {
	
	private OWLClass regexpclass;
	private Matcher matcher;
	
	public BONGMatchResult(OWLClass regexpclass,Matcher matcher) {
		this.regexpclass = regexpclass;
		this.matcher=matcher;
	}
	public OWLClass getRegexpOWLClass (){
		return regexpclass;
	}
	public Matcher getMatcher (){
		return matcher;
	}

}
