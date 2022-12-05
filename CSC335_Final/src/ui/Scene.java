/**
 * This class holds the scene type. It has a bunch of helper functions for drawing the UI
 * @author Ethan Rees
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import _main._SceneManager;
import ui.titleScreen.TitleScreenUI;

public abstract class Scene extends JPanel {
	
	protected boolean isDarkMode;
	protected JButton exitButton;
	
	/**
	 * This method will enter and exit dark mode, this shouldn't ever be called unless
	 * its called from _SceneManager!
	 *
	 * @author Ethan Rees 
	 * @param isDarkMode
	 */
	public void setIsDarkMode(boolean isDarkMode) {
		this.isDarkMode = isDarkMode;
		onThemeChange(isDarkMode);
		if(exitButton != null) {
			exitButton.setBackground((contrastColor(getHylightColor(), 4)));
			exitButton.setForeground(contrastColor(getTextColor(), 1));
			exitButton.setBorder(BorderFactory.createLineBorder(contrastColor(getHylightColor(), 2), 1));
		}
		repaint();
	}
	
	/**
	 * This button will create an exit button on the top left size of the screen!
	 * Color management is automatically handled!
	 *
	 * @author Ethan Rees
	 */
	public void createExitButton() {
		exitButton = new JButton("< exit");
		exitButton.setBounds(5, 5, 60, 40);
		exitButton.addActionListener(l -> {
			_SceneManager.setScene(new TitleScreenUI());
		});
		add(exitButton);
	}
	
	public abstract void onThemeChange(boolean isDarkMode);
	

	// -------------------
	//   COLOR MANAGEMENT
	// -------------------
	
	/**
	 * This is the border color of the tiles
	 * @author Ethan Rees
	 */
	public Color getHylightColor() {
		if(isDarkMode)
			return new Color(120, 120, 120);
		return new Color(150, 150, 150);
	}

	/**
	 * This is the background color of the page
	 * @author Ethan Rees
	 */
	public Color getBackgroundColor() {
		if(isDarkMode)
			return new Color(15, 15, 15);
		return new Color(200, 200, 200);
	}

	/**
	 * This is the color for stage 1, wrong place but right letter
	 * @author Ethan Rees
	 */
	public Color getWrongPlaceColor() {
		Color color = new Color(255, 224, 83);
		if(isDarkMode)
			return color;
		return color.darker();
	}

	/**
	 * This is the color for stage 2, right place and right letter
	 * @author Ethan Rees
	 */
	public Color getRightPlaceColor() {
		Color color = new Color(141, 255, 146);
		if(isDarkMode)
			return color;
		return color.darker();
	}

	/**
	 * This is the border color for text
	 * @author Ethan Rees
	 */
	public Color getTextColor() {
		if(isDarkMode)
			return new Color(255, 255, 255, 200);
		return new Color(90,90,90);
	}

	/**
	 * This will contrast the color based on dark/light
	 * @author Ethan Rees
	 */
	public Color contrastColor(Color color, int amount) {
		for(int i = 0; i < amount; i++) {
			if(isDarkMode)
				color = color.darker();
			else
				color = color.brighter();
		}
		return color;
	}

	/**
	 * This just rounds a double to an int, used for screen proportions
	 * @author Ethan Rees
	 */
	public int toInt(double x) {
		return (int)Math.round(x);
	}


	/**
	 * This helper method helps set component colors
	 *
	 * @author Ethan Rees 
	 * @param component The component
	 * @param backgroundContrast How much background constrast should there be
	 * @param borderSize The size of the border
	 */
	public void setColor(JComponent component, int backgroundContrast, int borderSize) {
		component.setBackground(contrastColor(getHylightColor().darker(), backgroundContrast));
		component.setForeground(getTextColor());
		component.setBorder(BorderFactory.createLineBorder(getHylightColor(), borderSize));
	}
	
	/**
	 * This helper class will calculate the X position for a item with a width to be centered
	 *
	 * @author Ethan Rees 
	 * @return
	 */
	protected int centeredWidthXOffset(Dimension size, int width) {
		return (int)(size.width * 0.5 - width*0.5);
	}

}
