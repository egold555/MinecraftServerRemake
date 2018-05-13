package org.golde.test.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;

public abstract class WorldGenerator {

	private final World world;
	public WorldGenerator(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
	public abstract Chunk generate(int chunkX, int chunkZ);
	
}
