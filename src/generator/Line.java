package generator;

import java.awt.Point;

public class Line {
	private Point p1;
	private Point p2;
	private int A;
	private int B;
	private int C;
	
	public Line(Point p1, Point p2) {
		this.setP1(p1);
		this.setP2(p2);
	}
	
	private void updateABC() {
		if(p2 == null || p1 == null) return;
		
		A = p2.y - p1.y;
		B = p1.x - p2.x;
		C = p1.y * p2.x - p1.x * p2.y;
	}
	
	public double getA() {
		return A;
	}
	
	public double getB() {
		return B;
	}
	
	public double getC() {
		return C;
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
		updateABC();
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
		updateABC();
	}
	
	public double getDistanceFromPoint(Point p) {
		int top = Math.abs(A * p.x + B * p.y + C);
		double bottom = Math.sqrt(A * A + B * B);
		
		return top / bottom;
	}
	
	public String toString() {
		return p1 + " | " + p2 + " | " + A + " | " + B + " | " + C;
	}
}
