/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The OWLDealer.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The OWLCapsule.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */

package uk.ac.manchester.gong.bong.owl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.coode.owl.rdf.rdfxml.RDFXMLRenderer;
import org.semanticweb.owl.apibinding.OWLManager;

//import org.semanticweb.owl.impl.model.OWLAnnotationInstanceImpl;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLEntityAnnotationAxiom;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;

import org.semanticweb.owl.util.OntologyToPhysicalURIMapper;


/**
 * This is a little wrapper for the most basic operations in the OWL API.
 */

public class OWLDealer{
	private OWLOntology ontology;
	private OWLDataFactory factory;
	private OWLOntologyManager manager; 
	
	public OWLDealer (String curl, String cfile, HashMap files) throws OWLException{
			manager = OWLManager.createOWLOntologyManager();
			manager.addURIMapper(new OntologyToPhysicalURIMapper(URI.create(curl),URI.create(cfile)));
			Iterator urls = (files.keySet()).iterator();
			while(urls.hasNext()){
				String url=(String) urls.next();
				String file=(String) files.get(url);
				manager.addURIMapper(new OntologyToPhysicalURIMapper(URI.create(url),URI.create(file)));
			}
			ontology = manager.loadOntology(URI.create(curl));
			factory = ontology.getOWLDataFactory();
	}
	// Get main ontology
	public OWLOntology getOntology (){
		return ontology;	
	}
	// Get central ontology
	public OWLOntology getOntology (String uri) throws OWLException{
		return manager.getOntology(URI.create(uri));	
	}
	
	public OWLDataFactory getFactory (){
		return factory;
	}	
	public OWLOntologyManager getManager (){
		return manager;
	}
	public void writeOntology (String fileName, String ontologyURI) throws IOException, OWLException{
		File file = new File(fileName);
		OutputStream os = new BufferedOutputStream (new FileOutputStream (file));
		RDFXMLRenderer renderer = new RDFXMLRenderer (manager.getOntology(URI.create(ontologyURI)), new OutputStreamWriter(os));
		renderer.render();
		os.flush();
		os.close();
	}
	public OWLClass getClass (String classURI) throws OWLException{
		OWLClass clazz = factory.getOWLClass(URI.create(classURI));
		return clazz;
	}
	public OWLClass getClass (URI classURI) throws OWLException{
		OWLClass clazz = factory.getOWLClass(classURI);
		return clazz;
	}
	public HashMap generateLabel2Name (String ontouri) throws OWLException{
		HashMap label2name = new HashMap();
		OWLOntology map_ontology = manager.getOntology(URI.create(ontouri));
		Set classes = map_ontology.getReferencedClasses();
		Iterator classesIterator = classes.iterator();
		while(classesIterator.hasNext()){
			OWLClass clazz = (OWLClass) classesIterator.next();
			String long_label = getAnnotation ("label", clazz, map_ontology);
			if(long_label != null){
				String properlabel = (String) long_label.substring(1,long_label.length()-9);
				label2name.put(properlabel, clazz);
			}
		}
		return label2name;
	}
	public String getAnnotation (String annotProp, OWLClass clazz, OWLOntology ontology){
		String value = null;
		try {
			for(OWLEntityAnnotationAxiom annot : clazz.getAnnotations(ontology)){
				String prop = annot.getURI().getFragment();
				if(prop.equals(annotProp)){
					value = annot.getLiteral().toString();
					break;
				}
			}
		} 
		catch (OWLException e) {e.printStackTrace();}		
		return value;
	}
}
