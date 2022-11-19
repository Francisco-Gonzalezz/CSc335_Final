package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class KeyboardUITile extends JPanel implements MouseListener {
	String content;
	WordleGameBoardUI ui;
	Font font;
	boolean isHovering, isClicked;
	int stage;
	public KeyboardUITile(WordleGameBoardUI ui, String content, Font font) { 
		this.ui = ui;
		this.content = content;
		this.font = font;
		this.isHovering = false;
		this.isClicked = false;
		this.stage = 0;
		
		addMouseListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setColor(ui.getBackgroundColor());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		int padding = 3;
		g2.setColor(getColor());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.fillRoundRect(padding, padding, getWidth() - padding*2, getHeight() - padding*2, 10, 10);

		g2.setColor(ui.getTextColor());
		g2.setFont(font);
		
		// figure out the center of the letter and draw from there
		
		// get the width of the letter
		int width = getFontMetrics(font).stringWidth(content);
		
		// box width / 2 - letter width / 2
		int xOffset = ui.toInt(getWidth()*.5 - width*0.5);
		g2.drawString(content, xOffset, 35);
	}
	
	Color getColor() {
		Color color = ui.getHylightColor();
		
		if(stage == 1)
			color =  ui.getWrongPlaceColor();
		if(stage == 2)
			color = ui.getRightPlaceColor();

		color =	color.darker();
		if(this.isHovering) color = color.brighter();
		if(this.isClicked) color = color.brighter();
		
		return color;
	}
	
	public void setStage(int stage) {
		this.stage = stage;
		repaint();
	}
	
	public String getContent() {
		return this.content;
	}
	
	public int widthMultiplier(int width) {
		if(content.length() > 1)
			return ui.toInt(width * 1.5);
		return width;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.isClicked = true;
		if(content.length() == 1)
			ui.addLetter(content.charAt(0));
		else {
			if(content.equals("ENTER"))
				ui.enterNewRow();
			else
				ui.removeLetter();
		}
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.isHovering = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.isHovering = false;
		this.isClicked = false;
		repaint();
	}
}
