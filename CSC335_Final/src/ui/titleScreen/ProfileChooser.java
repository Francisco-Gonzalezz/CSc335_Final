/**
 * This class will create a profile chooser ui system
 * 
 * @author Ethan Rees
 */
package ui.titleScreen;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProfileChooser extends JPanel {
	String name;
	TitleScreenUI title;
	
	JButton signIn;
	JPanel signCreds;
	
	JLabel usernameLabel, passwordLabel;
	JTextField usernameField, passwordField;
	
	JButton signInButton;
	
	boolean isLoggedIn;
	
	public ProfileChooser(TitleScreenUI title, Dimension size) {
		this.name = null;
		this.title = title;
		this.isLoggedIn = false;
		
		setOpaque(false);
		setLayout(null);
		
		// create the sign in button
		int signInWidth = 128;
		int signInHeight = 48;
		signIn = new JButton();
		signIn.setBounds(size.width - signInWidth, 0, signInWidth, signInHeight);
		signIn.addActionListener(l -> {
			if(isLoggedIn)
				title.logOutRequest();
			else
				setPopupOpen(!signCreds.isVisible());
		});
		add(signIn);
		
		// create the sign in creds panel
		signCreds = new JPanel();
		signCreds.setBounds(0, signInHeight+3, size.width, size.height - 52);
		signCreds.setLayout(null);
		add(signCreds);
		
		// setup UI
		createSignCredsPanelContents(signCreds.getSize());
		setPopupOpen(false);
		setLoggedIn(false);
	}
	
	/**
	 * This method will fill in the contents of the sign in creds panel
	 *
	 * @author Ethan Rees 
	 */
	void createSignCredsPanelContents(Dimension size) {
		int fieldHeight = (size.height-10) / 5;
		
		// create the username label
		usernameLabel = new JLabel("Username:");
		usernameLabel.setBounds(5, 0, size.width, fieldHeight);
		signCreds.add(usernameLabel);
		
		// create the username field
		usernameField = new JTextField();
		usernameField.setBounds(5, fieldHeight, size.width-10, fieldHeight);
		signCreds.add(usernameField);

		// create the password label
		passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(5, fieldHeight*2, size.width, fieldHeight);
		signCreds.add(passwordLabel);

		// create the password field
		passwordField = new JTextField();
		passwordField.setBounds(5, fieldHeight*3, size.width-10, fieldHeight);
		signCreds.add(passwordField);
		
		// create the sign in button
		signInButton = new JButton("");
		signInButton.setBounds(10, fieldHeight*4+5, size.width-20, fieldHeight);
		signInButton.addActionListener(l -> {
			signInButton.setText("...");
			title.loggedInRequest(usernameField.getText(), passwordField.getText());
		});
		signCreds.add(signInButton);
	}
	
	/**
	 * This method will update the colors of the chooser, this is prob
	 * called by the panel's parent
	 *
	 * @author Ethan Rees
	 */
	public void onThemeChanged() {
		setColor(signIn, 3, 2);
		setColor(signCreds, 2, 2);
		setColor(usernameField, 3, 0);
		setColor(passwordField, 3, 0);
		setColor(signInButton, 1, 1);
		usernameLabel.setForeground(title.getTextColor());
		passwordLabel.setForeground(title.getTextColor());
	}
	
	/**
	 * This will set if the system should consider the player logged in
	 * 
	 * @author Ethan Rees
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.isLoggedIn = loggedIn;
		signIn.setText(isLoggedIn ? "Log Out" : "Sign In / Sign Up");
	}
	
	/**
	 * This will set if the pop out is open or not
	 *
	 * @author Ethan Rees 
	 */
	public void setPopupOpen(boolean open) {
		signInButton.setText("Sign In / Sign Up");
		signCreds.setVisible(open);
	}
	
	/**
	 * This helper method helps set component colors
	 *
	 * @author Ethan Rees 
	 * @param component The component
	 * @param backgroundContrast How much background constrast should there be
	 * @param borderSize The size of the border
	 */
	void setColor(JComponent component, int backgroundContrast, int borderSize) {
		component.setBackground(title.contrastColor(title.getHylightColor().darker(), backgroundContrast));
		component.setForeground(title.getTextColor());
		component.setBorder(BorderFactory.createLineBorder(title.getHylightColor(), borderSize));
	}
}
