package codeGenerator;

import java.awt.Point;
import java.util.ArrayList;

public class Vector {
	private double x;
	private double y;
	
	public Vector(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Vector(Point point) {
		setX(point.getX());
		setY(point.getY());
	}
	
	public Vector(Vector vector) {
		setX(vector.getX());
		setY(vector.getY());
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vector normalize() {
		double length = getLength();
		
		Vector rtnVal = new Vector(getX() / length, getY() / length);
		return rtnVal;
	}
	
	public static Vector normalize(Point point) {
		return new Vector(point).normalize();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public static ArrayList<Vector> toVectorArray(ArrayList<Point> points) {
		ArrayList<Vector> returnList = new ArrayList<>();
		
		for(Point point : points) {
			returnList.add(0, new Vector(point));
		}
		
		return returnList;
	}
	
	public String toString() {
		return "X: " + x + ", Y: " + y;
	}
}
