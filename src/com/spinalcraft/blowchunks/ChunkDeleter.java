package com.spinalcraft.blowchunks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class ChunkLocation{
	public String world;
	public int x, z;
	public ChunkLocation(String world, int x, int z){
		this.world = world;
		this.x = x;
		this.z = z;
	}
}

public class ChunkDeleter {
	private Path serverFolderPath;
	private Connection conn;
	
	public ChunkDeleter(Connection conn, Path serverFolderPath){
		this.serverFolderPath = serverFolderPath;
		this.conn = conn;
	}
	
	public int deleteChunksFromDB(){
		String query = "SELECT world, x, z FROM Chunks";// WHERE deleted IS NULL";
		Statement stmt;
				
		ArrayList<ChunkLocation> chunks = new ArrayList<ChunkLocation>();
		
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				chunks.add(new ChunkLocation(rs.getString("world"), rs.getInt("x"), rs.getInt("z")));
			}
			for(int i = 0; i < chunks.size(); i ++)
				deleteChunk(chunks.get(i));
			
			//query = "UPDATE Chunks SET deleted = NULL WHERE deleted NOT NULL";
			//stmt = conn.createStatement();
			//stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		
		return 0;
	}
	
	private void deleteChunk(ChunkLocation location){
		int regionX = (int)Math.floor(location.x / 32.0);
		int regionZ = (int)Math.floor(location.z / 32.0);
		
		Path regionDirPath = serverFolderPath;
		
		if(location.world.equalsIgnoreCase("world")){
			regionDirPath = serverFolderPath.resolve("world/region");
		}
		else if(location.world.equalsIgnoreCase("world_nether")){
			regionDirPath = serverFolderPath.resolve("world_nether/DIM-1/region");
		}
		else if(location.world.equalsIgnoreCase("world_the_end")){
			regionDirPath = serverFolderPath.resolve("world_the_end/DIM1/region");
		}
		
		Path regionFilename = regionDirPath.resolve("r." + regionX + "." + regionZ + ".mca");
		
		int xPosInRegion = location.x % 32;
		if(xPosInRegion < 0)
			xPosInRegion += 32;
		int zPosInRegion = location.z % 32;
		if(zPosInRegion < 0)
			zPosInRegion += 32;
		
		long byteOffset = 4 * (xPosInRegion + zPosInRegion * 32);
		byte[] zeroBytes = {0, 0, 0, 0};
		
		try {
			RandomAccessFile raf = new RandomAccessFile(regionFilename.toFile(), "rw");
			raf.seek(byteOffset);
			raf.write(zeroBytes);
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Deleted chunk (" + location.x + ", " + location.z + ")");
	}
}
