package com.spinalcraft.blowchunks;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BlowChunks {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost";
	static Connection conn = null;
	
	public static void main(String[] args){
		if(args.length < 1){
			System.out.println("Must specify a path to region folder!");
			return;
		}
		
		Path regionDir = Paths.get(args[0]);
		
		connectToDatabase();
		
		ChunkDeleter deleter = new ChunkDeleter(conn, regionDir);
		
		switch(deleter.deleteChunksFromDB()){
		case 0:
			System.out.println("Successfully deleted chunks!");
			break;
		case 1:
			System.out.println("SQL error.");
			break;
		}
	}
	
	private static void connectToDatabase(){
		String query;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, "root", "password");
			Statement stmt = conn.createStatement();
			stmt = conn.createStatement();

			query = "USE Spinalcraft";
			stmt.executeQuery(query);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
