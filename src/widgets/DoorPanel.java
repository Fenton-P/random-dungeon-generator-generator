package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Set;

import codeGenerator.Vector;
import generator.Door;

public class DoorPanel extends Widget {
	private static final long serialVersionUID = 5085530429578026984L;

	private Set<Door> doors;
	private Door tempDoor;
	private final Color doorColor = new Color(255, 211, 99);
	
	public DoorPanel() {
		setOpaque(false);
		setLocation(0, 0);
		setSize(new Dimension(800, 450));
	}
	
	public void addDoor() {
		tempDoor = new Door(new Vector(0, 0));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		paint.setColor(doorColor);
		paint.rotate(3.14159 / 2, 410, 302.5);
		paint.fillRoundRect(400, 300, 20, 5, 5, 5);
		paint.rotate(-3.14159 / 2, 410, 302.5);
		paint.setColor(Color.black);
		paint.fillRoundRect(400, 300, 20, 5, 5, 5);
	}
}