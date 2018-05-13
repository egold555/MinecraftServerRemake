package org.golde.test.world;

public class World {

	private final String name;
	private final WorldGenerator worldGenerator;
	
	public World(String name) {
		this.name = name;
		this.worldGenerator = new FlatWorldGenerator(this);
	}
	
	public WorldGenerator getWorldGenerator() {
		return worldGenerator;
	}
	
}
