package generator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import codeGenerator.Vector;

public class Node {
	private Set<Node> childNodes;
	private Node parentNode;
	private String nodeName;
	private ArrayList<Point> points;
	private boolean isRootNode;
	private Set<Door> doors;
	private Set<Door> parentDoors;
	private boolean generatable;
	
	public Node(ArrayList<Point> points) {
		this.setNodeName("HI");
		childNodes = new HashSet<>();
		doors = new HashSet<>();
		parentDoors = new HashSet<>();
		
		this.points = points;
		isRootNode = true;
		setGeneratable(true);
	}
	
	public Node(String nodeName) {
		this.setNodeName(nodeName);
		
		childNodes = new HashSet<>();
		
		doors = new HashSet<>();
		parentDoors = new HashSet<>();
		
		points = new ArrayList<>();
		points.add(new Point(-1, -1));
		points.add(new Point(1, -1));
		points.add(new Point(1, 1));
		points.add(new Point(-1, 1));
		
		isRootNode = true;
		setGeneratable(true);
	}
	
	public void setParentNode(Node node) {
		parentNode = node;
		
		if(parentNode == null) return;
		
		initParentDoors();
	}
	
	public Node getParentNodoe() {
		return parentNode;
	}
	
	public void addChildNode(Node node) {
		boolean duplicate = false;
		
		duplicate = Main.checkAllNodeNames(node.getNodeName()) != null;
		
		if(duplicate) return;
		
		childNodes.add(node);
		node.setPoints(points);
		node.setParentNode(this);
	}
	
	public void setRootNode(boolean value) {
		isRootNode = value;
	}
	
	public int countChildren() {
		int count = 0;
		
		for(Node node : childNodes) {
			count += 1 + node.countChildren();
		}
		
		return count;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
		
		for(Node childNode : childNodes) {
			childNode.setPoints(points);
		}
	}
	
	public boolean isRoot() {
		return isRootNode;
	}
	
	public Set<Node> getChildNodes() {
		return childNodes;
	}
	
	public void addDoor(Door door) {
		doors.add(door);
		
		for(Node childNode : childNodes) {
			childNode.addDoor(door);
		}
	}
	
	public void setDoors(Set<Door> doors) {
		this.doors = doors;
		
		for(Node node : childNodes) {
			node.setParentDoors(getTotalDoors());
		}
	}
	
	public void setParentDoors(Set<Door> doors) {
		parentDoors = doors;
	}
	
	public Set<Door> getDoors() {
		return doors;
	}
	
	public Set<Door> getParentDoors() {
		return parentDoors;
	}
	
	public Set<Door> getTotalDoors() {
		Set<Door> total = new HashSet<>();
		
		total.addAll(doors);
		total.addAll(parentDoors);
		
		return total;
	}
	
	public void initParentDoors() {
		parentDoors = parentNode.getTotalDoors();
	}

	public boolean isGeneratable() {
		return generatable;
	}

	public void setGeneratable(boolean generatable) {
		this.generatable = generatable;
	}
}
