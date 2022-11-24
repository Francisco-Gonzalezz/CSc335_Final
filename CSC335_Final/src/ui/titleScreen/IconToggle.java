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
	
		addActionListener(l -> {
			this.value = !this.value;
			updateIcon();
		});

		updateIcon();
	}
	
	public void updateIcon() {
		setIcon(value ? ifTrueIcon : ifFalseIcon);
		repaint();
		setBackground(null);
		setForeground(null);
		setBorder(BorderFactory.createEmptyBorder());
	    setContentAreaFilled(true);
	}
}
