package screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import codeGenerator.PolygonCollision;
import generator.Main;
import generator.Node;
import generator.Room;
import generator.WindowHandler;

public class Generate extends WindowHandler {
	private static final long serialVersionUID = -4622964542876776200L;
	private int dungeonSize;
	private Set<Node> nodes;
	private int scalar;
	private Set<Room> rooms;
	
	public Generate() {
		super();
		
		dungeonSize = 5;
		scalar = 10;
		nodes = Main.getGeneratableNodes();
		
		setOpaque(true);
		setBackground(Color.white);
		
		rooms = new HashSet<>();
		
		Room baseRoom = new Room(Main.getBaseNode(), scalar);
		baseRoom.setLocation(400, 225);
		
		rooms.add(baseRoom);
	}
	
	public Node getRandomNode(Set<Node> nodes) {
		int size = nodes.size();
		int index = (int) (Math.random() * size);
		
		ArrayList<Node> nodeList = new ArrayList<>();
		
		for(Node node : nodes) {
			nodeList.add(node);
		}
		
		return nodeList.get(index);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		paint.setStroke(new BasicStroke(dungeonSize / 5 * 2));
		
		for(Room room : rooms) {
			drawRoom(room, paint);
		}
	}
	
	public void drawRoom(Room room, Graphics2D paint) {
		Polygon shapePolygon = new Polygon();
		
		for(Point point : room.getPoints()) {
			shapePolygon.addPoint(point.x + room.getLocation().x, point.y + room.getLocation().y);
		}
		
		paint.drawPolygon(shapePolygon);
	}
}
