package screens;

import java.awt.Color;
import java.awt.Dimension;

import animation.ShadowPanel;
import generator.WindowHandler;
import widgets.ModernButton;

public class OpeningScreen extends WindowHandler {
	private static final long serialVersionUID = -3108841471689984158L;
	private ShadowPanel shadowPanel;
	
	public OpeningScreen() {
		super();
		
		shadowPanel = new ShadowPanel(getPreferredSize());
		
		setBackground(Color.white);
		setOpaque(true);
		setLayout(null);
		
		ModernButton newDungeon = new ModernButton("Create New Dungeon");
		newDungeon.setCenterBox(new Dimension(800, 100));
		newDungeon.setOnClick(() -> WindowHandler.putWindow(NodeScreen.class));
		
		shadowPanel.addShadow(newDungeon);
		
		add(newDungeon);
		add(shadowPanel);
	}
}