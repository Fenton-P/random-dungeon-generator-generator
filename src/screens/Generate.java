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

import codeGenerator.HallwayNode;
import codeGenerator.PolygonCollision;
import codeGenerator.Vector;
import generator.Door;
import generator.Main;
import generator.Node;
import generator.Room;
import generator.WindowHandler;
import widgets.DoorPanel;

public class Generate extends WindowHandler {
	private static final long serialVersionUID = -4622964542876776200L;
	private int dungeonSize;
	private Set<Node> nodes;
	private int scalar;
	private Set<Room> rooms;
	private Color doorColor = new Color(255, 211, 99);
	private Set<HallwayNode> hallways;
	private int avgLength = 10;
	
	public Generate() {
		super();
		
		dungeonSize = 2;
		scalar = 10;
		nodes = Main.getGeneratableNodes();
		
		setOpaque(true);
		setBackground(Color.white);
		
		rooms = new HashSet<>();
		//hallways = new HashSet<>();
		
		Room baseRoom = new Room(Main.getBaseNode(), scalar);
		baseRoom.setLocation(400, 225);
		Set<Door> baseRoomDoors = new HashSet<>();
		
		for(Door d : baseRoom.getTotalDoors()) {
			Door newDoor = new Door(d.getPoint1(), d.getPoint2(), d.getScalar(), baseRoom);
			baseRoomDoors.add(newDoor);
		}
		
		baseRoom.setParentDoors(new HashSet<Door>());
		baseRoom.setDoors(baseRoomDoors);
		
		rooms.add(baseRoom);
		
		addRandomRoom();
		
		//placeRooms();
		//addHallways(rooms);
		//addRandomRoom();
	}
	
	private void addHallways(Set<Room> rooms) {
		ArrayList<Door> doors = getTotalDoors(rooms);
		
		while(doors.size() > 1) {
			int index1 = (int) (Math.random() * doors.size());
			int index2 = (int) (Math.random() * doors.size());
			
			if(doors.size() == 1) return;
			if(index2 == index1) index2 = (index2 + 1) % doors.size();
			
			Vector room1Position = applyRoomTransformation(doors.get(index1), doors.get(index1).getParentRoom());
			Vector room2Position = applyRoomTransformation(doors.get(index2), doors.get(index2).getParentRoom());
			
			HallwayNode hallway = createNewHallway(room1Position, room2Position);
			
			hallways.add(hallway);
			
			if(index1 < index2) {
				doors.remove(index2);
				doors.remove(index1);
				continue;
			}
			
			doors.remove(index1);
			doors.remove(index2);
		}
	}
	
	private HallwayNode createNewHallway(Vector p1, Vector p2) {
		HallwayNode hallway = new HallwayNode(p1);
		
//		int length = 3 + (int) (Math.random() * (avgLength - 3));
//		
//		HallwayNode current = hallway;
//		for(int i = 0;i<length - 1;i++) {
//			int x = (int) (Math.random() * 800);
//			int y = (int) (Math.random() * 450);
//			
//			HallwayNode next = new HallwayNode(new Vector(x, y));
//			
//			if(!checkLineCollision(current.getLocation(), next.getLocation())) {
//				current.setNext(next);
//			}
//			
//			current = current.getNext();
//		}
//		
//		current.setNext(new HallwayNode(p2));
		
		int d = 50;
		double distance = p1.distanceTo(p2);
		HallwayNode current = hallway;
		double r = 50;
		
		while(distance > d) {
			double x = (int) (Math.random() * r - r / 2) + p2.getX();
			double y = (int) (Math.random() * r - r / 2) + p2.getY();
			
			Vector potential = new Vector(x, y);
			double distance2 = potential.distanceTo(current.getLocation());
			if(distance2 == 0) continue;
			double scale = d / distance2;
			potential.setX(potential.getX() * scale);
			potential.setY(potential.getY() * scale);
			
			if(pointInRooms(potential)) continue;
			current.setNext(new HallwayNode(potential));
			current = current.getNext();
		}
		
		return hallway;
	}
	
	private boolean pointInRooms(Vector v) {
		for(Room room : rooms) {
			if(PolygonCollision.checkPointAgainstRoom(room, v)) return true;
		}
		
		return false;
	}
	
	private boolean checkLineCollision(Vector v1,  Vector v2) {
		return false;
	}
	
	private Vector applyRoomTransformation(Door d, Room r) {
		Vector p1 = r.getPositionOfDoor(d);
		Vector newLocation = new Vector(p1.getX(), p1.getY());
		
		newLocation.setX(newLocation.getX() + r.getLocation().x);
		newLocation.setY(newLocation.getY() + r.getLocation().y);
		
		return newLocation;
	}
	
	private ArrayList<Door> getTotalDoors(Set<Room> rooms) {
		ArrayList<Door> doors = new ArrayList<>();
		
		for(Room r : rooms) {
			doors.addAll(r.getAvailableDoors());
		}
		
		return doors;
	}
	
	private void placeRooms() {
		for(int i = 0;i<dungeonSize;i++) {
			Room newRoom = new Room(getRandomNode(nodes), scalar);
			
			Set<Door> doors = new HashSet<>();
			for(Door door : newRoom.getDoors()) {
				doors.add(new Door(door.getPoint1(), door.getPoint2(), door.getScalar(), newRoom));
			}
			
			newRoom.setDoors(doors);
			
			placeRandomLocation(newRoom);
			rooms.add(newRoom);
		}
	}
	
	private void placeRandomLocation(Room room) {
		Point location = getRandomPoint();
		room.setLocation(location);
		
		while(isCollidingWith(room, rooms)) {
			location = getRandomPoint();
			room.setLocation(location);
		}
	}
	
	private boolean isCollidingWith(Room room, Set<Room> rooms) {
		for(Room r : rooms) {
			if(PolygonCollision.isColliding(room, r)) return true;
		}
		
		return false;
	}
	
	private Point getRandomPoint() {
		return new Point((int) (Math.random() * 800), (int) (Math.random() * 450));
	}
	
	private void addRandomRoom() {
		ArrayList<Door> doors = getTotalDoors(rooms);
		int index = (int) (Math.random() * doors.size());
		
		Door door = doors.get(index);
		Room room = door.getParentRoom();
		
		Set<Node> possibleNodes = Main.getGeneratableNodes();
		
		for(Node node : possibleNodes) {
			if(node.getTotalDoors().size() == 0) {
				possibleNodes.remove(node);
			}
		}
		
		Room roomToAdd = new Room(getRandomNode(possibleNodes), scalar);
		
		Set<Door> roomDoors = new HashSet<>();
		for(Door d : roomToAdd.getDoors()) {
			roomDoors.add(new Door(d.getPoint1(), d.getPoint2(), d.getScalar(), roomToAdd));
		}
		
		roomToAdd.setDoors(roomDoors);
		
		Door doorToAttatch = getRandomDoorFromRoom(roomToAdd);
		
		Vector pointToAttatchTo = room.getPositionOfDoor(door);
		pointToAttatchTo.setX(pointToAttatchTo.getX() + room.getLocation().x);
		pointToAttatchTo.setY(pointToAttatchTo.getY() + room.getLocation().y);
		
		double angle = room.getAngleOfDoor(door);
		double angle2 = roomToAdd.getAngleOfDoor(doorToAttatch);
		double finalAngle = angle - angle2 - Math.PI;
		
		Point pointLocation = new Point((int)pointToAttatchTo.getX(), (int)pointToAttatchTo.getY());
		roomToAdd.setLocation(pointLocation);
		roomToAdd.setRotation(finalAngle);
		
		Vector location = roomToAdd.getPositionOfDoor(door);
		location.setX(-location.getX() + roomToAdd.getLocation().x);
		location.setY(-location.getY() + roomToAdd.getLocation().y);
		
		roomToAdd.setLocation(new Point((int) location.getX(), (int) location.getY()));
		
		rooms.add(roomToAdd);
	}
	
	private Door getRandomDoorFromRoom(Room room) {
		ArrayList<Door> doors = room.getAvailableDoors();
		
		if(doors.size() == 0) {
			System.out.println("No available Doors");
			return null;
		}
		
		int index = (int) (Math.random() * doors.size());
		return doors.get(index);
	}
	
	private ArrayList<Room> getPossibleRooms(Set<Room> rooms) {
		ArrayList<Room> possibleRooms = new ArrayList<>();
		
		for(Room room : rooms) {
			if(room.getAvailableDoors().size() <= 0) continue;
			System.out.println("TEST");
			possibleRooms.add(room);
		}
		
		return possibleRooms;
	}
	
	private ArrayList<Door> getPossibleDoors(Set<Room> rooms) {
		ArrayList<Door> possibleDoors = new ArrayList<>();
		
		for(Room room : rooms) {
			for(int i = 0;i<room.getTotalDoors().size();i++) {
				//TEMPORARY
				possibleDoors.add(room.getTotalDoors().get(i));
			}
		}
		
		return possibleDoors;
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
		
		for(Room room : rooms) {
			drawRoom(room, paint);
		}
		
//		for(HallwayNode hallway : hallways) {
//			drawHallway(hallway, paint);
//		}
	}
	
	public void drawHallway(HallwayNode hallway, Graphics2D paint) {
		if(hallway.getNext() == null) return;
		
		Vector point1 = hallway.getLocation();
		Vector point2 = hallway.getNext().getLocation();
		
		drawLine(point1, point2, paint);
		
		drawHallway(hallway.getNext(), paint);
	}
	
	public void drawLine(Vector p1, Vector p2, Graphics2D paint) {
		paint.setStroke(new BasicStroke(5f));
		paint.setColor(Color.black);
		paint.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
	}
	
	public void drawRoom(Room room, Graphics2D paint) {
		Polygon shapePolygon = new Polygon();
		
		paint.setStroke(new BasicStroke(2f));
		paint.setColor(Color.black);
		for(Point point : room.getPoints()) {
			shapePolygon.addPoint(point.x + room.getLocation().x, point.y + room.getLocation().y);
		}
		
		paint.drawPolygon(shapePolygon);
		
		paint.setColor(doorColor);
		for(Door door : room.getBaseNode().getTotalDoors()) {
			DoorPanel.drawDoor(paint, door, room.getPoints(), room.getLocation().x, room.getLocation().y, 12, 3);
		}
	}
}