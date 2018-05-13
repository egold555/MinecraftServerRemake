package org.golde.test.entities;

import org.golde.test._Main;
import org.golde.test.util.Location;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;

public class Entity {

	private final int id;
	private Location location;

	protected Entity(int id, Location location) {
		this.id = id;
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location loc) {
		// determine what has changed about the location.
		boolean positionChanged = false, headChanged = false;

		if (loc.getX() != this.location.getX())
			positionChanged = true;
		if (loc.getY() != this.location.getY())
			positionChanged = true;
		if (loc.getZ() != this.location.getZ())
			positionChanged = true;
		if (loc.getYaw() != this.location.getYaw())
			headChanged = true;
		if (loc.getPitch() != this.location.getPitch())
			headChanged = true;

		// For every session, send a packet that the entity has moved.
		//http://wiki.vg/Protocol#Entity_Teleport
		for (EntityPlayer other: _Main.getInstance().getOnlinePlayers()) {
			if(other != this) {
				if (positionChanged && headChanged){
					other.getSession().send(new ServerEntityPositionRotationPacket(getId(), loc.getX() - location.getX(), loc.getY() - location.getY(), loc.getZ() - location.getZ(), location.getYaw() - loc.getYaw(), location.getPitch() - loc.getPitch(), loc.isOnGround()));
				}
				else if (positionChanged){
					other.getSession().send(new ServerEntityPositionPacket(getId(), loc.getX() - location.getX(), loc.getY() - location.getY(), loc.getZ() - location.getZ(), loc.isOnGround()));
				}
				else if (headChanged){
					other.getSession().send(new ServerEntityRotationPacket(getId(), location.getYaw() - loc.getYaw(), location.getPitch() - loc.getPitch(), loc.isOnGround()));
				}
			}
		}
		
		this.location = loc;


	}



}
