package codeGenerator;

public class Triangle {
	private Vector[] points = new Vector[3];
	
	public Triangle(Vector p1, Vector p2, Vector p3) {
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
	}
	
	public Triangle(Vector[] points) {
		this.points = points;
	}
	
	public Vector[] getPoints() {
		return points;
	}
	
	@Override
	public String toString() {
		String returnString = "Point 1: X: " + points[0].getX() + ", Y: " + points[0].getY();
		returnString += ". Point 2: X: " + points[1].getX() + ", Y: " + points[1].getY();
		returnString += ". Point 3: X: " + points[2].getX() + ", Y: " + points[2].getY() + ".";
		
		return returnString;
	}
}
