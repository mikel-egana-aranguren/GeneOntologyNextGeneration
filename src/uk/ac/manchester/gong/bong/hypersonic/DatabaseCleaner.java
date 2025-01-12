/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The DatabaseCleaner.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The DatabaseCleaner.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */



package uk.ac.manchester.gong.bong.hypersonic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Little program for dropping tables from BONG database; pass table name 
 * executing.
 *
 */
public class DatabaseCleaner {

	public static void main(String[] args) {
		SQLDealer sqldealer = new SQLDealer (args[0]);
		try {
			sqldealer.connect();
			sqldealer.dropTable();
			sqldealer.disconnect();
		} 
		catch (ClassNotFoundException e) {e.printStackTrace();} 
		catch (SQLException e) {e.printStackTrace();}
	}
}
