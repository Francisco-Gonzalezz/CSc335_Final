/**
 * This class creates and holds a title screen UI
 * 
 * @author Ethan Rees
 */
package ui.titleScreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import _main.KeyStage;
import ui.Scene;
import ui.SceneManager;
import ui.help.HelpPageUI;
import ui.leaderboard.LeaderboardUI;
import ui.wordleGameBoard.GameBoardUITile;
import ui.wordleGameBoard.WordleGameBoardUI;

public class TitleScreenUI extends Scene {
	
	String[] titleButtons = {
			"Play As Guest",
			"Leaderboard",
			"Help"
	};
	Scene[] titleButtonsScenes = {
			new WordleGameBoardUI(SceneManager.size),
			new LeaderboardUI(SceneManager.size),
			new HelpPageUI(SceneManager.size)
	};
	
	JButton[] titleJButtons;
	
	JPanel titleTileHolder;
	GameBoardUITile[] titleTiles;
	
	JLabel subtitle;
	JLabel darkModeToggleLabel;
	
	ProfileChooser chooser;
	boolean error;
	
	public TitleScreenUI() {
		
		setLayout(null);
		int buttonWidth = 200;
		int buttonHeight = 50;
		int verticalSpacing = 5;
		int verticalOffset = 370;
		
		// create the title buttons
		titleJButtons = new JButton[titleButtons.length];
		for(int i = 0; i < titleButtons.length; i++) {
			JButton button = new JButton(titleButtons[i]);
			final int j = i;
			button.addActionListener(l -> {
				SceneManager.setScene(titleButtonsScenes[j]);
			});
			button.setBounds((int)(SceneManager.size.width*0.5 - buttonWidth * 0.5), verticalOffset + buttonHeight * i + verticalSpacing*i, buttonWidth, buttonHeight);
			add(button);
			titleJButtons[i] = button;
		}
		constructLogo();

		// create the subtitles
		subtitle = new JLabel("Frankie Gonzalez, Aditya Gupta, Ethan Rees, Brian Vu");
		int subtitleWidth = getFontMetrics(subtitle.getFont()).stringWidth(subtitle.getText())+2;
		subtitle.setBounds((int)(SceneManager.size.width*0.5 - subtitleWidth*0.5),SceneManager.size.height - 105,subtitleWidth,100);
		add(subtitle);
		
		int size = 35;
		IconToggle toggle = new IconToggle("toDarkLogo.png", "toLightLogo.png", isDarkMode, size, size);
		toggle.setBounds(10, 10, size, size);
		toggle.addActionListener(l -> {
			SceneManager.setDarkMode(toggle.value);
		});
		
		darkModeToggleLabel = new JLabel("Switch");
		darkModeToggleLabel.setBounds(8, 10 + size, size+10, 20);
		add(darkModeToggleLabel);
		add(toggle);
		
		Dimension profileChooserSize = new Dimension(200, 170);
		chooser = new ProfileChooser(this, profileChooserSize);
		chooser.setBounds(SceneManager.size.width - 18 - profileChooserSize.width, 5, profileChooserSize.width, profileChooserSize.height);
		add(chooser);
	}
	
	/**
	 * This method will construct the wordle logo
	 *
	 * @author Ethan Rees
	 */
	void constructLogo() {
		titleTileHolder = new JPanel();
		titleTileHolder.setOpaque(false);
		titleTileHolder.setLayout(null);
		
		// position the frame
		int width = 500;
		int x = 32;
		int y = 60;
		int tileSize = 100;
		int padding = 5;
		titleTileHolder.setBounds((int)(SceneManager.size.width*0.5 - width*0.5), 70, 500, 300);
		
		// create the text
		Font font = new Font("Arial", Font.BOLD, 75);
		String text = "WORDLE";
		
		// iterate through the tiles and create the title board
		titleTiles = new GameBoardUITile[text.length()];
		for(int i = 0; i < text.length(); i++) {
			titleTiles[i] = new GameBoardUITile(this, font);
			titleTiles[i].setHeightOffset(73);
			titleTiles[i].setCharacter(text.charAt(i) + "");
			titleTiles[i].setBounds(x, y, tileSize, tileSize);
			titleTiles[i].setStage(Math.random() > 0.5 ? KeyStage.InWordWrongPlace : KeyStage.InWordRightPlace, false);
			x += tileSize + padding;
			if(i == 2) {
				x = (int)(tileSize + tileSize*0.5 + padding);
				y += tileSize + padding;
			}
			titleTileHolder.add(titleTiles[i]);
		}
		
		add(titleTileHolder);
	}
	
	/**
	 * This is called once the profile chooser has requested a login
	 *
	 * @author Ethan Rees 
	 * @param username given username
	 * @param password given password
	 */
	public void loggedInRequest(String username, String password) {
		titleJButtons[0].setText("Play as " + username);
		chooser.setPopupOpen(false);
		chooser.setLoggedIn(true);
	}
	
	/**
	 * This is called once the profile chooser has requested a logout
	 *
	 * @author Ethan Rees
	 */
	public void logOutRequest() {
		titleJButtons[0].setText("Play as Guest");
		chooser.setLoggedIn(false);
	}
	
	/**
	 * This is called on an error message, it just replaces the credits with
	 * the error
	 *
	 * @author Ethan Rees 
	 * @param error
	 */
	public void onError(Exception error) {
		subtitle.setText(error.toString());
		this.error = true;
		subtitle.setForeground(Color.red);
	}
	
	@Override
	public void onThemeChange(boolean isDarkMode) {
		setBackground(getBackgroundColor());
		
		// update the main buttons
		for(int i = 0; i < titleJButtons.length; i++) {
			JButton button = titleJButtons[i];
			button.setBackground(contrastColor(getHylightColor().darker(), 3));
			button.setForeground(getTextColor());
			button.setBorder(BorderFactory.createLineBorder(getHylightColor(), 1));
		}
		
		// update the subtitle
		Color textColor = new Color(getTextColor().getRed(), getTextColor().getGreen(), getTextColor().getBlue(), 100);
		subtitle.setForeground(error ? Color.red : textColor);
		
		// dark mode toggle
		darkModeToggleLabel.setForeground(textColor);
		
		// profile chooser
		chooser.onThemeChanged();
	}

}
