package org.golde.test.util;

public class Location {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	boolean onGround;
	
	public Location(double x, double y, double z) {
		this(x, y, z, 0, 0);
	}
	
	public Location(double x, double y, double z, float yaw, float pitch) {
		this(x, y, z, yaw, pitch, false);
	}
	
	public Location(double x, double y, double z, float yaw, float pitch, boolean onGround) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
    public boolean isOnGround() {
		return onGround;
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + ", onGround="
				+ onGround + "]";
	}

	
	
	
	
}
