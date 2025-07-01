package widgets;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import animation.Animation;

public class LinkText extends Widget {
	private static final long serialVersionUID = -7278698737694395452L;
	
	private String text;
	private int textWidth;
	private int textHeight;
	private boolean mouseDown;
	private InfoPanel infoPanel;
	private boolean mouseInBtn;
	private Runnable runnable;
	private Color lightColor;
	
	public LinkText(String text, InfoPanel infoPanel) {
		setOpaque(false);
		setFont(regular);
		setFontSize(12);
		setUnderlined(true);
		setText(text);
		setBounds(0, 0, textWidth, textHeight+5);
		setForeground(textColor);
		this.infoPanel = infoPanel;
		
		mouseDown = false;
		mouseInBtn = false;
		
		lightColor = new Color(100, 100, 100);
		
		Animation colorLightenAnimation = new Animation(this, "onClickLighten", textColor, lightColor, "textColor", .25);
		Animation colorDarkenAnimation = new Animation(this, "onReleaseDarken", lightColor, textColor, "textColor", .25);
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	mouseDown = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	if(!mouseInBtn) return;
            	if(mouseDown) {
            		if(runnable != null) new Thread(runnable).run();
            	}
            }
        });
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	mouseInBtn = true;
            	
            	colorDarkenAnimation.cancelAnimation();
            	colorLightenAnimation.playAnimation();
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	mouseInBtn = false;
            	
            	colorLightenAnimation.cancelAnimation();
            	colorDarkenAnimation.playAnimation();
            }
        });
	}
	
	public void setOnClickAction(Runnable runnable) {
		this.runnable = runnable;
		
		setOnClick(() -> {
			if(!(infoPanel == null || infoPanel.canDraw())) return;
			new Thread(runnable).run();
		});
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Dummy image
		Graphics2D g2d = img.createGraphics(); 
		FontMetrics metrics = g2d.getFontMetrics(getFont()); 
		g2d.dispose();
		textWidth = metrics.stringWidth(getText());
		textHeight = metrics.getHeight();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(!(infoPanel == null || infoPanel.canDraw())) return;
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		paint.setColor(getForeground());
		paint.setFont(getFont());
		paint.drawString(getText(), 0, getHeight() - 5);
	}
}
