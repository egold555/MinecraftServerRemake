package org.golde.test.entities;

import org.golde.test.util.Location;

public abstract class EntityLiving extends Entity {
	
	protected EntityLiving(int id, Location location) {
		super(id, location);
	}
	
	public abstract int getMaxHealth();

}
