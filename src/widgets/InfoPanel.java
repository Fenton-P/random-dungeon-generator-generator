package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import generator.Main;
import generator.SelectedNode;
import generator.WindowHandler;
import screens.AddNode;
import screens.EditShapeScreen;

public class InfoPanel extends Widget {
	private static final long serialVersionUID = 5387711434761185480L;
	private SelectedNode selectedNode;
	private final Color fillColor = new Color(235, 235, 235);
	private Font secondaryFont;
	private Font thirdFont;
	private LinkText editShape;
	private CheckBox rotatedChildren;
	private ModernButton addChildNode;
	
	public InfoPanel(SelectedNode selectedNode) {
		super();
		
		setOpaque(false);
		setLayout(null);
		this.selectedNode = selectedNode;
		setBounds(600, 10, 180, 300);
		setBackground(fillColor);
		
		addChildNode = new ModernButton("Add Child Node");
		addChildNode.setFont(semiBold.deriveFont(15f));
		addChildNode.updateSize();
		addChildNode.setCenterBox(new Dimension(getWidth(), getHeight() * 2 - 80));
		addChildNode.setCanDraw(false);
		
		addChildNode.setOnClick(() -> {
			Main.setCreatingRootNode(false);
			Main.setSelectedNode(selectedNode.getNodeName());
			WindowHandler.putWindow(AddNode.class);
		});
		
		add(addChildNode);
		
		editShape = new LinkText("Edit Shape", this);
		editShape.setLocation(new Point(70, 75 - editShape.getHeight()));
		editShape.setOnClickAction(() -> {
			Main.setSelectedNode(selectedNode.getNodeName());
			WindowHandler.putWindow(EditShapeScreen.class);
		});
		add(editShape);
		
		rotatedChildren = new CheckBox(this);
		rotatedChildren.setLocation(new Point(137, 90));
		add(rotatedChildren);
		
		setOnClickOrDrag(() -> {
		});
		
		secondaryFont = getFont().deriveFont(18f);
		thirdFont = semiBold.deriveFont(12f);
	}
	
	public boolean canDraw() {
		return selectedNode.getSelectedNode() != null;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		
		if(!canDraw()) return;
		addChildNode.setCanDraw(true);
		
		String nodeName = selectedNode.getNodeName();
		Point textLocation = selectedNode.getTextLocation();
		//RoomNodeWidget roomNode = selectedNode.getSelectedNode();
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges
		
		paint.setColor(getBackground());
		paint.fillRoundRect(0, 0, 180, 300, 20, 20);
		
		paint.setFont(secondaryFont);
		paint.setColor(getForeground());
		paint.drawString(nodeName, textLocation.x, textLocation.y);
		
		paint.setFont(thirdFont);
		paint.drawString("Shape:", 10, 70);
		paint.drawString("Rotated Children:", 10, 100);
	}
}
