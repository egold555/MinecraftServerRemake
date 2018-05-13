package org.golde.test.world;

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;

public class FlatWorldGenerator extends WorldGenerator{

	public FlatWorldGenerator(World world) {
		super(world);
	}

	@Override
	public Chunk generate(int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
		Chunk chunk = new Chunk(true);
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				chunk.getBlocks().set(x, 1, z, new BlockState(1, 0));
			}
		}
		return chunk;
	}

}
