package generator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import codeGenerator.PolygonCollision;
import codeGenerator.Vector;

public class Room {
	private Node baseNode;
	private Point location;
	private ArrayList<Point> points;
	private ArrayList<Door> parentDoors;
	private ArrayList<Door> doors;
	private ArrayList<Door> unconnectedDoors;
	private double rotation;
	
	public Room(Node node, int scale) {
		baseNode = node;
		location = new Point(0, 0);
		setRotation(0);
		
		setParentDoors(node.getParentDoors());
		setDoors(node.getTotalDoors());
		
		centerPoints(scale);
		rotatePoints();
		
		unconnectedDoors = getTotalDoors();
	}
	
	public double getAngleOfDoor(Door door) {
		double angle = rotation;
		
		Point p1 = points.get(door.getPoint1());
		Point p2 = points.get(door.getPoint2());
		Vector direction = new Vector(p2.x - p1.x, p2.y - p1.y);
		direction = direction.normalize();
		direction = direction.rotate(Math.PI / 2);
		angle += PolygonCollision.getTheta(direction);
		
		return angle;
	}
	
	public ArrayList<Door> getAvailableDoors() {
		return unconnectedDoors;
	}
	
	public void rotatePoints() {
		
	}

	public Point getLocation() {
		return location;
	}
	
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public ArrayList<Point> getPoints() {
		ArrayList<Point> points = new ArrayList<>();
		
		for(Point point : this.points) {
			Vector v = rotation % (2 * Math.PI) == 0 ? new Vector(point.x, point.y) : new Vector(point.x, point.y).rotate(rotation);
			points.add(new Point((int)(v.getX() + 0.5), (int)(v.getY() + 0.5)));
		}
		
		return points;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
	public void setLocation(int x, int y) {
		location.x = x;
		location.y = y;
	}

	public Node getBaseNode() {
		return baseNode;
	}
	
	public void centerPoints(int scale) {
		Point averagePosition = new Point(0, 0);
		
		for(Point point : getBaseNode().getPoints()) {
			averagePosition.x += point.x * scale;
			averagePosition.y += point.y * scale;
		}
		
		averagePosition.x /= getBaseNode().getPoints().size();
		averagePosition.y /= getBaseNode().getPoints().size();
		
		ArrayList<Point> points = new ArrayList<>();
		
		for(Point point : getBaseNode().getPoints()) {
			Point newPoint = new Point(point.x * scale, point.y * scale);
			
			newPoint.x -= averagePosition.x;
			newPoint.y -= averagePosition.y;
			
			points.add(newPoint);
		}
		
		setPoints(points);
	}

	public ArrayList<Door> getParentDoors() {
		return parentDoors;
	}

	public void setParentDoors(Set<Door> parentDoors) {
		ArrayList<Door> d = new ArrayList<>();
		
		for(Door door : parentDoors) {
			d.add(door);
		}
		
		this.parentDoors = d;
		if(this.doors == null) return;
		unconnectedDoors = getTotalDoors();
	}

	public ArrayList<Door> getDoors() {
		return doors;
	}
	
	public ArrayList<Door> getTotalDoors() {
		ArrayList<Door> doors = new ArrayList<>();
		
		doors.addAll(this.doors);
		doors.addAll(parentDoors);
		
		return doors;
	}

	public void setDoors(Set<Door> doors) {
		ArrayList<Door> d = new ArrayList<>();
		
		for(Door door : doors) {
			d.add(door);
		}
		
		this.doors = d;
		if(this.parentDoors==null) return;
		unconnectedDoors = getTotalDoors();
	}

	public Vector getPositionOfDoor(Door door) {
		ArrayList<Point> points = getPoints();
		
		Point p1 = points.get(door.getPoint1());
		Point p2 = points.get(door.getPoint2());
		
		Vector line = new Vector(p2.getX() - p1.getX(), p2.getY() - p1.getY());
		line.setX(line.getX() * door.getScalar());
		line.setY(line.getY() * door.getScalar());
		
		return new Vector(p1.getX() + line.getX(), p1.getY() + line.getY());
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
