package generator;

import codeGenerator.Vector;

public class Door {
	private Vector doorLocation;
	
	public Door(Vector doorLocation) {
		this.doorLocation = doorLocation;
	}
	
	public Vector getDoorLocation() {
		return doorLocation;
	}
	
	public double getPointInFrontOf() {
		return doorLocation.getX();
	}
	
	public double getRatioAsPercent() {
		return doorLocation.getY();
	}
	
	public void setDoorLocation(Vector door) {
		doorLocation.setX(door.getX());
		doorLocation.setY(door.getY());
	}
}
