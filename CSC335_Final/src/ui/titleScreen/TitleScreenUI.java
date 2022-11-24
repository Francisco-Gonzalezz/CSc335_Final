package ui.titleScreen;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import _main.KeyStage;
import ui.Scene;
import ui.SceneManager;
import ui.wordleGameBoard.GameBoardUITile;
import ui.wordleGameBoard.WordleGameBoardUI;

public class TitleScreenUI extends Scene {
	
	String[] titleButtons = {
			"Play",
			"Leaderboard",
			"Help"
	};
	Scene[] titleButtonsScenes = {
			new WordleGameBoardUI(SceneManager.size),
			null,
			null
	};
	
	JButton[] titleJButtons;
	
	JPanel titleTileHolder;
	GameBoardUITile[] titleTiles;
	
	JLabel subtitle;
	JLabel darkModeToggleLabel;
	
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
		subtitle = new JLabel("Aditya Gupta, Frankie Jackson, Ethan Rees, Brian Vu");
		int subtitleWidth = getFontMetrics(subtitle.getFont()).stringWidth(subtitle.getText())+2;
		subtitle.setBounds((int)(SceneManager.size.width*0.5 - subtitleWidth*0.5),SceneManager.size.height - 105,subtitleWidth,100);
		add(subtitle);
		
		int size = 50;
		IconToggle toggle = new IconToggle("toDarkLogo.png", "toLightLogo.png", isDarkMode, size, size);
		toggle.setBounds(10, 10, size, size);
		toggle.addActionListener(l -> {
			SceneManager.setDarkMode(toggle.value);
		});
		
		darkModeToggleLabel = new JLabel("Switch");
		darkModeToggleLabel.setBounds(15, 10 + size, size, 20);
		add(darkModeToggleLabel);
		add(toggle);
	}
	
	/**
	 * This method will construct the wordle logo
	 *
	 * @author Ethan Rees
	 */
	void constructLogo() {
		titleTileHolder = new JPanel();
		titleTileHolder.setLayout(null);
		
		// position the frame
		int width = 500;
		int x = 32;
		int y = 60;
		int tileSize = 100;
		int padding = 5;
		titleTileHolder.setBounds((int)(SceneManager.size.width*0.5 - width*0.5), 50, 500, 300);
		
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
	
	@Override
	public void onThemeChange(boolean isDarkMode) {
		setBackground(getBackgroundColor());
		
		for(int i = 0; i < titleJButtons.length; i++) {
			JButton button = titleJButtons[i];
			button.setBackground(contrastColor(getHylightColor().darker(), 3));
			button.setForeground(getTextColor());
			button.setBorder(BorderFactory.createLineBorder(getHylightColor(), 1));
		}

		titleTileHolder.setBackground(getBackgroundColor());
		Color textColor = new Color(getTextColor().getRed(), getTextColor().getGreen(), getTextColor().getBlue(), 100);
		subtitle.setForeground(textColor);
		darkModeToggleLabel.setForeground(textColor);
	}
}
