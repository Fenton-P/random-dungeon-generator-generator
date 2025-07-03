package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import codeGenerator.PolygonCollision;
import codeGenerator.Vector;
import generator.Door;
import generator.Line;
import generator.Main;
import generator.VectorLine;

public class DoorPanel extends Widget {
	private static final long serialVersionUID = 5085530429578026984L;

	private Set<Door> doors;
	private final Color doorColor = new Color(255, 211, 99);
	private int doorWidth;
	private int doorHeight;
	private int scalar;
	private MouseListener doorPlaceListener;
	private MouseMotionListener doorLocationListener;
	private ArrayList<Point> points;
	private Door doorHolder;
	private ArrayList<Line> lines;
	
	public DoorPanel(int scalar, ArrayList<Point> points) {
		setOpaque(false);
		setLocation(0, 0);
		setSize(new Dimension(800, 450));
		
		doors = new HashSet<>();
		
		doorWidth = 30;
		doorHeight = 6;
		
		this.points = points;
		this.scalar = scalar;
	}
	
	public ArrayList<Line> getLines() {
		ArrayList<Line> lines = new ArrayList<>();
		
		for(int i = 0;i<points.size();i++) {
			Line newLine = new Line(points.get(i), points.get((i + 1) % points.size()));

			lines.add(newLine);
		}
		
		return lines;
	}
	
	public void addDoor() {
		lines = getLines();
		doorHolder = getDoorFromMouse(new Point(0, 0));
		doors.add(doorHolder);
		
		doorLocationListener = new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				doors.remove(doorHolder);
				doorHolder = getDoorFromMouse(new Point(e.getX(), e.getY()));
				doors.add(doorHolder);
				SwingUtilities.invokeLater(Main::repaintWindow);
				//System.out.println(doorHolder);
			}
		
		};
		
		doorPlaceListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				doors.add(doorHolder);
				
				removeListeners();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		addMouseMotionListener(doorLocationListener);
		addMouseListener(doorPlaceListener);
	}
	
	private void removeListeners() {
		removeMouseMotionListener(doorLocationListener);
		removeMouseListener(doorPlaceListener);
	}
	
	private Door getDoorFromMouse(Point m) {
		ArrayList<Double> distances = new ArrayList<>();
		
		for(Line line : lines) {
			double d = line.getDistanceFromPoint(m);
			distances.add(d);
		}
		
		ArrayList<Integer> sorted = new ArrayList<>();
		
		for(int i = 0;i<distances.size();i++) {
			int smallest = -1;
			for(int j = 0;j<distances.size();j++) {
				if(sorted.contains(j) || smallest != -1 && (distances.get(smallest) < distances.get(j))) continue;
				smallest = j;
			}
			sorted.add(smallest);
		}
		
		for(int i = 0;i<sorted.size();i++) {
			Vector closestPoint = getClosestPointOnLine(m, lines.get(sorted.get(i)));
			//System.out.println(closestPoint);
			//System.out.println(lines.get(sorted.get(i)));
			
			Point startPoint = lines.get(sorted.get(i)).getP1();
			Point nextPoint = lines.get(sorted.get(i)).getP2();
			
			//System.out.println(closestPoint + " | " + startPoint + " | " + nextPoint);
			
			if(vectorBetweenPoints(closestPoint, startPoint, nextPoint)) {
				double smallestPointDistance = Double.MAX_VALUE;
				int index = 0;
				
				for(int j = 0;j<points.size();j++) {
					Point point = points.get(j);
					double distance = Vector.pointDistance(point, m);
					
					if(distance < smallestPointDistance) {
						smallestPointDistance = distance;
						index = j;
					}
				}
				
				int index2 = (index + 1) % points.size();
				
				if(smallestPointDistance < closestPoint.distanceTo(m)) {
					return new Door(new Vector(startPoint), new Vector(nextPoint), 0);
				}
				
				double scalar = closestPoint.distanceTo(startPoint) / Vector.pointDistance(startPoint, nextPoint);
				
				return new Door(new Vector(startPoint), new Vector(nextPoint), scalar);
			}
		}
		
		double smallestPointDistance = Double.MAX_VALUE;
		int index = 0;
		
		for(int j = 0;j<points.size();j++) {
			Point point = points.get(j);
			double distance = Vector.pointDistance(point, m);
			
			if(distance < smallestPointDistance) {
				smallestPointDistance = distance;
				index = j;
			}
		}
		
		return new Door(new Vector(points.get(index)), new Vector(points.get((index + 1) % points.size())), 0);
	}
	
	private boolean vectorBetweenPoints(Vector v, Point p1, Point p2) {
		Line line1 = new Line(p1, p2);
		VectorLine line2 = new VectorLine(new Vector(p1), v);
		double d = v.distanceTo(p1) + v.distanceTo(p2);
//		System.out.println("START");
//		System.out.println(v);
//		System.out.println(p1 + " | " + p2);
//		System.out.println(Vector.pointDistance(p1, p2) + " | " + d);
//		System.out.println("END");
		
		return d  == Vector.pointDistance(p1, p2);
	}
	
	private Vector getClosestPointOnLine(Point point, Line line) {
		//System.out.println("START");
		//System.out.println(point);
		//System.out.println(line);
		
		double x = (Math.pow(line.getB(), 2) * point.x - line.getC() * line.getA() - point.y * line.getB() * line.getA()) / (Math.pow(line.getB(), 2) + Math.pow(line.getA(), 2));
		double y = line.getB() != 0 ? - (line.getA() * x + line.getC()) / line.getB() : point.y;
		
		//System.out.println(x);
		//System.out.println(y);
		//System.out.println("END");
		
		return new Vector(x, y);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//EXAMPLE DON'T FORGET
		paint.setColor(doorColor);
//		paint.rotate(3.14159 / 2, 410, 302.5);
//		paint.fillRoundRect(400, 300, 20, 5, 5, 5);
//		paint.rotate(-3.14159 / 2, 410, 302.5);
//		paint.setColor(Color.black);
//		paint.fillRoundRect(400, 300, 20, 5, 5, 5);
		
		for(Door door : doors) {
			System.out.println(door);
			//Get angle
			double x = door.getPoint2().getX() - door.getPoint1().getX();
			double y = door.getPoint2().getY() - door.getPoint1().getY();
			Vector normalized = new Vector(x, y).normalize();
			double theta = PolygonCollision.getTheta(normalized);
			
			System.out.println("START");
			System.out.println(theta);
			System.out.println(normalized);
			System.out.println("END");
			
			x *= door.getScalar();
			y *= door.getScalar();
			x += door.getPoint1().getX();
			y += door.getPoint1().getY();
			
			//System.out.println(door);
			
			int finalX = (int)(x);
			int finalY = (int)(y);
			
			//paint.fillRect(300,  300,  100,  100);
			//System.out.println(finalX + " | " + finalY);
			//paint.fillRect(finalX, finalY, 100, 100);
			
			paint.rotate(theta, finalX, finalY);
			
			finalX -= doorWidth / 2;
			finalY -= doorHeight / 2;
			
			paint.fillRoundRect(finalX, finalY, doorWidth, doorHeight, 5, 5);
			
			finalX += doorWidth / 2;
			finalY += doorHeight / 2;
			
			paint.rotate(-theta, finalX, finalY);
		}
	}
}