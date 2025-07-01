package widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import animation.Animation;
import generator.Main;

public class AddNodeWidget extends Widget {
	private static final long serialVersionUID = -8658054094737255846L;
	
	private final Color fillColor = new Color(235, 235, 235);
	private FontMetrics metrics;
	private Font secondary;
	private EditText nodeNameInput;
	
	public AddNodeWidget() {
		setOpaque(false);
		setBackground(fillColor);
		setForeground(textColor);
		setFontSize(30);
		
		secondary = semiBold.deriveFont(20f);
		
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Dummy image
		Graphics2D g2d = img.createGraphics(); 
		metrics = g2d.getFontMetrics(getFont()); 
		g2d.dispose();
		
		setBounds(0, 0, 500, 400);
		
		nodeNameInput = new EditText("node" + Main.getNumberNodes());
		nodeNameInput.setLocation(160, 57);
		
		add(nodeNameInput);
		
		Animation.centerComponent(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		paint.setColor(getBackground());
		paint.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		
		paint.setColor(getForeground());
		paint.setFont(getFont());
		String header = "Add New Node";
		Point centerCoords = getCenterText(header);
		paint.drawString(header, centerCoords.x, centerCoords.y);
		
		paint.setFont(secondary);
		paint.drawString("Node Name: ", 10, 80);
	}
	
	public Point getCenterText(String string) {
		int width = metrics.stringWidth(string);
		
		int x = getWidth() - width;
		x /= 2;
		
		return new Point(x, 35);
	}
	
	public String getNewNodeName() {
		return nodeNameInput.getCurrentText();
	}
}