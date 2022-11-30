/**
 * This is a panel that drops down with alternating string and component options
 * @author Ethan Rees
 */
package ui.titleScreen;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ui.Scene;

public class DropdownPanel extends JPanel {
	Scene scene;
	public String[] labels;
	JLabel[] labelComponents;
	public JComponent[] components;
	Dimension size;
	DropDownPanelCloseAction action;
	public JButton closeButton;
	String buttonText;
	
	public DropdownPanel(Scene scene, String[] labels, JComponent[] components, Dimension size, String closeButtonText) {
		this.scene = scene;
		this.labels = labels;
		this.components = components;
		this.size = size;
		this.buttonText = closeButtonText;

		// create the contents of the panel
		int fieldHeight = (size.height-10) / (labels.length * 2 + 1);
		labelComponents = new JLabel[labels.length];
		
		for(int i = 0; i < labels.length*2; i++) {
			if(i % 2 == 0) {
				labelComponents[i/2] = new JLabel(labels[i/2]);
				labelComponents[i/2].setBounds(5, fieldHeight*i, size.width, fieldHeight);
				add(labelComponents[i/2]);
			} else {
				components[i/2].setBounds(5, fieldHeight*i, size.width-10, fieldHeight);
				add(components[i/2]);
			}
		}

		// create the sign in button
		closeButton = new JButton(buttonText);
		closeButton.setBounds(10, fieldHeight*labels.length*2+5, size.width-20, fieldHeight);
		closeButton.addActionListener(l -> {
			if(action != null) action.actionPerformed(null);
		});
		add(closeButton);
	}
	
	/**
	 * This will add an action for the button close
	 *
	 * @author Ethan Rees 
	 * @param action the callback
	 */
	public void addCloseButtonAction(DropDownPanelCloseAction action) {
		this.action = action;
	}
	
	/**
	 * This will update the theme of the panel
	 *
	 * @author Ethan Rees
	 */
	public void themeChange() {
		for(int i = 0; i < labelComponents.length; i++)
			scene.setColor(labelComponents[i], 0, 0);
		for(int i = 0; i < components.length; i++)
			scene.setColor(components[i], 3, 0);
		
		scene.setColor(closeButton, 1, 1);
	}
	
	/**
	 * This will set the text of the close button
	 *
	 * @author Ethan Rees 
	 * @param text new text
	 */
	public void setCloseButtonText(String text) {
		buttonText = text;
		closeButton.setText(text);
	}
	
	/**
	 * Get components of type at index
	 *
	 * @author Ethan Rees 
	 * @param index index
	 * @return the desired component
	 */
	public JTextField getTextComponent(int index) {
		return (JTextField)components[index];
	}
	public JTextArea getTextAreaComponent(int index) {
		return (JTextArea)components[index];
	}
}