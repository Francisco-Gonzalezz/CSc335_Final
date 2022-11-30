/**
 * This class will create a profile chooser ui system
 * 
 * @author Ethan Rees
 */
package ui.titleScreen;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import db.DBAdaptor;
import player.Player;

public class ProfileChooser extends JPanel {
	String name;
	TitleScreenUI title;
	
	JButton signIn;
	DropdownPanel signCreds, profileInfo;
	
	boolean isLoggedIn;
	
	Dimension size;
	ProfileIcon icon;
	boolean popupOpen;
	static final int SIGNB_IN_WIDTH = 128, SIGNB_IN_HEIGHT = 53;
	
	public ProfileChooser(TitleScreenUI title, Dimension size) {
		this.size = size;
		this.name = null;
		this.title = title;
		this.isLoggedIn = false;
		
		setOpaque(false);
		setLayout(null);
		
		// create the sign in button
		signIn = new JButton();
		signIn.addActionListener(l -> {
			if(isLoggedIn)
				title.logOutRequest();
			else
				setPopupOpen(!popupOpen);
		});
		add(signIn);
		
		// create the sign in creds panel
		Dimension credsPanelSize = new Dimension(size.width-35, 152);
		signCreds = new DropdownPanel(title, new String[] {
				"Username:",
				"Password:"
		}, new JComponent[] {
			new JTextField(),
			new JTextField()	
		}, credsPanelSize, "Login/Sign up");
		
		signCreds.setBounds(35, SIGNB_IN_HEIGHT+3, credsPanelSize.width, credsPanelSize.height);
		signCreds.setLayout(null);
		signCreds.addCloseButtonAction(l -> {
			signCreds.setCloseButtonText("...");
			title.loggedInRequest(signCreds.getTextComponent(0).getText(), signCreds.getTextComponent(1).getText());
		});
		add(signCreds);
		
		// create the profile panel
		Dimension profilePanelSize = new Dimension(size.width-35, 302);
		profileInfo = new DropdownPanel(title, new String[] {
			"First Name:",
			"Last Name:",
			"Username:",
			"Password:",
			"Bio:"
		}, new JComponent[] {
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JTextField(),
			new JTextArea()
		}, profilePanelSize, "Save");
		profileInfo.setBounds(35, SIGNB_IN_HEIGHT+3, profilePanelSize.width, profilePanelSize.height);
		profileInfo.setLayout(null);
		profileInfo.addCloseButtonAction(l -> {
			saveModifiedProfileSettings();
		});
		add(profileInfo);
		
		icon = new ProfileIcon(title);
		icon.setBounds(size.width - SIGNB_IN_HEIGHT, 0, SIGNB_IN_HEIGHT, SIGNB_IN_HEIGHT);
		icon.addActionListener(l -> {
			setPopupOpen(!popupOpen);
		});
		add(icon);
		
		// setup UI
		//createSignCredsPanelContents(signCreds.getSize());
		setPopupOpen(false);
		setLoggedIn(false);
	}
	
	/**
	 * This method will update the colors of the chooser, this is prob
	 * called by the panel's parent
	 *
	 * @author Ethan Rees
	 */
	public void onThemeChanged() {
		title.setColor(signIn, 3, 2);
		title.setColor(signCreds, 2, 2);
		title.setColor(profileInfo, 2, 2);
		signCreds.themeChange();
		profileInfo.themeChange();

		int signInOffset = 0;
		if(isLoggedIn) signInOffset -= SIGNB_IN_HEIGHT+5;
		signIn.setBounds(size.width - SIGNB_IN_WIDTH + signInOffset, 0, SIGNB_IN_WIDTH, SIGNB_IN_HEIGHT);
		icon.setVisible(isLoggedIn);
	}
	
	/**
	 * This will set if the system should consider the player logged in
	 * 
	 * @author Ethan Rees
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.isLoggedIn = loggedIn;
		signIn.setText(isLoggedIn ? "Log Out" : "Sign In / Sign Up");
		
		if(isLoggedIn && TitleScreenUI.loggedInPlayer != null) {
			Player player = TitleScreenUI.loggedInPlayer;
			icon.setCharacter(player.getUsername().charAt(0));
			profileInfo.getTextComponent(0).setText(player.getFirstName());
			profileInfo.getTextComponent(1).setText(player.getLastName());
			profileInfo.getTextComponent(2).setText(player.getUsername());
			profileInfo.getTextComponent(3).setText(player.getPassword());
			profileInfo.getTextAreaComponent(4).setText(player.getBio());
		}
		onThemeChanged();
	}
	
	/**
	 * After changes have been made on the profile info UI, save those changes to the server
	 *
	 * @author Ethan Rees
	 */
	public void saveModifiedProfileSettings() {
		Player player = TitleScreenUI.loggedInPlayer;
		player.setFirstName(profileInfo.getTextComponent(0).getText());
		player.setLastName(profileInfo.getTextComponent(1).getText());
		player.setUsername(profileInfo.getTextComponent(2).getText());
		player.setPassword(profileInfo.getTextComponent(3).getText());
		player.setBio(profileInfo.getTextAreaComponent(4).getText());
		DBAdaptor.updateUser(player);
		setPopupOpen(false);
	}
	
	/**
	 * This will set if the pop out is open or not
	 *
	 * @author Ethan Rees 
	 */
	public void setPopupOpen(boolean open) {
		signCreds.setCloseButtonText("Sign In / Sign Up");
		signCreds.setVisible(open && !isLoggedIn);
		profileInfo.setVisible(open && isLoggedIn);
		popupOpen = open;
	}
	
}
