package generator;

public class Door {
	private int point1;
	private int point2;
	private double scalar;
	
	public Door(int point1, int point2, double scalar) {
		this.setPoint1(point1);
		this.setPoint2(point2);
		this.setScalar(scalar);
	}
	
	public Door() {
		this(0, 1, 0.5);
	}

	public int getPoint1() {
		return point1;
	}

	public void setPoint1(int point1) {
		this.point1 = point1;
	}

	public int getPoint2() {
		return point2;
	}

	public void setPoint2(int point2) {
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
