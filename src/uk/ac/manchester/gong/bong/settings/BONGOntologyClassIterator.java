/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The BONGOntologyClassIterator.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The BONGOntologyClassIterator.java 
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

import java.net.URI;
import java.util.Iterator;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;

/**
 * Implementation of the Iterator interface for getting classes from
 * an OWL ontology of the type: class1, class2, ...
 *
 */

public class BONGOntologyClassIterator implements Iterator {
	private OWLOntology ontology;
	private String classgenericname;
	private int index;

	public BONGOntologyClassIterator(OWLOntology ontology, String classgenericname) {
		this.ontology = ontology;
		this.classgenericname = classgenericname;
		this.index = 1;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		boolean hasnext = false;
		String clazzname= classgenericname + index;
		try {
			boolean ref=(ontology.containsClassReference(URI.create(ontology.getURI() + "#" + clazzname)));
			if(ref){
				hasnext = true;
			}
		} 
		catch (OWLException e) {e.printStackTrace();} 
		return hasnext;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public Object next() {
		OWLClass clazz = null;
		String clazzname= classgenericname + index;
		try {
			clazz = ontology.getOWLDataFactory().getOWLClass(URI.create(ontology.getURI() + "#" + clazzname));
		} 
		catch (OWLException e) {e.printStackTrace();}
		index = index + 1;
		return clazz;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {}
}
