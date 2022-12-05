/**
 * This is the boot of the game, it handles what ui content is inside of the jframe!
 */
package _main;

import java.awt.Dimension;

import javax.swing.JFrame;

import db.DBAdaptor;
import ui.Scene;
import ui.titleScreen.TitleScreenUI;

public class _SceneManager {
	
	public static boolean isDarkMode;
	public static Scene activeScene;
	public static JFrame frame;
	public static Dimension size = new Dimension(800, 752);
	
	public static void main(String[] args) {
		_SceneManager.setScene(new TitleScreenUI());
	}
	
	/**
	 * This will set the content of the jframe
	 *
	 * @author Ethan Rees 
	 * @param scene
	 */
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
	
	/**
	 * This will set the scene to dark mode
	 *
	 * @author Ethan Rees 
	 * @param darkMode
	 */
	public static void setDarkMode(boolean darkMode) {
		isDarkMode = darkMode;
		if(activeScene != null) activeScene.setIsDarkMode(darkMode);
		
		// store preferences if logged in
		if(TitleScreenUI.loggedInPlayer != null) {
			TitleScreenUI.loggedInPlayer.setTheme(!darkMode);
			DBAdaptor.updateUser(TitleScreenUI.loggedInPlayer);
		}
	}
}
