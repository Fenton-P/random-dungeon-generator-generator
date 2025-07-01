package generator;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import animation.Animation;
import widgets.Widget;

public class WindowHandler extends JPanel {
	private static final long serialVersionUID = 1L;
	private static boolean isPlaying;
	
	private Runnable onClickRunnable;
	
	public static <T extends WindowHandler> void putWindow(Class<T> windowClass) {
		if(isPlaying) return;
		isPlaying = true;
		
		Widget holder = new Widget();
		holder.tieLocationTo(Main.getMainWindow());
		Animation windowAnimation = new Animation(holder, "slideOut", new Point(0, 0), new Point(800, 0), "location", 1, "accelerate");
		
		windowAnimation.onComplete(() -> {
			SwingUtilities.invokeLater(() -> {
				try {
					Main.removeAll();
					
					T newWindow = windowClass.getDeclaredConstructor().newInstance();
					newWindow.setLocation(-800, 0);
					
					Widget holder2 = new Widget();
					holder2.setLocation(-800, 0);
					holder2.tieLocationTo(newWindow);
					
					Main.render(newWindow);
					Main.makeVisible();
					
					Animation windowAnimationIn = new Animation(holder2, "slideIn", new Point(-800, 0), new Point(0, 0), "location", 1, "deaccelerate");
					
					windowAnimationIn.onComplete(() -> isPlaying = false);
					
					windowAnimationIn.playAnimation();
					
					Main.centerWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});
		
		windowAnimation.playAnimation();
	}
	
	protected WindowHandler() {
		setPreferredSize(new Dimension(800, 450));
	}
	
	public void setOnClick(Runnable runnable) {
		onClickRunnable = runnable;
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				new Thread(onClickRunnable).run();
			}
			
		});
	}
	
	public void setOnClickOrDrag(Runnable runnable) {
		setOnClick(runnable);

		addMouseMotionListener(new MouseMotionAdapter() {
			
			@Override
			public void mouseDragged(MouseEvent e) {
				new Thread(onClickRunnable).run();
			}
			
		});
	}
}
	