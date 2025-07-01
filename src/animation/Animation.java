package animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import generator.Main;
import widgets.Widget;

public class Animation {
	private String propertyType;
	private String interpolater;
	private double timePlaying;
	private final int FPS = 60;
	private String id;
	private Widget component;
	private Thread animationThread;
	private boolean isAnimationPlaying = false;
	private Runnable onCompleteRunnable;
	
	public Animation(Widget component, String id, Object startProperty, Object endProperty, String type) {
		component.putClientProperty("start" + id, startProperty);
		component.putClientProperty("end" + id, endProperty);
		
		propertyType = type;
		interpolater = "accelerate_deccelerate";
		timePlaying = 1;
		this.id = id;
		this.component = component;
	}
	
	public Animation(Widget component, String id, Object startProperty, Object endProperty, String type, double timePlaying) {
		component.putClientProperty("start" + id, startProperty);
		component.putClientProperty("end" + id, endProperty);
		
		propertyType = type;
		interpolater = "accelerate_deccelerate";
		this.timePlaying = timePlaying;
		this.id = id;
		this.component = component;
	}
	
	public Animation(Widget component, String id, Object startProperty, Object endProperty, String type, String interpolater) {
		component.putClientProperty("start" + id, startProperty);
		component.putClientProperty("end" + id, endProperty);
		
		propertyType = type;
		this.interpolater = interpolater;
		timePlaying = 1;
		this.id = id;
		this.component = component;
	}
	
	public Animation(Widget component, String id, Object startProperty, Object endProperty, String type, double timePlaying, String interpolater) {
		component.putClientProperty("start" + id, startProperty);
		component.putClientProperty("end" + id, endProperty);
		
		propertyType = type;
		this.interpolater = interpolater;
		this.timePlaying = timePlaying;
		this.component = component;
		this.id = id;
	}
	
	public void playAnimation() {
		isAnimationPlaying = true;
		
		animationThread = new Thread(() -> {
			int totalFrames = (int) (FPS * timePlaying);
			totalFrames = totalFrames == 0 ? 1 : totalFrames;
			
			for(int i = 0;i<totalFrames + 1;i++) {
				if(animationThread == null) {
					isAnimationPlaying = false;
					break;
				}
				
				switch(propertyType) {
					case "color" -> runDeltaColorFrame(i);
					case "size" -> runDeltaSizeFrame(i);
					case "number" -> runDeltaNumberFrame(i);
					case "textColor" -> runDeltaTextColorFrame(i);
					case "location" -> runDeltaLocationFrame(i);
					default -> System.out.println("Property " + propertyType + " has not been handled");
				}
				
				try {
					Thread.sleep(1000 / FPS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			isAnimationPlaying = false;
			if(onCompleteRunnable != null) new Thread(onCompleteRunnable).run();
		});
		
		animationThread.start();
	}
	
	private void runDeltaColorFrame(int currFrame) {
		Color startColor = (Color)component.getClientProperty("start" + id);
		Color endColor = (Color)component.getClientProperty("end" + id);
		double deltaVal = (double) currFrame / (int) (timePlaying * FPS);
		deltaVal = callInterpolaters(deltaVal);
		int newRed = startColor.getRed() + (int) (deltaVal * (endColor.getRed() - startColor.getRed()));
		int newGreen = startColor.getGreen() + (int) (deltaVal * (endColor.getGreen() - startColor.getGreen()));
		int newBlue = startColor.getBlue() + (int) (deltaVal * (endColor.getBlue() - startColor.getBlue()));
		SwingUtilities.invokeLater(() -> component.setBackground(new Color(newRed, newGreen, newBlue)));
	}
	
	private void runDeltaSizeFrame(int currFrame) {
		Dimension startDimension = (Dimension)component.getClientProperty("start" + id);
		Dimension endDimension = (Dimension)component.getClientProperty("end" + id);
		double deltaVal = currFrame / timePlaying / FPS;
		deltaVal = callInterpolaters(deltaVal);
		int newWidth = startDimension.width + (int) (deltaVal * ((endDimension.width - startDimension.width)));
		int newHeight = startDimension.height + (int) (deltaVal * ((endDimension.height - startDimension.height)));
		SwingUtilities.invokeLater(() -> component.setSize(new Dimension(newWidth, newHeight)));
	}
	
	private void runDeltaLocationFrame(int currFrame) {
		Point startDimension = (Point)component.getClientProperty("start" + id);
		Point endDimension = (Point)component.getClientProperty("end" + id);
		double deltaVal = currFrame / timePlaying / FPS;
		deltaVal = callInterpolaters(deltaVal);
		int newWidth = startDimension.x + (int) (deltaVal * ((endDimension.x - startDimension.x)));
		int newHeight = startDimension.y + (int) (deltaVal * ((endDimension.y - startDimension.y)));
		SwingUtilities.invokeLater(() -> {
			component.setLocation(new Point(newWidth, newHeight));
			Main.repaintWindow();
		});
	}
	
	private void runDeltaNumberFrame(int currFrame) {
		int start = (Integer) component.getClientProperty("start" + id);
		int end = (Integer) component.getClientProperty("end" + id);
		double deltaVal = currFrame / timePlaying / FPS;
		deltaVal = callInterpolaters(deltaVal);
		int newNum = start + (int) (deltaVal * (end - start));
		SwingUtilities.invokeLater(() -> component.setShadowDepth(newNum));
	}
	
	private void runDeltaTextColorFrame(int currFrame) {
		Color startColor = (Color)component.getClientProperty("start" + id);
		Color endColor = (Color)component.getClientProperty("end" + id);
		double deltaVal = (double) currFrame / (int) (timePlaying * FPS);
		deltaVal = callInterpolaters(deltaVal);
		int newRed = startColor.getRed() + (int) (deltaVal * (endColor.getRed() - startColor.getRed()));
		int newGreen = startColor.getGreen() + (int) (deltaVal * (endColor.getGreen() - startColor.getGreen()));
		int newBlue = startColor.getBlue() + (int) (deltaVal * (endColor.getBlue() - startColor.getBlue()));
		SwingUtilities.invokeLater(() -> component.setForeground(new Color(newRed, newGreen, newBlue)));
	}
	
	public void cancelAnimation() {
		animationThread = null;
	}
	
	private double callInterpolaters(double x) {
		switch(interpolater) {
			case "accelerate_deccelerate": return  x * x * (3 - 2 * x);
			case "accelerate": return x * x * x;
			case "deaccelerate": return Math.pow(x, 1 / 2.0);
			default: return x;
		}
	}
	
	public void onComplete(Runnable runnable) {
		onCompleteRunnable = runnable;
	}
	
	public static void centerComponent(JComponent component) {
		if(component.getClientProperty("centerBox") == null) {
			//System.out.println("No center box attribute set");
			return;
		}
		
		int newX = ((Dimension)component.getClientProperty("centerBox")).width / 2 - component.getWidth() / 2;
        int newY = ((Dimension)component.getClientProperty("centerBox")).height / 2 - component.getHeight() / 2;
        component.setBounds(newX, newY, component.getWidth(), component.getHeight());
    }
	
	public boolean isPlaying() {
		return isAnimationPlaying;
	}
}