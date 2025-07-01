package generator;

import java.awt.Point;
import java.util.ArrayList;

public class Room {
	private Node baseNode;
	private Point location;
	private ArrayList<Point> points;
	
	public Room(Node node, int scale) {
		baseNode = node;
		location = new Point(0, 0);
		
		centerPoints(scale);
		rotatePoints();
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
			points.add(new Point(point.x, point.y));
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
}
