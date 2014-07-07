package com.spinalcraft.blowchunks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChunkDeleter {
	private String regionDirPath;
	
	public ChunkDeleter(String regionDirPath){
		this.regionDirPath = regionDirPath;
	}
	
	public void deleteChunk(int chunkX, int chunkZ){
		int regionX = (int)Math.floor(chunkX / 32.0);
		int regionZ = (int)Math.floor(chunkZ / 32.0);
		
		Path regionFilename = Paths.get(regionDirPath, "r." + regionX + "." + regionZ + ".mca");
		
		int xPosInRegion = chunkX % 32;
		if(chunkX < 0)
			chunkX += 32;
		int zPosInRegion = chunkZ % 32;
		if(chunkZ < 0)
			chunkZ += 32;
		
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
	}
}
