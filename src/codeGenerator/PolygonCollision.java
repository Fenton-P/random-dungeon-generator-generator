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
	
	private static double[] projectOntoAxis(Vector[] points, Vector axis) {
	    double min = dot(points[0], axis);
	    double max = min;
	    
	    for (int i = 1; i < points.length; i++) {
	        double projection = dot(points[i], axis);
	        if (projection < min) min = projection;
	        if (projection > max) max = projection;
	    }
	    
	    return new double[] { min, max };
	}
	
	private static double dot(Vector v1, Vector v2) {
	    return v1.getX() * v2.getX() + v1.getY() * v2.getY();
	}
	
	public static boolean triangleCollision(Triangle t1, Triangle t2) {
	    Vector[][] all = { t1.getPoints(), t2.getPoints() };
	    
	    for (int shape = 0; shape < 2; shape++) {
	        Vector[] pts = all[shape];
	        
	        for (int i = 0; i < 3; i++) {
	            Vector p1 = pts[i];
	            Vector p2 = pts[(i + 1) % 3];
	            Vector edge = new Vector(p2.getX() - p1.getX(), p2.getY() - p1.getY());
	            
	            Vector axis = new Vector(-edge.getY(), edge.getX());
	            
	            double[] proj1 = projectOntoAxis(t1.getPoints(), axis);
	            double[] proj2 = projectOntoAxis(t2.getPoints(), axis);
	            
	            double give = 0;
	            if (proj1[1] <= proj2[0] + give || proj2[1] <= proj1[0] + give) {
	                return false;
	            }
	        }
	    }
	    
	    return true;
	}
	
	private static boolean isAngleGreaterThan180(Vector v1, Vector v2, Vector v3) {
		Vector v4 = new Vector(v1.getX() - v2.getX(), v1.getY() - v2.getY());
		Vector v5 = new Vector(v3.getX() - v2.getX(), v3.getY() - v2.getY());
		double cross = v4.getX() * v5.getY() - v4.getY() * v5.getX();
		
		return cross < 0;
	}
	
	public static Set<Triangle> triangulateRoom(Room room) {
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
			
			boolean angle = isAngleGreaterThan180(p1, p2, p3);
			
			if(angle) {
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
			currentIndex = 0;
		}
		
		triangles.add(new Triangle(points.get(0), points.get(1), points.get(2)));
		
		return triangles;
	}
	
	private static boolean pointInTriangle(Vector point, Triangle triangle) {
//		double scalar1 = 0;
//		double scalar2 = 0;
//		
//		Vector A = triangle.getPoints()[0];
//		Vector B = triangle.getPoints()[1];
//		Vector C = triangle.getPoints()[2];
//		
////		B.setX(B.getX() - A.getX());
////		B.setY(B.getY() - A.getY());
////		
////		C.setX(C.getX() - A.getX());
////		C.setY(C.getY() - A.getY());
////		
////		A.setX(0);
////		A.setY(0);
//		
//		double bottom = (B.getY() - A.getY()) * (C.getX() - A.getX()) - (B.getX() - A.getX()) * (C.getY() - A.getY());
//		double top = A.getX() * (C.getY() - A.getY()) + (point.getY() - A.getY()) * (C.getX() - A.getX()) - point.getX() * (C.getY() - A.getY());
//		
//		scalar1 = top / bottom;
//		
//		top = point.getY() - A.getY() - scalar1 * (B.getY() - A.getY());
//		bottom = C.getY() - A.getY();
//		
//		scalar2 = top / bottom;
//		
//		if(!Double.isFinite(scalar1) || !Double.isFinite(scalar2)) {
//			return pointInTriangle(point, new Triangle(B, C, A));
//		}
//		
//		boolean finalBool = scalar2 + scalar1 < 1 && scalar2 > 0 && scalar1 > 0;
//		
//		//System.out.println(triangle + " | " + point);
//		//System.out.println(finalBool);
//		
//		return finalBool;
		
		double dX = point.getX() - triangle.getPoints()[1].getX();
		double dY = point.getY() - triangle.getPoints()[1].getY();
		double dX1 = triangle.getPoints()[0].getX() - triangle.getPoints()[1].getX();
		double dY1 = triangle.getPoints()[0].getY() - triangle.getPoints()[1].getY();
		double dX2 = triangle.getPoints()[2].getX() - triangle.getPoints()[1].getX();
		double dY2 = triangle.getPoints()[2].getY() - triangle.getPoints()[1].getY();
		
		double dot00 = dX1 * dX1 + dY1 * dY1;
		double dot01 = dX1 * dX2 + dY1 * dY2;
		double dot02 = dX1 * dX + dY1 * dY;
		double dot11 = dX2 * dX2 + dY2 * dY2;
		double dot12 = dX2 * dX + dY2 * dY;
		
		double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;
	
	  return u >= 0 && v >= 0 && u + v <= 1;
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
		
		//System.out.println(beta);
		//System.out.println(possibleAngles);
		
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
	
	public static boolean checkPointAgainstRoom(Room room, Vector v) {
		Set<Triangle> triangles = triangulateRoom(room);
		
		for(Triangle triangle : triangles) {
			if(pointInTriangle(v, triangle)) return true;
		}
		
		return false;
	}
}