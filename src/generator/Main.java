package generator;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import screens.OpeningScreen;

public class Main {
	
	private static JFrame windowFrame;
	private static WindowHandler currentWindow;
	private static List<Image> icons;
	private static Set<Node> nodes;
	private static String selectedNodeName;
	private static boolean creatingRootNode;

	public static void main(String[] args) {
		icons = new ArrayList<>();
		icons.add(Toolkit.getDefaultToolkit().getImage("./src/assets/logo_128.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage("./src/assets/logo_64.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage("./src/assets/logo_32.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage("./src/assets/logo_16.png"));
		
		SwingUtilities.invokeLater(Main::initWindowFrame);
		
		WindowHandler.putWindow(OpeningScreen.class);
		
		creatingRootNode = true;
		
		nodes = new HashSet<>();
		nodes.add(new Node("Base Node"));
	}
	
	public static Node getBaseNode() {
		for(Node node : nodes) {
			if(node.getNodeName().equals("Base Node")) return node;
		}
		
		return (Node) nodes.toArray()[0];
	}
	
	public static void setCreatingRootNode(boolean val) {
		creatingRootNode = val;
	}
	
	public static Set<Node> getNodes() {
		return nodes;
	}
	
	public static void addNewNode(Node node) {
		if(checkAllNodeNames(node.getNodeName()) != null) return;
		
		nodes.add(node);
	}
	
	public static int getNumberNodes() {
		int count = 0;
		
		for(Node node : nodes) {
			count += 1 + node.countChildren();
		}
		
		return count;
	}
	
	private static void initWindowFrame() {
		windowFrame = new JFrame("Random Dungeon Generator");
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowFrame.getContentPane().setBackground(Color.white);
		windowFrame.setIconImages(icons);
	}
	
	public static void render(WindowHandler window) {
		currentWindow = window;
		windowFrame.add(window);
		windowFrame.pack();
		window.setLocation(new Point(-800, 0));
	}
	
	public static void makeVisible() {
		windowFrame.setVisible(true);
	}
	
	public static void centerWindow() {
		windowFrame.setLocationRelativeTo(null);
	}
	
	public static void removeAll() {
		if(currentWindow == null) return;
		windowFrame.remove(currentWindow);
	}
	
	public static void repaintWindow() {
		windowFrame.repaint();
	}
	
	public static WindowHandler getMainWindow() {
		return currentWindow;
	}
	
	public static JFrame getWindowFrame() {
		return windowFrame;
	}
	
	public static void setSelectedNode(String nodeName) {
		selectedNodeName = nodeName;
	}
	
	public static Node getSelectedNode() {
		return checkAllNodeNames(selectedNodeName);
	}
	
	public static boolean isCreatingRootNode() {
		return creatingRootNode;
	}
	
	public static Node checkAllNodeNames(String nodeName)  {
		for(Node node : nodes) {
			if(node.getNodeName().equals(nodeName)) return node;
			
			Node child = checkAllNodeNames(nodeName, node.getChildNodes());
			if(child != null) return child;
		}
		
		return null;
	}
	
	public static Node checkAllNodeNames(String nodeName, Set<Node> nodeList) {
		for(Node node : nodeList) {
			if(node.getNodeName().equals(nodeName)) return node;
			
			Node child = checkAllNodeNames(nodeName, node.getChildNodes());
			if(child != null) return child;
		}
		
		return null;
	}
	
	public static Set<Node> getGeneratableNodes() {
		Set<Node> nodes = new HashSet<>();
		
		addNodesToSet(nodes, Main.nodes);
		
		return nodes;
	}
	
	private static void addNodesToSet(Set<Node> nodes, Set<Node> nodeList) {
		for(Node node : nodeList) {
			if(node.isGeneratable()) {
				nodes.add(node);
			}
			
			addNodesToSet(nodes, node.getChildNodes());
		}
	}
}

// 2855 lines of code