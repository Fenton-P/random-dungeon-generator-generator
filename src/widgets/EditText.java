package widgets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import generator.Main;

public class EditText extends Widget {
	private static final long serialVersionUID = -36019314984066908L;
	
	private String currentText;
	private boolean currentlySelected;
	private int timeBetweenBlinks;
	private Timer blinkTimer;
	private boolean showing;
	private int maxLength;
	private KeyListener keyListener;
	
	public EditText(String defaultText) {
		setOpaque(false);
		
		setSize(200, 30);
		setBackground(Color.white);
		setFocusable(true);
		
		maxLength = 15;
		currentText = defaultText;
		timeBetweenBlinks = 500;
		showing = false;
		
		keyListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				char keyChar = e.getKeyChar();
				if(currentText.length() > maxLength && e.getKeyCode() != 8) return;
				
				if(!(Character.isDigit(keyChar) || Character.isAlphabetic(keyChar) || keyChar == ' ')) {
					if(e.getKeyCode() == 8 && currentText.length() > 0) {
						currentText = currentText.substring(0, currentText.length() - 1);
						SwingUtilities.invokeLater(() -> repaint());
					}
					return;
				}
				
				currentText += keyChar;
				SwingUtilities.invokeLater(() -> repaint());
			}
			
		};
		
		setOnClickOrDrag(() -> setCurrentlySelected(true));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D paint = (Graphics2D) g;
		paint.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		paint.setColor(getBackground());
		paint.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		
		paint.setColor(textColor);
		paint.setStroke(new BasicStroke(2f));
		paint.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		
		paint.setFont(regular);
		paint.drawString(currentText + (showing && currentlySelected ? "|" : ""), 5, 25);
	}

	public boolean isCurrentlySelected() {
		return currentlySelected;
	}

	public void setCurrentlySelected(boolean currentlySelected) {
		if(currentlySelected == this.currentlySelected) return;
		this.currentlySelected = currentlySelected;
		
		if(currentlySelected) {
			blinkTimer = new Timer(timeBetweenBlinks, e -> {
	            showing = !showing;
	            SwingUtilities.invokeLater(this::repaint);
	        });
			
			Main.getWindowFrame().addMouseListener(new MouseAdapter() {
				@Override
			    public void mouseClicked(MouseEvent e) {
			        if(getBounds().contains(e.getPoint())) return;
			        setCurrentlySelected(false);
			        Main.getWindowFrame().removeMouseListener(this);
			        
			        SwingUtilities.invokeLater(() -> repaint());
			    }
			});
			
			this.requestFocusInWindow();
			this.addKeyListener(keyListener);
			
			blinkTimer.start();
		} else {
			this.removeKeyListener(keyListener);
			
			showing = false;
			blinkTimer.stop();
			blinkTimer = null;
			
			repaint();
		}
	}
	
	public String getCurrentText() {
		return currentText;
	}
}
