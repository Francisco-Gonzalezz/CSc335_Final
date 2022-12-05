/**
 * This is one of the letter panels on the UI, it holds the letter and handles drawing
 * @author Ethan Rees
 */
package ui.wordleGameBoard;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

import _main.KeyStage;
import ui.Scene;
import ui.UIAnimationReciever;
import ui.UIAnimator;

public class GameBoardUITile extends JPanel implements UIAnimationReciever {
	Scene ui;
	Font font;
	
	String letterToDisplay;
	// -1 = unknown
	// 0 = not in word
	// 1 = in word, wrong spot
	// 2 = in word, right spot
	KeyStage stage;
	KeyStage setOnAnimationStage;
	
	double animationBounceOffset;
	double animationShakeInfluence, animationShakeOffset;
	double animationFadeIn;
	double animationBackgroundColorLerp = 0;
	
	int characterOffset;
	
	public GameBoardUITile(Scene ui, Font font) {
		this.ui = ui;
		this.font = font;
		this.letterToDisplay = null;
		this.stage = KeyStage.Unknown;
		this.setOnAnimationStage = KeyStage.Unknown;
		this.animationBounceOffset = 0;
		this.animationShakeInfluence = 0;
		this.animationShakeOffset = 0;
		this.animationFadeIn = 0;
		this.characterOffset = 55;
		this.animationBackgroundColorLerp = 0;
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
		int padding = 2 + ui.toInt(animationBounceOffset) - ui.toInt(animationFadeIn);
		
		g2.setColor(ui.getBackgroundColor());
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		// draw the background box
		g2.setColor(UIAnimator.lerpColor(ui.contrastColor(getMainColor(), 3), ui.getBackgroundColor(), animationBackgroundColorLerp));
		g2.fillRoundRect(padding+ui.toInt(animationShakeOffset), padding, getWidth() - padding*2, getHeight() - padding*2, 3, 3);
		
		// draw the outline
		Color borderColor = UIAnimator.lerpColor(getMainColor(), Color.red, animationShakeInfluence);
		borderColor = UIAnimator.lerpColor(borderColor, ui.getBackgroundColor(), animationBackgroundColorLerp);
		
		g2.setColor(borderColor);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(padding+ui.toInt(animationShakeOffset), padding, getWidth() - padding*2, getHeight() - padding*2, 3, 3);
		
		// draw the character
		if(letterToDisplay != null) {
			Color textColor = ui.getTextColor();
			textColor = UIAnimator.lerpColor(textColor, ui.getBackgroundColor(), animationBounceOffset);
			textColor = UIAnimator.lerpColor(textColor, Color.red, animationShakeInfluence);
			textColor = UIAnimator.lerpColor(textColor, ui.getBackgroundColor(), animationBackgroundColorLerp);
			g2.setColor(textColor);
			g2.setFont(font);
			
			// figure out the center of the letter and draw from there
			
			// get the width of the letter
			int width = getFontMetrics(font).stringWidth(letterToDisplay);
			
			// box width / 2 - letter width / 2
			int xOffset = ui.toInt(getWidth()*.5 - width*0.5)+ui.toInt(animationShakeOffset);
			g2.drawString(letterToDisplay, xOffset, characterOffset);
		}
	}
	
	/**
	 * This will set the character
	 *
	 * @author Ethan Rees 
	 * @param character new character
	 */
	public void setCharacter(String character) {
		if(letterToDisplay == null) {
			UIAnimator.beginAnimation(this, "popIn", 0, 0.2);
		}
		this.letterToDisplay = character;
		repaint();
	}
	
	/**
	 * This will just get the character
	 *
	 * @author Ethan Rees 
	 * @return the character
	 */
	public String getCharacter() {
		return this.letterToDisplay;
	}
	
	/**
	 * This will remove the character from the key
	 *
	 * @author Ethan Rees
	 */
	public void removeCharacter() {
		this.letterToDisplay = null;
		repaint();
	}
	
	/**
	 * This will set the stage of the ui tile
	 *
	 * @author Ethan Rees 
	 * @param stage The new stage for the key
	 * @param waitForAnimationUpdate If true, the gui wont be updated until the bounce animation is complete
	 */
	public void setStage(KeyStage stage, boolean waitForAnimationUpdate) {
		if(waitForAnimationUpdate) {
			this.setOnAnimationStage = stage;
		} else {
			this.stage = stage;
			this.setOnAnimationStage = KeyStage.Unknown;
			repaint();
		}
	}
	
	/**
	 * Change the letter offset
	 *
	 * @author Ethan Rees 
	 */
	public void setHeightOffset(int offset) {
		this.characterOffset = offset;
	}
	
	/**
	 * This will return the main color
	 *
	 * @author Ethan Rees 
	 * @return the main color
	 */
	Color getMainColor() {
		if(stage == KeyStage.InWordWrongPlace)
			return ui.getWrongPlaceColor();
		if(stage == KeyStage.InWordRightPlace)
			return ui.getRightPlaceColor();

		if(letterToDisplay == null)
			return ui.getHylightColor().darker();
		
		return ui.getHylightColor();
	}
	
	/**
	 * This is called every tick update, it updates the animation fields accordingly
	 *
	 * @author Ethan Rees 
	 */
	@Override
	public void onAnimationTick(String animationName, double time, double percentageComplete) {

		// handle the bounce animation, uses the equation: (abs(2x-1)-1)^2 * 10
		if(animationName.equals("bounce")) {
			this.animationBounceOffset = Math.pow(Math.abs(2 * percentageComplete - 1) - 1, 2)*10;
			if(percentageComplete > 0.5 && setOnAnimationStage != KeyStage.Unknown)
				setStage(setOnAnimationStage, false);
		}
		
		// handles the shake animation, uses the equation: max(0,-4x(x-1)) * cos(35x)
		if(animationName.equals("shake")) {
			this.animationShakeInfluence = Math.max(0, -4 * percentageComplete * (percentageComplete - 1));
			double shake = Math.cos(35 * percentageComplete);
			this.animationShakeOffset = animationShakeInfluence * shake * 3;
		}
		
		// handles the pop in animation, just lerps between 2 and 0
		if(animationName.equals("popIn")) {
			this.animationFadeIn = UIAnimator.lerp(2, 0, percentageComplete);
		}

		// handle the game over bounce
		if(animationName.equals("gameOver")) {
			animationBackgroundColorLerp = UIAnimator.lerp(0, 0.8, percentageComplete);
		}
		repaint();
	}

	/**
	 * This is called once an animation finishes, just make sure the values are reset properly
	 *
	 * @author Ethan Rees 
	 */
	@Override
	public void onAnimationFinish(String animationName) {
		if(animationName.equals("bounce"))
			this.animationBounceOffset = 0;
		
		if(animationName.equals("shake")) {
			this.animationShakeInfluence = 0;
			this.animationShakeOffset = 0;
		}
		
		if(animationName.equals("popIn"))
			this.animationFadeIn = 0;
		
		repaint();
	}
}
