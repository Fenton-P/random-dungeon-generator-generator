package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import animation.Animation;
import animation.ShadowPanel;
import generator.Main;
import generator.Node;
import screens.NodeScreen;

public class RoomNodeWidget extends Widget {
	private static final long serialVersionUID = -2484028080322449936L;
	
	private String roomName;
	private int padding;
	private int fontSize;
	private int textWidth;
	private int textHeight;
	private Color lightColor = new Color(225, 225, 225);
	private Color textColor = new Color(45, 45, 45);
	private Color darkColor = new Color(145, 145, 145);
	private Point relativeDistance;
	private Dimension bigSize;
	private Dimension smallSize;
	private int growAmount;
	private int smallShadow;
	private int bigShadow;
	private Node node;
	private NodeScreen parent;
	private Set<RoomNodeWidget> childNodeWidgets;
	
	public RoomNodeWidget(Node node, NodeScreen parent) {
		super();
		
		this.parent = parent;
		childNodeWidgets = new HashSet<>();
		
		this.setNode(node);
		setRoomName(node.getNodeName());
		
		setOpaque(false);
		setFontSize(20);
		
		try {
		    Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./src/fonts/Poppins/Poppins-Regular.ttf")).deriveFont(18f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(customFont);
		    setFont(customFont);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FONT FAILED");
		    setFont(new Font("Arial", Font.BOLD, 20));
		}
		
		growAmount = 20;
		
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Dummy image
		Graphics2D g2d = img.createGraphics(); 
		FontMetrics metrics = g2d.getFontMetrics(getFont()); 
		g2d.dispose();
		textWidth = metrics.stringWidth(getRoomName());
		textHeight = metrics.getHeight();
		
		setPadding(20);
		putClientProperty("centerBox", new Dimension(800, 450));
		setPreferredSize(new Dimension(2 * padding + textWidth, 2 * padding + textHeight));
		setSize(new Dimension(2 * padding + textWidth, 2 * padding + textHeight));
		setBackground(lightColor);
		setForeground(textColor);
		smallSize = getSize();
		bigSize = new Dimension(smallSize.width + growAmount, smallSize.height + growAmount);
		smallShadow = 0;
		bigShadow = 8;
		
		Animation colorDarkenAnimation = new Animation(this, "onClickDarken", lightColor, darkColor, "color", .25);
		Animation colorLightenAnimation = new Animation(this, "onReleaseLighten", darkColor, lightColor, "color", .25);
		
		Animation sizeGrowAnimation = new Animation(this, "onClickGrow", smallSize, bigSize, "size", .25);
		Animation sizeShrinkAnimation = new Animation(this, "onClickShrink", bigSize, smallSize, "size", .25);
		
		Animation shadowDepthGrowAnimation = new Animation(this, "onClickShadowGrow", smallShadow, bigShadow, "number", .25);
		Animation shadowDepthShrinkAnimation = new Animation(this, "onClickShadowShrink", bigShadow, smallShadow, "number", .25);
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	relativeDistance = new Point(e.getX() + growAmount / 2, e.getY() + growAmount / 2);
            	colorLightenAnimation.cancelAnimation();
            	colorDarkenAnimation.playAnimation();
            	
            	sizeShrinkAnimation.cancelAnimation();
            	sizeGrowAnimation.playAnimation();
            	
            	shadowDepthShrinkAnimation.cancelAnimation();
            	shadowDepthGrowAnimation.playAnimation();
            	simulateClick(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	colorDarkenAnimation.cancelAnimation();
            	colorLightenAnimation.playAnimation();
            	
            	sizeGrowAnimation.cancelAnimation();
            	sizeShrinkAnimation.playAnimation();
            	
            	shadowDepthGrowAnimation.cancelAnimation();
            	shadowDepthShrinkAnimation.playAnimation();
            }
        });
		
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if(relativeDistance == null) return;
				
				RoomNodeWidget comp = (RoomNodeWidget) e.getComponent();
				Point relativeRelativeDistance = new Point(comp.getRelativeDistance().x - e.getX(), comp.getRelativeDistance().y - e.getY());
				comp.moveBox(relativeRelativeDistance);
				comp.getParent().setComponentZOrder(comp, comp.getParent().getComponentCount() - Main.getNumberNodes() - 1);
				
				SwingUtilities.invokeLater(Main::repaintWindow);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
		});
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		SwingUtilities.invokeLater(this::repaint);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges
		
		paint.setColor(getBackground());
		paint.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		
		Point centeredText = getCenter();
		paint.setColor(getForeground());
		paint.drawString(getRoomName(), centeredText.x, centeredText.y);
	}
	
	public void initChildWidgets(ShadowPanel shadowPanel) {
		Set<Node> childNodes = node.getChildNodes();
		
		for(Node node : childNodes) {
			initChildWidget(node, shadowPanel);
		}
	}
	
	private void initChildWidget(Node node, ShadowPanel shadowPanel) {
		RoomNodeWidget childNode = new RoomNodeWidget(node, parent);
		
		shadowPanel.addShadow(childNode);
		
		parent.initNodeWidget(childNode);
		parent.add(childNode);
		
		childNodeWidgets.add(childNode);
	}
	
	private Point getCenter() {
		return new Point((int)(getWidth() / 2 - textWidth / 2), (int)(getHeight() / 2 + textHeight / 4));
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
		SwingUtilities.invokeLater(this::repaint);
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
		SwingUtilities.invokeLater(this::repaint);
	}
	
	public int getPadding() {
		return padding;
	}
	
	public Dimension getCenterBox() {
		return (Dimension)getClientProperty("centerBox");
	}
	
	public void moveBox(Point vector) {
		Dimension centerBox = getCenterBox();
		centerBox.width -= vector.x;
		centerBox.height -= vector.y;
		setCenterBox(centerBox);
	}

	public void setCenterBox(Dimension centerBox) {
		this.putClientProperty("centerBox", centerBox);
		firePropertyChange("location", new Point(0, 0), new Point(1, 1));
	}
	
	public Point getRelativeDistance() {
		return relativeDistance;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public Set<RoomNodeWidget> getChildNodes() {
		return childNodeWidgets;
	}
}