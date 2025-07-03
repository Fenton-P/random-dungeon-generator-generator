package generator;

import codeGenerator.Vector;

public class VectorLine {
	private Vector p1;
	private Vector p2;
	private double A;
	private double B;
	private double C;
	
	public VectorLine(Vector p1, Vector p2) {
		this.setP1(p1);
		this.setP2(p2);
	}
	
	private void updateABC() {
		if(p2 == null || p1 == null) return;
		
		A = p2.getX() - p1.getX();
		B = p1.getY() - p2.getY();
		C = p1.getY() * p2.getY() + p1.getX() * p1.getX() - p1.getX() * p2.getX() - p1.getY() * p1.getY();
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

	public Vector getP1() {
		return p1;
	}

	public void setP1(Vector p1) {
		this.p1 = p1;
		updateABC();
	}

	public Vector getP2() {
		return p2;
	}

	public void setP2(Vector p2) {
		this.p2 = p2;
		updateABC();
	}
	
	public double getDistanceFromPoint(Vector p) {
		double top = Math.abs(A * p.getX() + B * p.getX() + C);
		double bottom = Math.sqrt(A * A + B * B);
		
		return top / bottom;
	}
	
	public String toString() {
		return p1 + " | " + p2;
	}
	
	public boolean equals(Line line) {
		double a = A / line.getA();
		double b = B / line.getB();
		double c = C / line.getC();
		
		boolean X = Double.isFinite(a);
		boolean Y = Double.isFinite(b);
		boolean Z = Double.isFinite(c);
		
		if(X && Y && Z) {
			return a == b && b == c;
		}
		
		int countTrue = (X ? 1 : 0) + (Y ? 1 : 0) + (Z ? 1 : 0);
		
		if(countTrue == 0) {
			return A == B && B == C && C == line.getA() && line.getA() == line.getB() && line.getB() == line.getC() && A == 0;
		}
		
		if(countTrue == 2) {
			if(!X) {
				return A == 0 && line.getA() == 0;
			}
			
			if(!Y) {
				return B == 0 && line.getB() == 0;
			}
			
			if(!Z) {
				return C == 0 && line.getC() == 0;
			}
		}
		
		if(countTrue == 1) {
			if(X) {
				return B == 0 && C == 0 && line.getB() == 0 && line.getC() == 0;
			}
			
			if(Y) {
				return A == 0 && C == 0 && line.getA() == 0 && line.getC() == 0;
			}
			
			if(Z) {
				return B == 0 && A == 0 && line.getB() == 0 && line.getA() == 0;
			}
		}
		
		return false;
	}
}
