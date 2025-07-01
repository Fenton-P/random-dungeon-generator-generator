package widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import animation.Animation;

public class CheckBox extends Widget {
	private static final long serialVersionUID = -7251256855452557837L;
	
	private boolean isChecked;
	private int boxSize;
	private InfoPanel infoPanel;
	
	public CheckBox(InfoPanel infoPanel) {
		super();
		
		this.infoPanel = infoPanel;
		setOpaque(false);
		setChecked(false);
		boxSize = 12;
		setBackground(new Color(250, 250, 250));
		setForeground(new Color(250, 250, 250));
		setBounds(0, 0, boxSize, boxSize);
		
		setOnClick(() -> {
			if(!(infoPanel == null || infoPanel.canDraw())) return;
			setChecked(!isChecked());
			if(isChecked()) setForeground(textColor);
			SwingUtilities.invokeLater(this::repaint);
		});
		
		int alter = 100;
		
		Animation darkenAnimation = new Animation(this, "onEnterDarken", new Color(250, 250, 250), new Color(200, 200, 200), "textColor", .5);
		Animation lightenAnimation = new Animation(this, "onExitLighten", new Color(200, 200, 200), new Color(250, 250, 250), "textColor", .5);
		
		Animation fadeAnimation = new Animation(this, "onEnterFade", textColor, new Color(textColor.getRed() + alter, textColor.getGreen() + alter, textColor.getBlue() + alter), "textColor", .5);
		Animation fadeInAnimation = new Animation(this, "onExitReappear",new Color(textColor.getRed() + alter, textColor.getGreen() + alter, textColor.getBlue() + alter), textColor, "textColor", .5);
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	if(isChecked()) {
            		fadeInAnimation.cancelAnimation();
            		fadeAnimation.playAnimation();
	            } else {
	            	lightenAnimation.cancelAnimation();
	            	darkenAnimation.playAnimation();
            	}
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	if(!isChecked()) {
            		darkenAnimation.cancelAnimation();
                	lightenAnimation.playAnimation();
	            } else {
	            	fadeAnimation.cancelAnimation();
	            	fadeInAnimation.playAnimation();
            	}
            }
        });
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(!(infoPanel == null || infoPanel.canDraw())) return;
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		paint.setColor(getBackground());
		paint.fillRect(0, 0, boxSize, boxSize);
		
		paint.setColor(textColor);
		paint.setStroke(new BasicStroke(2f));
		paint.drawRect(0, 0, boxSize, boxSize);
		
		//if(!isChecked()) return;
		
		paint.setColor(getForeground());
		int offset = 3;
		paint.drawLine(offset, offset, boxSize - offset, boxSize - offset);
		paint.drawLine(boxSize - offset, offset, offset, boxSize - offset);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public int getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(int boxSize) {
		this.boxSize = boxSize;
		setBounds(0, 0, boxSize, boxSize);
		SwingUtilities.invokeLater(() -> repaint());
	}
}
