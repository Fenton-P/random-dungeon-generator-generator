package codeGenerator;

public class HallwayNode {
	private Vector location;
	private HallwayNode next;
	
	public HallwayNode(Vector location) {
		this.location = location;
	}
	
	public HallwayNode(Vector location, HallwayNode next) {
		this.location = location;
		
		setNext(next);
	}

	public Vector getLocation() {
		return location;
	}

	public void setLocation(Vector location) {
		this.location = location;
	}

	public HallwayNode getNext() {
		return next;
	}

	public void setNext(HallwayNode next) {
		this.next = next;
	}
}
