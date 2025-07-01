package generator;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import animation.Animation;
import widgets.InfoPanel;
import widgets.RoomNodeWidget;
import widgets.Widget;

public class SelectedNode {
	private RoomNodeWidget selectedNode;
	private Point textLocation;
	private String nodeName;
	private Widget shadowWidget;
	private Animation downAndOutAnimation;
	private Animation downAnimation;
	private Animation downAndOutAnimationShadow;
	private Animation downAnimationShadow;
	private static boolean once = false;
	private boolean x;
	
	public void setInfoPanel(InfoPanel infoPanel) {
		shadowWidget = new Widget();
		shadowWidget.setBounds(infoPanel.getBounds());
		shadowWidget.setShadowHidden(true);
		shadowWidget.setShadowDepth(10);
		
		downAndOutAnimation = new Animation(infoPanel, "onDespawn", new Point(infoPanel.getLocation()), new Point(infoPanel.getX(), 500), "location", 1, "accelerate");
		downAndOutAnimationShadow = new Animation(shadowWidget, "onDespawn", new Point(infoPanel.getLocation()), new Point(infoPanel.getX(), 500), "location", 1, "accelerate");
		
		downAnimation = new Animation(infoPanel, "onSpawn", new Point(600, -350), new Point(infoPanel.getX(), 10), "location", 1, "deaccelerate");
		downAnimationShadow = new Animation(shadowWidget, "onSpawn", new Point(600, -350), new Point(infoPanel.getX(), 10), "location", 1, "deaccelerate");
		
		x = false;
		
		downAndOutAnimation.onComplete(() -> {
			if(!x) selectedNode = null;
			SwingUtilities.invokeLater(() -> shadowWidget.setShadowHidden(true));
			
			if(x) SwingUtilities.invokeLater(this::continueSetSelectedNode);
			else setOnce(false);
		});
	}
	
	public RoomNodeWidget getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(RoomNodeWidget selectedNode) {
		if(isOnce()) return;
		if(selectedNode.equals(this.selectedNode)) return;
		
		setOnce(true);
		
		this.selectedNode = selectedNode;
		x = true;
		
		if(!shadowWidget.isShadowHidden()) {
			downAndOutAnimation.playAnimation();
			downAndOutAnimationShadow.playAnimation();
		} else continueSetSelectedNode();
	}
	
	public void continueSetSelectedNode() {
		shadowWidget.setShadowHidden(false);
		
		String text = selectedNode.getRoomName();
		
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Dummy image
		Graphics2D g2d = img.createGraphics(); 
		FontMetrics metrics = g2d.getFontMetrics(selectedNode.getFont()); 
		int textWidth = metrics.stringWidth(text);
		int textHeight = metrics.getHeight();
		
		if(textWidth > 175) {
			text = text.substring(0, 10) + "...";
			textWidth = metrics.stringWidth(text);
			textHeight = metrics.getHeight();
		}
		
		g2d.dispose();
		setNodeName(text);
		
		setTextLocation(new Point((180 - textWidth) / 2, 10 + textHeight));
		
		downAnimation.onComplete(() -> setOnce(false));
		
		downAnimationShadow.playAnimation();
		downAnimation.playAnimation();
	}
	
	public Widget getShadowedWidget() {
		return shadowWidget;
	}
	
	public void setTextLocation(Point location) {
		textLocation = location;
	}
	
	public void clearSelectedNode() {
		if(isOnce()) return;
		setOnce(true);
		
		x = false;
		
		downAndOutAnimation.playAnimation();
		downAndOutAnimationShadow.playAnimation();
	}
	
	public Point getTextLocation() {
		return textLocation;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public static boolean isOnce() {
		return once;
	}

	public static void setOnce(boolean once) {
		SelectedNode.once = once;
	}
}
