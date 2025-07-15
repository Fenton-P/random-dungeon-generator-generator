package screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import animation.Animation;
import animation.ShadowPanel;
import generator.Main;
import generator.Node;
import generator.SelectedNode;
import generator.WindowHandler;
import widgets.InfoPanel;
import widgets.ModernButton;
import widgets.RoomNodeWidget;

public class NodeScreen extends WindowHandler {
	private static final long serialVersionUID = 4117245432959222704L;
	private Set<RoomNodeWidget> roomNodeWidgets;
	private ModernButton backBtn;
	private ShadowPanel shadowPanel;
	private SelectedNode selectedNode;
	private InfoPanel infoPanel;
	private ModernButton addNewNode;
	private ModernButton generationSettings;
	private ModernButton generate;

	public NodeScreen() {
		super();
		
		setBackground(Color.white);
		setOpaque(true);
		setLayout(null);
		
		shadowPanel = new ShadowPanel(getPreferredSize());
		selectedNode = new SelectedNode();
		infoPanel = new InfoPanel(selectedNode);
		selectedNode.setInfoPanel(infoPanel);
		
		backBtn = new ModernButton("Home");
		backBtn.setCenterBox(new Dimension(150, 100));
		backBtn.setShadowDepth(10);
		
		addNewNode = new ModernButton("+");
		addNewNode.setCenterBox(new Dimension(80, 650));
		addNewNode.setShadowDepth(10);
		
		generationSettings = new ModernButton("Generation Settings");
		generationSettings.setCenterBox(new Dimension(250, 800));
		generationSettings.setShadowDepth(10);
		
		generate = new ModernButton("Generate");
		generate.setCenterBox(new Dimension(1400, 800));
		generate.setShadowDepth(10);
		
		generate.setOnClick(() -> WindowHandler.putWindow(Generate.class));
		backBtn.setOnClick(() -> WindowHandler.putWindow(OpeningScreen.class));
		
		addNewNode.setOnClick(() -> {
			Main.setCreatingRootNode(true);
			WindowHandler.putWindow(AddNode.class);
		});
		
		generationSettings.setOnClick(() -> WindowHandler.putWindow(GenerationSettings.class));
		
		roomNodeWidgets = new HashSet<>();
		
		for(Node node : Main.getNodes()) {
			roomNodeWidgets.add(new RoomNodeWidget(node, this));
		}
		
		for(RoomNodeWidget nodeWidget : roomNodeWidgets) {
			initNodeWidget(nodeWidget);
		}
		
		setOnClickOrDrag(() -> {
			selectedNode.clearSelectedNode();
			SwingUtilities.invokeLater(Main::repaintWindow);
		});
		
		shadowPanel.addShadow(backBtn, addNewNode, generationSettings, generate, selectedNode.getShadowedWidget());
		
		add(infoPanel);
		add(backBtn);
		add(generate);
		add(generationSettings);
		add(addNewNode);
		
		addAll(roomNodeWidgets);
		
		add(shadowPanel);
		
		this.setComponentZOrder(infoPanel, 0);
		this.setComponentZOrder(addNewNode, 0);
		this.setComponentZOrder(backBtn, 0);
	}
	
	private void addAll(Set<RoomNodeWidget> components) {
		for(RoomNodeWidget widget : components) {
			add(widget);
		}
	}
	
	public void initNodeWidget(RoomNodeWidget nodeWidget) {
		nodeWidget.setOnClick(() -> {
			selectedNode.setSelectedNode(nodeWidget);
		});
		
		nodeWidget.addPropertyChangeListener(evt -> {
			SwingUtilities.invokeLater(() -> {
	    		Animation.centerComponent(nodeWidget);
	    	});
		});
		
		nodeWidget.initChildWidgets(shadowPanel);
		shadowPanel.addShadow(nodeWidget);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paint.clearRect(0, 0,  800, 450);
		
		for(RoomNodeWidget roomNodeWidget : roomNodeWidgets) {
			drawCurvesFrom(roomNodeWidget, paint);
		}
	}
	
	public void drawCurvesFrom(RoomNodeWidget roomNode, Graphics2D paint) {
		for(RoomNodeWidget childNode : roomNode.getChildNodes()) {
			drawCurvesFrom(childNode, paint);
			
			CubicCurve2D c = new CubicCurve2D.Float();
			
			int curveIntensity = 20;
			
			int x1 = roomNode.getX() + roomNode.getWidth() / 2;
			int y1 = roomNode.getY() + roomNode.getHeight() / 2;
			int x2 = childNode.getX() + childNode.getWidth() / 2;
			int y2 = childNode.getY() + childNode.getHeight() / 2;
			int ctrl1Y = (y1 - x2) / curveIntensity + y2;
			int ctrl1X = (x1 + x2) / 2;
			int ctrl2Y = (curveIntensity - 1) * (y1 - y2) / curveIntensity + y2;
			int ctrl2X = (x1 + x2) / 2;
			
			c.setCurve(x1, y1, ctrl1X, ctrl1Y, ctrl2X, ctrl2Y, x2, y2);
			paint.draw(c);
		}
	}
}