package widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import animation.Animation;
import generator.Main;

public class Widget extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int shadowDepth;
	protected Color textColor;
	private boolean shadowHidden;
	protected Font semiBold;
	protected Font bold;
	protected Font regular;
	private float fontSize;
	private Runnable onClickRunnable;
	private JComponent tiedComponent;
	
	public Widget() {
		super();
		
		setLayout(null);
		shadowDepth = 0;
		textColor = new Color(45, 45, 45);
		setForeground(textColor);
		setShadowHidden(false);
		fontSize = 18f;
		
		try {
		    Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./src/fonts/Poppins/Poppins-Bold.ttf")).deriveFont(fontSize);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(customFont);
		    bold = customFont;
		    
		    Font customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("./src/fonts/Poppins/Poppins-SemiBold.ttf")).deriveFont(fontSize);
		    GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge2.registerFont(customFont2);
		    semiBold = customFont2;
		    
		    Font customFont3 = Font.createFont(Font.TRUETYPE_FONT, new File("./src/fonts/Poppins/Poppins-Regular.ttf")).deriveFont(fontSize);
		    GraphicsEnvironment ge3 = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge3.registerFont(customFont3);
		    regular = customFont3;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FONT FAILED");
		    bold = new Font("Arial", Font.BOLD, 20);
		    semiBold = new Font("Arial", Font.BOLD, 20);
		    regular = new Font("Arial", Font.BOLD, 20);
		}
		
		setFont(bold);
	}
	
	public void setShadowDepth(int shadowDepth) {
		this.shadowDepth = shadowDepth;
		Main.repaintWindow();
	}
	
	public int getShadowDepth() {
		return shadowDepth;
	}
	
	public float getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
		setFont(getFont().deriveFont(fontSize));
	}
	
	public void setUnderlined(boolean val) {
		Map<TextAttribute, Object> attributes = new HashMap<>(getFont().getAttributes());
		if(val) attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		else attributes.remove(TextAttribute.UNDERLINE);
		setFont(getFont().deriveFont(attributes));
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
	
	public void simulateClick(MouseEvent e) {
		new Thread(onClickRunnable).run();
	}

	public boolean isShadowHidden() {
		return shadowHidden;
	}

	public void setShadowHidden(boolean shadowHidden) {
		this.shadowHidden = shadowHidden;
	}
	
	@Override
	public void setLocation(Point point) {
		super.setLocation(point);
		
		if(tiedComponent == null) return;
		
		tiedComponent.setLocation(point);
	}
	
	public void tieLocationTo(JComponent component) {
		tiedComponent = component;
		
//		addPropertyChangeListener("location", evt -> {
//		    component.setLocation(getLocation());
//		    SwingUtilities.invokeLater(() -> component.repaint());
//		});
	}
	
	public void setCenterBox(Dimension centerBox) {
		this.putClientProperty("centerBox", centerBox);
		Animation.centerComponent(this);
		SwingUtilities.invokeLater(this::repaint);
	}
	
	public void setCenterBox(int width, int height) {
		setCenterBox(new Dimension(width, height));
	}
}
