package screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import animation.ShadowPanel;
import generator.Door;
import generator.Main;
import generator.WindowHandler;
import widgets.DoorPanel;
import widgets.ModernButton;
import widgets.ShapeGrid;
import widgets.Widget;

public class EditShapeScreen extends WindowHandler {
	private static final long serialVersionUID = -9192554214164385476L;

	private ModernButton back;
	private ModernButton finish;
	private ShadowPanel shadowPanel;
	private ShapeGrid shapeGrid;
	private ArrayList<Point> points;
	private Set<Door> doors;
	private ArrayList<Widget> fakes;
	private ModernButton addDoor;
	private DoorPanel doorPanel;
	
	public EditShapeScreen() {
		super();
		
		shadowPanel = new ShadowPanel(getPreferredSize());
		
		fakes = new ArrayList<>();
		doors = new HashSet<>();
		
		setLayout(null);
		setOpaque(true);
		setBackground(Color.white);
		
		back = new ModernButton("Back");
		back.setCenterBox(new Dimension(110, 80));
		back.setOnClick(() -> WindowHandler.putWindow(NodeScreen.class));
		
		finish = new ModernButton("Save");
		finish.setCenterBox(new Dimension(1490, 80));
		
		finish.setOnClick(() -> {
			Main.getSelectedNode().setPoints(shapeGrid.getScaledPoints());
			Main.getSelectedNode().setDoors(doorPanel.getDoors());
			WindowHandler.putWindow(NodeScreen.class);
		});
		
		addDoor = new ModernButton("Add Door");
		addDoor.setCenterBox(new Dimension(150, 800));
		
		shapeGrid = new ShapeGrid(Main.getSelectedNode(), this);
		doorPanel = new DoorPanel(shapeGrid.getPoints(), Main.getSelectedNode());
		
		addDoor.setOnClick(() -> {
			doorPanel.addDoor();
		});
		
		points = shapeGrid.getPoints();
		
		if(Main.getSelectedNode().isRoot()) {
			for(Point point : points) {
				Widget fake = new Widget();
				fake.setBounds(point.x - 6, point.y - 6, 12, 12);
				fake.setShadowDepth(-3);
				fakes.add(fake);
				shadowPanel.addShadow(fake);
			}
		}
		
		shadowPanel.addShadow(back, finish, addDoor);
		
		add(doorPanel);
		add(back);
		add(addDoor);
		add(finish);
		add(shapeGrid);
		add(shadowPanel);
	}
	
	public void alterShadowDepth(int fakeId, int shadowDepth) {
		fakes.get(fakeId).setShadowDepth(shadowDepth);
		SwingUtilities.invokeLater(this::repaint);
	}
	
	public Widget getFakes(int index) {
		return fakes.get(index);
	}
}
