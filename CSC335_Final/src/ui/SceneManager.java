package ui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class SceneManager {
	
	public static boolean isDarkMode;
	public static Scene activeScene;
	public static JFrame frame;
	public static Dimension size = new Dimension(800, 752);
	
	public static void setScene(Scene scene) {
		if(frame == null) {
			isDarkMode = true;
			frame = new JFrame();
			frame.setTitle("Wordle - Csc335");
			
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setPreferredSize(size);
			frame.setResizable(false);
		} else {
			frame.getContentPane().removeAll();
		}
		
		activeScene = scene;
		scene.setIsDarkMode(isDarkMode);
		
		frame.getContentPane().add(scene);
		frame.pack();
		scene.requestFocusInWindow();
		scene.requestFocus();
	}
	
	public static void setDarkMode(boolean darkMode) {
		isDarkMode = darkMode;
		if(activeScene != null) activeScene.setIsDarkMode(darkMode);
	}
}
