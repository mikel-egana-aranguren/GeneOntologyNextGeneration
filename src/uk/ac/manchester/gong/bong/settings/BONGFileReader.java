/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The BONGFileReader.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The BONGFileReader.java 
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Parser for reading and stroing the BONG file info.
 *
 */

public class BONGFileReader {
	private static String tablename;
	private static String bongOntologyURI;
	private static String bongOntologyFile;
	private static HashMap ontologies;
	
	public static void readFile(String file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		ontologies = new HashMap();
	    String str;
	    while ((str = in.readLine()) != null) {
	    	if(str.startsWith("[HypersonicTableName] ")){
	    		tablename = str.split(" ")[1];
	    	}
	    	else if (str.startsWith("[BONGOntology] ")){
	    		bongOntologyFile = str.split(" - ")[1];
	    		bongOntologyURI = (str.split(" - ")[0]).split(" ")[1]; 
//	    		System.out.println((str.split(" - ")[0]).split(" ")[1]);
	    	}
	    	else{
	    		ontologies.put((str.split(" - ")[0]).split(" ")[1], str.split(" - ")[1]);
//	    		System.out.println(str.split(" - ")[1]);
//	    		System.out.println((str.split(" - ")[0]).split(" ")[1]); 
	    	}
	    }
	    in.close();
	}
	public static String tablename (){
		return tablename;
	}
	public static String bongOntologyFile (){
		return bongOntologyFile;
	}
	public static String bongOntologyURI (){
		return bongOntologyURI;
	}
	public static HashMap ontologies (){
		return ontologies;
	}
}
