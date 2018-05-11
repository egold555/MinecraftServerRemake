package org.golde.test.util;

public class Location {

	private double x;
	private double y;
	private double z;
	private double yaw;
	private double pitch;
	boolean onGround;
	
	public Location(double x, double y, double z) {
		this(x, y, z, 0, 0);
	}
	
	public Location(double x, double y, double z, double yaw, double pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
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

	public double getYaw() {
		return yaw;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
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
