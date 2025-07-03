package codeGenerator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import generator.Room;

public abstract class PolygonCollision {
	
	public static boolean isColliding(Room room1, Room room2) {
		Set<Triangle> room1Triangles = triangulateRoom(room1);
		Set<Triangle> room2Triangles = triangulateRoom(room2);
		
		room1Triangles = translate(room1Triangles, room1.getLocation());
		room2Triangles = translate(room2Triangles, room2.getLocation());
		
		for(Triangle triangle1 : room1Triangles) {
			//System.out.println("MAIN");
			//System.out.println(triangle1);
			//System.out.println("SECOND");
			for(Triangle triangle2 : room2Triangles) {
				//System.out.println(triangle2);
				if(triangleCollision(triangle1, triangle2)) return true;
			}
		}
		
		return false;
	}
	
	private static Set<Triangle> translate(Set<Triangle> triangles, Point point) {
//		for(Triangle triangle : triangles) {
//			for(Vector vector : triangle.getPoints()) {
//				System.out.println(vector);
//				vector.setX(vector.getX() + point.x);
//				vector.setY(vector.getY() + point.y);
//			}
//		}
		
		Set<Triangle> triSet = new HashSet<>();
		
		for(Triangle triangle : triangles) {
			Vector[] points = triangle.getPoints();
			
			Vector vector1 = new Vector(points[0].getX() + point.x, points[0].getY() + point.y);
			Vector vector2 = new Vector(points[1].getX() + point.x, points[1].getY() + point.y);
			Vector vector3 = new Vector(points[2].getX() + point.x, points[2].getY() + point.y);
			
			Triangle temp = new Triangle(vector1, vector2, vector3);
			
			triSet.add(temp);
		}
		
		return triSet;
	}
	
	private static boolean triangleCollision(Triangle tri1, Triangle tri2) {
		//System.out.println("FIRST");
		for(Vector point1 : tri1.getPoints()) {
			//System.out.println(point1 + " | " + tri2);
			//System.out.println(point1);
			if(pointInTriangleEdge(point1, tri2)) return true;
		}
		
		//System.out.println("SECOND");
		for(Vector point2 : tri2.getPoints()) {
			//System.out.println(point2);
			if(pointInTriangleEdge(point2, tri1)) return true;
		}
		
		return false;
	}
	
	private static Set<Triangle> triangulateRoom(Room room) {
		ArrayList<Vector> points = Vector.toVectorArray(room.getPoints());
		Set<Triangle> triangles = new HashSet<>();
		
		//System.out.println(points);
		
		int currentIndex = 0;
		while(points.size() > 3) {
			if(currentIndex >= points.size()) currentIndex = 0;
			
			int prevIndex = currentIndex == 0 ? points.size() - 1 : currentIndex - 1;
			int futureIndex = currentIndex == points.size() - 1 ? 0 : currentIndex + 1;
			
			Vector p1 = points.get(prevIndex);
			Vector p2 = points.get(currentIndex);
			Vector p3 = points.get(futureIndex);
			
			double angle = getAngleFromPoints(p1, p2, p3);
			
			if(angle >= Math.PI) {
				currentIndex++;
				continue;
			}
			
			//System.out.println("TEST");
			
			boolean skip = false;
			for(int i = 0;i<points.size();i++) {
				if(i == prevIndex || i == currentIndex || i == futureIndex) continue;
				if(pointInTriangle(points.get(i), new Triangle(p1, p2, p3))) {
					skip = true;
					break;
				}
			}
			
			if(skip) {
				currentIndex++;
				continue;
			}
			
			//System.out.println(p1 + " | " + p2 + " | " + p3);
			
			//System.out.println(currentIndex);
			
			triangles.add(new Triangle(p1, p2, p3));
			points.remove(currentIndex);
			currentIndex++;
		}
		
		triangles.add(new Triangle(points.get(0), points.get(1), points.get(2)));
		
		return triangles;
	}
	
	private static boolean pointInTriangle(Vector point, Triangle triangle) {
		double scalar1 = 0;
		double scalar2 = 0;
		
		Vector A = triangle.getPoints()[0];
		Vector B = triangle.getPoints()[1];
		Vector C = triangle.getPoints()[2];
		
//		B.setX(B.getX() - A.getX());
//		B.setY(B.getY() - A.getY());
//		
//		C.setX(C.getX() - A.getX());
//		C.setY(C.getY() - A.getY());
//		
//		A.setX(0);
//		A.setY(0);
		
		double bottom = (B.getY() - A.getY()) * (C.getX() - A.getX()) - (B.getX() - A.getX()) * (C.getY() - A.getY());
		double top = A.getX() * (C.getY() - A.getY()) + (point.getY() - A.getY()) * (C.getX() - A.getX()) - point.getX() * (C.getY() - A.getY());
		
		scalar1 = top / bottom;
		
		top = point.getY() - A.getY() - scalar1 * (B.getY() - A.getY());
		bottom = C.getY() - A.getY();
		
		scalar2 = top / bottom;
		
		if(!Double.isFinite(scalar1) || !Double.isFinite(scalar2)) {
			return pointInTriangle(point, new Triangle(B, C, A));
		}
		
		boolean finalBool = scalar2 + scalar1 < 1 && scalar2 > 0 && scalar1 > 0;
		
		//System.out.println(triangle + " | " + point);
		//System.out.println(finalBool);
		
		return finalBool;
	}
	
	private static boolean pointInTriangleEdge(Vector point, Triangle triangle) {
		double scalar1 = 0;
		double scalar2 = 0;
		
		Vector A = triangle.getPoints()[0];
		Vector B = triangle.getPoints()[1];
		Vector C = triangle.getPoints()[2];
		
//		B.setX(B.getX() - A.getX());
//		B.setY(B.getY() - A.getY());
//		
//		C.setX(C.getX() - A.getX());
//		C.setY(C.getY() - A.getY());
//		
//		A.setX(0);
//		A.setY(0);
		
		double bottom = (B.getY() - A.getY()) * (C.getX() - A.getX()) - (B.getX() - A.getX()) * (C.getY() - A.getY());
		double top = A.getX() * (C.getY() - A.getY()) + (point.getY() - A.getY()) * (C.getX() - A.getX()) - point.getX() * (C.getY() - A.getY());
		
		scalar1 = top / bottom;
		
		top = point.getY() - A.getY() - scalar1 * (B.getY() - A.getY());
		bottom = C.getY() - A.getY();
		
		scalar2 = top / bottom;
		
		if(!Double.isFinite(scalar1) || !Double.isFinite(scalar2)) {
			return pointInTriangleEdge(point, new Triangle(B, C, A));
		}
		
		boolean finalBool = scalar2 + scalar1 <= 1 && scalar2 >= 0 && scalar1 >= 0;
		
		//System.out.println(triangle + " | " + point);
		//System.out.println(finalBool);
		
		return finalBool;
	}
	
	private static double getAngleFromPoints(Vector p1, Vector p2, Vector p3) {
		if(p2.getX() == 0 && p2.getY() == 0) {
			p1.setX(p1.getX() + 1);
			p1.setY(p1.getY() + 1);
			
			p2.setX(p2.getX() + 1);
			p2.setY(p2.getY() + 1);
			
			p3.setX(p3.getX() + 1);
			p3.setY(p3.getY() + 1);
		}
		
		double side1 = getLength(p1, p2);
		double side2 = getLength(p2, p3);
		double side3 = getLength(p3, p1);
		Vector centroid = getCentroid(p1, p2, p3);
		
		double top = side1 * side1 + side2 * side2 - side3 * side3;
		double bottom = 2 * side1 * side2;
		//System.out.println(bottom);
		double quotient = top / bottom;
		double theta = Math.acos(quotient);
		//System.out.println(theta);
		//System.out.println(quotient);
		
		Vector normalizedP2 = p2.normalize();
		Vector normalizedCentroid = centroid.normalize();
		
		double theta2 = getTheta(normalizedP2);
		double theta1 = getTheta(normalizedCentroid);
		
		//System.out.println(theta1 + " | " + theta2);
		
		if(theta1 > theta2) theta = 2 * Math.PI - theta;
		
		return theta;
	}
	
	public static double getTheta(Vector vector) {
		Set<Double> possibleAngles = new HashSet<>();
		
		double beta = Math.acos(vector.getX());
		possibleAngles.add(beta);
		possibleAngles.add(2 * Math.PI - beta);
		
		beta = Math.asin(vector.getY());
		
		System.out.println(beta);
		System.out.println(possibleAngles);
		
		if(beta < 0) beta = Math.PI * 2 + beta;
		
		for(double theta : possibleAngles) {
			if((int) (theta * 1000) == (int) (beta * 1000)) return theta;
		}
		
		return Math.PI - beta;
	}
	
	private static Vector getCentroid(Vector p1, Vector p2, Vector p3) {
		Vector centroid = new Vector((p1.getX() + p2.getX() + p3.getX()) / 3.0, (p1.getY() + p2.getY() + p3.getY()) / 3.0);
		
		return centroid;
	}
	
	private static double getLength(Vector p1, Vector p2) {
		double xDiff = p1.getX() - p2.getX();
		double yDiff = p1.getY() - p2.getY();
		
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}
}