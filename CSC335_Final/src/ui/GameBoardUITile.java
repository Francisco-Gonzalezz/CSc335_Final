/**
 * This is one of the letter panels on the UI, it holds the letter and handles drawing
 * @author Ethan Rees
 */
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameBoardUITile extends JPanel {
	WordleGameBoardUI ui;
	Font font;
	
	String letterToDisplay;
	// 0 = not in word
	// 1 = in word, wrong spot
	// 2 = in word, right spot
	int stage;
	
	public GameBoardUITile(WordleGameBoardUI ui, Font font) {
		this.ui = ui;
		this.font = font;
		this.letterToDisplay = null;
		this.stage = 0;
	}
	
	/**
	 * The way the panel is drawn is all custom, I didn't indent do make it all custom, it sorta just happened
	 * But basically this method overides the JPanel's draw call and implements its own
	 * 
	 * Step 1: draw a rounded rect for the background box
	 * Step 2: Draw a rounded empty rect for the outline of the box
	 * Step 3: Calculate the size of the letter, then draw it in the box
	 * 
	 * I did it this way so we could have that nice tile look without some crazy stupid swing nonsense
	 * @author Ethan Rees
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		int padding = 2;
		
		// draw the background box
		Color c = getColor().darker().darker().darker();
		g2.setColor(c);
		g2.fillRoundRect(padding, padding, getWidth() - padding*2, getHeight() - padding*2, 3, 3);
		
		// draw the outline
		g2.setColor(getColor());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(padding, padding, getWidth() - padding*2, getHeight() - padding*2, 3, 3);
		
		// draw the character
		if(letterToDisplay != null) {
			g2.setColor(ui.getTextColor());
			g2.setFont(font);
			
			// figure out the center of the letter and draw from there
			
			// get the width of the letter
			int width = getFontMetrics(font).stringWidth(letterToDisplay);
			
			// box width / 2 - letter width / 2
			int xOffset = ui.toInt(getWidth()*.5 - width*0.5);
			g2.drawString(letterToDisplay, xOffset, 55);
		}
	}
	
	public void setCharacter(char character) {
		this.letterToDisplay = character + "";
		repaint();
	}
	
	public void removeCharacter() {
		this.letterToDisplay = null;
		repaint();
	}
	
	public void setStage(int stage) {
		this.stage = stage;
	}
	
	Color getColor() {
		if(stage == 1)
			return ui.getWrongPlaceColor();
		if(stage == 2)
			return ui.getRightPlaceColor();

		if(letterToDisplay == null)
			return ui.getHylightColor().darker();
		return ui.getHylightColor();
	}
}
