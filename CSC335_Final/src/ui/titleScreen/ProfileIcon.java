/**
 * This class represents the profile icon that is custom drawn.
 * @author Ethan Rees
 */
package ui.titleScreen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ProfileIcon extends JButton implements MouseListener {

	char character;
	boolean hovering;
	TitleScreenUI ui;
	Font font;
	
	public ProfileIcon(TitleScreenUI ui) {
		addMouseListener(this);
		this.ui = ui;
		this.character = 'w';
		this.font = new Font("Arial", Font.BOLD, 32);
	}
	
	/**
	 * Set the character
	 *
	 * @author Ethan Rees 
	 * @param character new character
	 */
	public void setCharacter(char character) {
		this.character = character;
	}
	
	/*
	 * Override the paint method
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(ui.getBackgroundColor());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		int padding = 3;
		Color color = getColor();
		if(hovering) color = color.brighter();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw the circle
		g2.setColor(color);
		g2.setStroke(new BasicStroke(3));
		g2.fillOval(padding, padding, getWidth()-padding*2, getHeight()-padding*2);
		g2.setColor(color.darker());
		g2.drawOval(padding, padding, getWidth()-padding*2, getHeight()-padding*2);
		
		
		// draw the text
		String toDraw = (character + "").toUpperCase();
		Rectangle2D textBounds = getFontMetrics(font).getStringBounds(toDraw, g2);
		g2.setFont(font);
		g2.setColor(ui.getTextColor());
		g2.drawString(toDraw, (int)( getWidth()*0.5 - textBounds.getWidth()*0.5), (int)(textBounds.getHeight()*1.05));
	}
	
	/**
	 * This will get a color based on the hash of the the character
	 *
	 * @author Ethan Rees 
	 * @return
	 */
	Color getColor() {
		int hash = (character + "").hashCode();
		return new Color(Color.HSBtoRGB((hash/(float)100 % 1), 1, 0.9f));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		hovering = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		hovering = false;
	}
}
