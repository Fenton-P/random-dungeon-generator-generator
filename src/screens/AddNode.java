package screens;

import java.awt.Color;
import java.awt.Dimension;

import animation.ShadowPanel;
import generator.Main;
import generator.Node;
import generator.WindowHandler;
import widgets.AddNodeWidget;
import widgets.ModernButton;

public class AddNode extends WindowHandler {
	private static final long serialVersionUID = 2390088017097037750L;
	
	private ModernButton cancel;
	private ModernButton finish;
	private ShadowPanel shadowPanel;
	private AddNodeWidget addNodeWidget;
	
	public AddNode() {
		setOpaque(true);
		setLayout(null);
		setBackground(Color.white);
		
		shadowPanel = new ShadowPanel(getPreferredSize());
		
		cancel = new ModernButton("Cancel");
		cancel.setCenterBox(new Dimension(150, 100));
		
		cancel.setOnClick(() -> WindowHandler.putWindow(NodeScreen.class));
		
		finish = new ModernButton("Finish");
		finish.setCenterBox(new Dimension(150, 800));
		
		finish.setOnClick(() -> {
			String name = addNodeWidget.getNewNodeName();
			Node node = new Node(name);
			node.setRootNode(Main.isCreatingRootNode());
			
			if(node.isRoot()) Main.addNewNode(node);
			else Main.getSelectedNode().addChildNode(node);
			
			WindowHandler.putWindow(NodeScreen.class);
		});
		
		addNodeWidget = new AddNodeWidget();
		addNodeWidget.setCenterBox(800, 450);
		
		shadowPanel.addShadow(cancel, finish, addNodeWidget);
		
		add(cancel);
		add(finish);
		add(addNodeWidget);
		add(shadowPanel);
	}
}
