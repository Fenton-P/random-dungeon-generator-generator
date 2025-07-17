package widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.SwingUtilities;

import animation.Animation;
import codeGenerator.PolygonCollision;
import codeGenerator.Triangle;
import codeGenerator.Vector;
import generator.Main;
import generator.Node;
import generator.Room;
import screens.EditShapeScreen;

public class ShapeGrid extends Widget {
	private static final long serialVersionUID = -9024494870621230968L;
	
	private ArrayList<Point> points;
	private int highlight;
	private final int boxSize = 30;
	private final int pointRadius = 12;
	private Animation shadowAnimation;
	private Animation shadowAnimationShrink;
	private boolean shadowGrown;
	private boolean canEditShape;
	
	private Set<Triangle> triangles;

	public ShapeGrid(Node node, EditShapeScreen parent) {
		super();
		
		setSize(800, 450);
		setLocation(0, 0);
		setOpaque(false);
		
		points = copyOf(node.getPoints());
		centerAndScalePoints();
		
		highlight = -1;
		canEditShape = node.isRoot();
		
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if(highlight == -1 || !canEditShape) return;
				
				points.get(highlight).setLocation(new Point((e.getPoint().x + 15) / 30 * 30, (15 + e.getPoint().y) / 30 * 30));
				parent.getFakes(highlight).setLocation(new Point(points.get(highlight).x - pointRadius / 2, points.get(highlight).y - pointRadius / 2));
				//triangles = PolygonCollision.triangulateRoom(new Room(new Node(points), 1));
				
				SwingUtilities.invokeLater(Main::repaintWindow);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if(!canEditShape) return;
				
				Point mouseLocation = e.getPoint();
				
				int prevHighlight = highlight;
				highlight = -1;
				for(int i = 0;i<points.size();i++) {
					Point point = points.get(i);
					if(!isTouching(point, mouseLocation)) continue;
					
					highlight = i;
				}
				
				if(prevHighlight != highlight) SwingUtilities.invokeLater(() -> repaint());
			}
			
		});
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!shadowGrown) return;
				shadowGrown = false;
				
				shadowAnimationShrink = new Animation(parent.getFakes(highlight), "onReleaseShrink", 0, -3, "number", .25);
				shadowAnimation.cancelAnimation();
				shadowAnimationShrink.playAnimation();
				
				SwingUtilities.invokeLater(Main::repaintWindow);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if(shadowGrown) return;
				if(highlight == -1) return;
				
				shadowAnimation = new Animation(parent.getFakes(highlight), "onDragShadowGrow", -3, 0, "number", .25);
				if(shadowAnimationShrink != null) shadowAnimationShrink.cancelAnimation();
				shadowAnimation.playAnimation();
				shadowGrown = true;
				
				SwingUtilities.invokeLater(Main::repaintWindow);
			}
			
		});
	}
	
	public boolean isTouching(Point p1, Point p2) {
		int xDiff = p1.x - p2.x;
		int yDiff = p1.y - p2.y;
		int sum = xDiff * xDiff + yDiff * yDiff;
		
		return sum <= pointRadius * pointRadius / 4;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public ArrayList<Point> getScaledPoints() {
		ArrayList<Point> tempPoints = new ArrayList<>();
		
		for(Point point : points) {
			tempPoints.add(new Point(point));
			
			tempPoints.get(tempPoints.size() - 1).x /= 30;
			tempPoints.get(tempPoints.size() - 1).y /= 30;
		}
		
		return tempPoints;
	}
	
	public ArrayList<Point> copyOf(ArrayList<Point> points) {
		ArrayList<Point> newPoints = new ArrayList<>();
		
		for(Point point : points) {
			newPoints.add(new Point(point));
		}
		
		return newPoints;
	}
	
	private void centerAndScalePoints() {
		int avgx = 0;
		int avgy = 0;
		
		for(Point point : points) {
			avgx += point.x;
			avgy += point.y;
			
			point.x *= boxSize;
			point.y *= boxSize;
		}
		
		avgx /= points.size();
		avgy /= points.size();
		
		int centerX = 800 / 2 / boxSize;
		int centerY = 450 / 2 / boxSize;
		
		for(Point point : points) {
			point.x += boxSize * (centerX - avgx);
			point.y += boxSize * (centerY - avgy);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(Point point : points) {
        	drawGridAround(point, paint);
        }
        
        Polygon pointPolygon = new Polygon();
        for(Point point : points) {
        	pointPolygon.addPoint(point.x, point.y);
        }
        //if(triangles != null) drawTriangles(paint);
        paint.setPaint(Color.black);
        paint.setStroke(new BasicStroke(canEditShape ? 3f : 2f));
        paint.drawPolygon(pointPolygon);
        
        int index = 0;
        for(Point point : points) {
        	if(canEditShape) {
	        	paint.setPaint(Color.black);
	        	paint.fillArc(point.x - pointRadius/2, point.y - pointRadius/2, pointRadius, pointRadius, 0, 360);
	        	if(index != highlight) paint.setPaint(Color.white);
	        	else paint.setPaint(new Color(225, 225, 225));
	        	paint.setStroke(new BasicStroke(2f));
	        	paint.drawArc(point.x - pointRadius / 3, point.y - pointRadius / 3, pointRadius / 3 * 2, pointRadius / 3 * 2, 0, 360);
	        	index++;
        	} else {
        		paint.setPaint(Color.black);
        		paint.fillArc(point.x - pointRadius/4, point.y - pointRadius/4, pointRadius/2, pointRadius/2, 0, 360);
        	}
        }
	}
	
	private void drawTriangles(Graphics2D paint) {
		for(Triangle t : triangles) {
			paint.setPaint(Color.gray);
			paint.setStroke(new BasicStroke(2f));
			
			Polygon polygon= new Polygon();
			
			Point avgPoint = new Point(0, 0);
			
			for(Point point : points) {
				avgPoint.x += point.x;
				avgPoint.y += point.y;
			}
			
			avgPoint.x /= points.size();
			avgPoint.y /= points.size();
			
			for(Vector p : t.getPoints()) {
				polygon.addPoint((int) p.getX() + avgPoint.x, (int) p.getY() + avgPoint.y);
			}
			
			paint.drawPolygon(polygon);
		}
	}
	
	private void drawGridAround(Point point, Graphics2D paint) {
		int numRows = 450  / boxSize;
		int numCols = 800 / boxSize;
		float radius = 100;
		float centerX = point.x;
		float centerY = point.y;
		float[] dist = {0.0f, 1.0f};
        Color[] colors = {
            new Color(0, 0, 0, 180), // Bright center
            new Color(0, 0, 0, 0)    // Transparent edge
        };
		
		RadialGradientPaint paint2 = new RadialGradientPaint(
            new Point2D.Float(centerX, centerY), radius, dist, colors
        );
        
        paint.setPaint(paint2);
		
		for(int i = 0;i<numCols + 1;i++) {
			paint.drawLine(i * boxSize, 0, i * boxSize, 450);
		}
		
		for(int i = 0;i<numRows + 1;i++) {
			paint.drawLine(0, i * boxSize, 800, i * boxSize);
		}
	}
	
	public int getScale() {
		return boxSize;
	}
}
