package org.golde.test.entities;

import org.golde.test.util.Location;

import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;

public class EntityMob extends EntityLiving {

	protected EntityMob(int id, Location location, MobType mobType) {
		super(id, location);
	}

	@Override
	public int getMaxHealth() {
		return -1;
	}

}
