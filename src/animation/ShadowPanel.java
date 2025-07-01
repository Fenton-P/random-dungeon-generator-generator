package animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import widgets.Widget;

public class ShadowPanel extends JPanel {
	private static final long serialVersionUID = -1354393369885479410L;
	
	private Set<Widget> objectShadows;
	private int shadowDepth;
	private Color shadowColor;
	
	public ShadowPanel(Dimension d) {
		objectShadows = new HashSet<>();
		shadowDepth = 5;
		shadowColor = new Color(0, 0, 0, 50);
		
		setSize(d);
		setOpaque(false);
	}
	
	public void addShadow(Widget...rectangles) {
		for(Widget rectangle : rectangles) {
			objectShadows.add(rectangle);
		}
	}
	
	public <T extends Widget> void addAll(Set<T> widgets) {
		for(Widget widget : widgets) {
			objectShadows.add(widget);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges

		paint.setColor(shadowColor);
		for(Widget shadow : objectShadows) {
			if(shadow.isShadowHidden()) continue;
			paint.fillRoundRect(shadow.getX() + shadowDepth + shadow.getShadowDepth(), shadow.getY() + shadowDepth + shadow.getShadowDepth(), shadow.getWidth(), shadow.getHeight(), 20, 20);
		}
	}
}
