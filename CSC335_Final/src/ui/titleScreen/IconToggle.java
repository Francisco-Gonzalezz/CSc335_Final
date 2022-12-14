/**
 * This class represents an icon iconToggle, used on the title screen
 * 
 * @author Ethan Rees
 */
package ui.titleScreen;

import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class IconToggle extends JButton {
	public boolean value;
	public ImageIcon ifTrueIcon, ifFalseIcon;
	
	public IconToggle(String ifTrue, String ifFalse, boolean defaultValue, int width, int height) {
		this.value = defaultValue;
		this.ifTrueIcon = new ImageIcon(new ImageIcon(ifTrue).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		this.ifFalseIcon = new ImageIcon(new ImageIcon(ifFalse).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
		updateIcon();
	}
	
	/**
	 * This will set the value of the icon and update the icon
	 *
	 * @author Ethan Rees 
	 * @param icon true or false
	 */
	public void setValue(boolean icon) {
		this.value = icon;
		updateIcon();
	}
	
	/**
	 * This helper updates the image and makes sure its pretty
	 *
	 * @author Ethan Rees
	 */
	public void updateIcon() {
		setIcon(value ? ifTrueIcon : ifFalseIcon);
		repaint();
		setBackground(null);
		setForeground(null);
		setBorder(BorderFactory.createEmptyBorder());
	    setContentAreaFilled(true);
	}
}
