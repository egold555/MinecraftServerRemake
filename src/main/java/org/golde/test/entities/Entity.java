package org.golde.test.entities;

import org.golde.test.util.Location;

public class Entity {
	
	private final int id;
	private Location location;

	protected Entity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public Location getLoccation() {
		return location;
	}
	
	public void setLoccation(Location loc) {
		this.location = loc;
	}
	
}
