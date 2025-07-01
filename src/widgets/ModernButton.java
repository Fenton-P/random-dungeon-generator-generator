package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import animation.Animation;
import generator.Main;

public class ModernButton extends Widget {
	private static final long serialVersionUID = 6209497746035630610L;
	private int textWidth;
	private int textHeight;
	private int padding;
	private final Color lightColor = new Color(156, 250, 255);
	private final Color darkColor = new Color(100, 200, 210);
	private boolean mouseInBtn;
	private int shrinkSize;
	private String text;
	private Animation shrinkAnimation;
	private Animation growAnimation;
	private boolean canDraw;

	public ModernButton(String text) {
		super();
		
		canDraw = true;
		
		setText(text);
		setBackground(lightColor);
		setOpaque(false);
		setPadding(20);
		setShrinkSize(10);
		mouseInBtn = false;
		setFont(semiBold);
		putClientProperty("centerBox", new Dimension(800, 450));
		
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Dummy image
		Graphics2D g2d = img.createGraphics(); 
		FontMetrics metrics = g2d.getFontMetrics(getFont()); 
		g2d.dispose();
		textWidth = metrics.stringWidth(getText());
		textHeight = metrics.getHeight();
		
		setPreferredSize(new Dimension(textWidth + 2 * padding, textHeight + 2 * padding));
		setSize(getPreferredSize());
		
		shrinkAnimation = new Animation(this, "onEnterShrink", getPreferredSize(), new Dimension(getPreferredSize().width - getShrinkSize(), getPreferredSize().height - getShrinkSize()), "size", .25);
		growAnimation = new Animation(this, "onExitGrow", new Dimension(getPreferredSize().width - getShrinkSize(), getPreferredSize().height - getShrinkSize()), getPreferredSize(), "size", .25);
		
		Animation colorDarkenAnimation = new Animation(this, "onClickDarken", lightColor, darkColor, "color", .25);
		Animation colorLightenAnimation = new Animation(this, "onReleaseLighten", darkColor, lightColor, "color", .25);
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	mouseInBtn = true;
            	growAnimation.cancelAnimation();
            	shrinkAnimation.playAnimation();
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	mouseInBtn = false;
            	if(colorDarkenAnimation.isPlaying() || !getBackground().equals(lightColor)) {
            		colorDarkenAnimation.cancelAnimation();
            		colorLightenAnimation.playAnimation();
            	}
            	shrinkAnimation.cancelAnimation();
            	growAnimation.playAnimation();
            }
        });
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	colorLightenAnimation.cancelAnimation();
            	colorDarkenAnimation.playAnimation();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	if(!mouseInBtn) return;
            	if(!getBackground().equals(lightColor)) {
            		simulateClick(e);
            	}
            }
        });
		
		init();
	}
	
	public void updateSize() {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Dummy image
		Graphics2D g2d = img.createGraphics(); 
		FontMetrics metrics = g2d.getFontMetrics(getFont()); 
		g2d.dispose();
		textWidth = metrics.stringWidth(getText());
		textHeight = metrics.getHeight();
		
		setPreferredSize(new Dimension(textWidth + 2 * padding, textHeight + 2 * padding));
		setSize(getPreferredSize());
		
		shrinkAnimation = new Animation(this, "onEnterShrink", getPreferredSize(), new Dimension(getPreferredSize().width - getShrinkSize(), getPreferredSize().height - getShrinkSize()), "size", .25);
		growAnimation = new Animation(this, "onExitGrow", new Dimension(getPreferredSize().width - getShrinkSize(), getPreferredSize().height - getShrinkSize()), getPreferredSize(), "size", .25);
	}
	
	public void setCanDraw(boolean val) {
		canDraw = val;
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(!canDraw) return;
		
        Graphics2D g2d = (Graphics2D) g;
        //Animation.centerComponent(this);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges
        
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        g2d.setColor(getForeground());
        g2d.setFont(getFont());
        Point centeredText = getCenter();
        g2d.drawString(getText(), centeredText.x, centeredText.y);
    }
	
	private Point getCenter() {
		return new Point((int)(getWidth() / 2 - textWidth / 2), (int)(getHeight() / 2 + textHeight / 4));
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public Dimension getCenterBox() {
		return (Dimension)getClientProperty("centerBox");
	}

	public int getShrinkSize() {
		return shrinkSize;
	}

	public void setShrinkSize(int shrinkSize) {
		this.shrinkSize = shrinkSize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void init() {
		addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(ComponentEvent e) {
		    	SwingUtilities.invokeLater(() -> {
		    		Animation.centerComponent((JComponent) e.getComponent());
		    		Main.repaintWindow();
		    	});
		    }
		});
	}
}