package generator;

import codeGenerator.Vector;

public class Door {
	private Vector point1;
	private Vector point2;
	private double scalar;
	
	public Door(Vector point1, Vector point2, double scalar) {
		this.setPoint1(point1);
		this.setPoint2(point2);
		this.setScalar(scalar);
	}
	
	public Door() {
		this(new Vector(0, 0), new Vector(0, 1), 0.5);
	}

	public Vector getPoint1() {
		return point1;
	}

	public void setPoint1(Vector point1) {
		this.point1 = point1;
	}

	public Vector getPoint2() {
		return point2;
	}

	public void setPoint2(Vector point2) {
		this.point2 = point2;
	}

	public double getScalar() {
		return scalar;
	}

	public void setScalar(double scalar) {
		this.scalar = scalar;
	}
	
	public String toString() {
		return point1 + " | " + point2 + " | " + scalar;
	}
}
