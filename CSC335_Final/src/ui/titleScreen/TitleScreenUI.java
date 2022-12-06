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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import _main.KeyStage;
import _main._SceneManager;
import _main.wordleLogic;
import db.DBAdaptor;
import player.Player;
import ui.Scene;
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
	
	JButton[] titleJButtons;
	
	JPanel titleTileHolder;
	GameBoardUITile[] titleTiles;
	
	JLabel subtitle;
	JLabel darkModeToggleLabel;
	
	JComboBox<String> gameDifficulty, gameMode;
	
	ProfileChooser chooser;
	IconToggle iconToggle;
	boolean error;
	
	public static Player loggedInPlayer;
	
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
				if(j == 0) {
					setGameSize();
					_SceneManager.setScene(new WordleGameBoardUI(_SceneManager.size, gameMode.getSelectedItem().equals("Countries")));
				} else if(j == 1) {
					_SceneManager.setScene(new LeaderboardUI(_SceneManager.size, null));
				} else if(j == 2) {
					_SceneManager.setScene(new HelpPageUI(_SceneManager.size));
				}
				
			});
			button.setBounds((int)(_SceneManager.size.width*0.5 - buttonWidth * 0.5), verticalOffset + buttonHeight * i + verticalSpacing*i, buttonWidth, buttonHeight);
			add(button);
			titleJButtons[i] = button;
		}
		constructLogo();

		// create the subtitles
		subtitle = new JLabel("Frankie Gonzalez, Aditya Gupta, Ethan Rees, Brian Vu");
		int subtitleWidth = getFontMetrics(subtitle.getFont()).stringWidth(subtitle.getText())+10;
		subtitle.setBounds((int)(_SceneManager.size.width*0.5 - subtitleWidth*0.5),_SceneManager.size.height - 105,subtitleWidth,100);
		add(subtitle);
		
		// create the dark/light toggle
		int darkLightSize = 35;
		iconToggle = new IconToggle("toLightLogo.png", "toDarkLogo.png", isDarkMode, darkLightSize, darkLightSize);
		iconToggle.setBounds(10, 10, darkLightSize, darkLightSize);
		iconToggle.addActionListener(l -> {
			_SceneManager.setDarkMode(!isDarkMode);
		});
		add(iconToggle);
		
		// create the dark/light toggle leaderboardLabel
		darkModeToggleLabel = new JLabel("Switch");
		darkModeToggleLabel.setBounds(8, 10 + darkLightSize, darkLightSize+10, 20);
		add(darkModeToggleLabel);
		
		// create the profile chooser
		Dimension profileChooserSize = new Dimension(200, 400);
		chooser = new ProfileChooser(this, profileChooserSize);
		chooser.setBounds(_SceneManager.size.width - 18 - profileChooserSize.width, 5, profileChooserSize.width, profileChooserSize.height);
		add(chooser);
		
		//gameDifficulty = new JComboBox<>(new String[] {"Normal", ""});
		//gameDifficulty.setBounds(darkLightSize + 30, 10, 100, darkLightSize);
		//add(gameDifficulty);

		gameMode = new JComboBox<>(new String[] {"Wordle", "Countries"});
		gameMode.setBounds(darkLightSize + 30, 10, 100, darkLightSize);
		add(gameMode);
		
		// load up the game
		updatePlayButtonName();
		if(loggedInPlayer != null) {
			chooser.setLoggedIn(true);
			_SceneManager.setDarkMode(!loggedInPlayer.getTheme());
		}
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
		titleTileHolder.setBounds((int)(_SceneManager.size.width*0.5 - width*0.5), 70, 500, 300);
		
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
	
	
	void setGameSize() {
		int wordLength = 5;
		int attemptCount = 6;
		WordleGameBoardUI.WORD_SIZE = wordLength;
		WordleGameBoardUI.ATTEMPT_AMOUNT = attemptCount;
	}
	/**
	 * This is called once the profile chooser has requested a login
	 *
	 * @author Ethan Rees 
	 * @param username given username
	 * @param password given password
	 */
	public void loggedInRequest(String username, String password) {
		
		loggedInPlayer = DBAdaptor.loginToUser(username, password);
		if(loggedInPlayer == null) {
			DBAdaptor.registerNewUser(new Player(username, password, "", ""));
			loggedInPlayer = DBAdaptor.loginToUser(username, password);
		
			if(loggedInPlayer == null) {
				onError(new Exception("Failed to login!"));
				chooser.setLoggedIn(false);
				chooser.setPopupOpen(true);
				return;
			}
		}
		
		updatePlayButtonName();
		chooser.setPopupOpen(false);
		chooser.setLoggedIn(true);
		_SceneManager.setDarkMode(!loggedInPlayer.getTheme());
		subtitle.setText("Frankie Gonzalez, Aditya Gupta, Ethan Rees, Brian Vu");
	}
	
	/**
	 * This will update the play button names
	 *
	 * @author Ethan Rees
	 */
	void updatePlayButtonName() {
		if(loggedInPlayer == null)
			titleJButtons[0].setText("Play as Guest");
		else
			titleJButtons[0].setText("Play as " + loggedInPlayer.getDisplayName());
		
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
		subtitle.setText(error.getLocalizedMessage());
		this.error = true;
		subtitle.setForeground(Color.red);
	}
	
	/*
	 * The theme has changed
	 */
	@Override
	public void onThemeChange(boolean isDarkMode) {
		setBackground(getBackgroundColor());
		iconToggle.setValue(isDarkMode);
		
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
		
		setColor(gameMode, 0, 1);
		
		// dark mode iconToggle
		darkModeToggleLabel.setForeground(textColor);
		
		// profile chooser
		chooser.onThemeChanged();
	}

}
