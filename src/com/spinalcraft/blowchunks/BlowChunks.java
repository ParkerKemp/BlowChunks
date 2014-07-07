package com.spinalcraft.blowchunks;

public class BlowChunks {
	
	public static void main(String[] args){
		if(args.length < 1){
			System.out.println("Must specify a path to region folder!");
			return;
		}
		
		String regionDir = args[0];
		
		ChunkDeleter deleter = new ChunkDeleter(regionDir);
		
		//deleter.deleteChunk(5, 2);
		//System.out.println("Deleted a chunk.");
	}
}
