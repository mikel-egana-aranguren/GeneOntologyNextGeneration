/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The Starter.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The Starter.java 
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
import uk.ac.manchester.gong.bong.settings.BONGFileReader;

/**
 * This class reads the BONG file and passes all the information to BONG, 
 * the one that actually does everything.
 *
 */

public class BONGPerformer {

	public static void main(String[] args) {
		try {
			BONGFileReader.readFile(args[0]);
			BONG.work(BONGFileReader.tablename(),BONGFileReader.bongOntologyURI(), BONGFileReader.bongOntologyFile(),BONGFileReader.ontologies());
		} 
		catch (IOException e) {e.printStackTrace();}
	}
}
