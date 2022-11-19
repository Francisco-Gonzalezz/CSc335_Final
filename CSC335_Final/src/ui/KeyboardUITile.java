/**
 * This class holds the panel for a keyboard tile, it holds the letter and handles mouse events
 * @author Ethan Rees
 */
package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import _main.KeyStage;

public class KeyboardUITile extends JPanel implements MouseListener {
	String content;
	WordleGameBoardUI ui;
	Font font;
	boolean isHovering, isClicked;
	KeyStage stage;
	
	public KeyboardUITile(WordleGameBoardUI ui, String content, Font font) { 
		this.ui = ui;
		this.content = content;
		this.font = font;
		this.isHovering = false;
		this.isClicked = false;
		this.stage = KeyStage.Unknown;
		
		addMouseListener(this);
	}
	
	/**
	 * This will override the Jpanel's default paint event and will draw it manually
	 *
	 * @author Ethan Rees 
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		// draw the space with the background color
		g2.setColor(ui.getBackgroundColor());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		// draw the rounded rect with padding
		int padding = 3;
		g2.setColor(getBackgroundColor());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.fillRoundRect(padding, padding, getWidth() - padding*2, getHeight() - padding*2, 10, 10);

		// Draw the letter + figure out the center of the letter and draw from there
		g2.setColor(ui.getTextColor());
		g2.setFont(font);
		
		// get the width of the letter
		int width = getFontMetrics(font).stringWidth(content);
		
		// box width / 2 - letter width / 2
		int xOffset = ui.toInt(getWidth()*.5 - width*0.5);
		g2.drawString(content, xOffset, 35);
	}
	
	/**
	 * This will get the background color for the tile
	 *
	 * @author Ethan Rees 
	 */
	Color getBackgroundColor() {
		Color color = ui.getHylightColor();
		if(stage == KeyStage.NotInWord)
			return color.darker();
		if(stage == KeyStage.InWordWrongPlace)
			color =  ui.getWrongPlaceColor();
		if(stage == KeyStage.InWordRightPlace)
			color = ui.getRightPlaceColor();

		color =	color.darker();
		if(this.isHovering) color = color.brighter();
		if(this.isClicked) color = color.brighter();
		
		return color;
	}
	
	/**
	 * This will update the stage of the letter
	 *
	 * @author Ethan Rees 
	 * @param stage the new stage
	 */
	public void setStage(KeyStage stage) {
		this.stage = stage;
		repaint();
	}
	
	/**
	 * This will get the content of the key
	 *
	 * @author Ethan Rees 
	 * @return the content of the key
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * This will return the new width of the key based on the content
	 *
	 * @author Ethan Rees 
	 * @param width desired width
	 * @return new width
	 */
	public int getNewWidth(int width) {
		if(content.length() > 1)
			return ui.toInt(width * 1.5);
		return width;
	}

	/**
	 * A mouse has clicked on me!
	 * @author Ethan Rees
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		this.isClicked = true;
		
		// if we are just a letter, press that letter
		if(content.length() == 1)
			ui.addLetter(content.charAt(0));
		
		// otherwise, run the action event
		else {
			if(content.equals("ENTER"))
				ui.enterNewRow();
			else
				ui.removeLetter();
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	/**
	 * A mouse has entered my tile!
	 * @author Ethan Rees 
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		this.isHovering = true;
		repaint();
	}

	/**
	 * A mouse has left my tile!
	 * @author Ethan Rees 
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		this.isHovering = false;
		this.isClicked = false;
		repaint();
	}
}
