package ui;

import java.awt.Color;

import javax.swing.JPanel;

public abstract class Scene extends JPanel {
	protected boolean isDarkMode;
	public abstract void onThemeChange(boolean isDarkMode);
	
	public void setIsDarkMode(boolean isDarkMode) {
		this.isDarkMode = isDarkMode;
		onThemeChange(isDarkMode);
		repaint();
	}

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

}
