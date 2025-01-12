/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The SQLDealer.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The SQLDealer.java 
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
 * This class stores the results in an Hypersonic DB
 * called "bong". The table name is provided by the user, 
 * it should something like "bong1"
 */

public class SQLDealer {
	
	private String tablename;
	private Connection conn;
	private Statement st;
	
	public SQLDealer (String tablename) {
		this.tablename=tablename;
	}
	public void connect () throws ClassNotFoundException, SQLException{
		Class.forName("org.hsqldb.jdbcDriver");
		conn= DriverManager.getConnection("jdbc:hsqldb:db/bong","SA","");
		st = conn.createStatement();
	}
	public void disconnect () throws SQLException {
		st.execute("SHUTDOWN");
		conn.close();
	}
	public void dropTable () throws SQLException {
		st.execute("DROP TABLE "+ tablename);
	}
	public void createTable () throws SQLException{
		st.execute("CREATE TABLE " + tablename +"(" +
				" id INT IDENTITY PRIMARY KEY, " +
				" origin VARCHAR(256), " +
				" relationship VARCHAR(256), " +
				" filler VARCHAR(256)" +
				")");
	}
	public void injectResult (int id, String origin, String relationship, String filler) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("INSERT INTO " + tablename + "(id,origin,relationship,filler)" + 
				    "VALUES(" + 
				    id + "," + 
				    "'" + origin + "'" +"," +
				    "'" + relationship + "'" +"," +
				    "'" + filler +"'" +
				    ")");
//		st.execute("INSERT INTO bong_4(id,origin,relationship,filler) VALUES(7,'GO_00000034','is_a','GO_00000005')");
//		st.execute("SELECT * FROM bong WHERE id=7 ");
//		ResultSet resultset = st.getResultSet();
//		while(resultset.next()){
//			System.out.println(resultset.getString("origin"));
//			System.out.println(resultset.getString("relationship"));
//			System.out.println(resultset.getString("filler"));
//		}
	}
}
