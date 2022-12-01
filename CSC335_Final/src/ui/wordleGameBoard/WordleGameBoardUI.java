/**
 * @author Ethan Rees
 * This class creates the game board for the wordle game, it holds the different
 * keys and handles basic ui interaction
 */
package ui.wordleGameBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JPanel;

import _main.*;
import ui.*;


public class WordleGameBoardUI extends Scene implements KeyListener {
	
	// some settings
	public static final int WORD_SIZE = 5, ATTEMPT_AMOUNT = 6;
	public static final int BOARD_CELL_PADDING = 2, BOARD_HORI_PADDING = 190, KEYBOARD_HORI_PADDING = 100;
	public static final int KEY_TILE_DEFAULT_WIDTH = 60, KEY_TILE_HEIGHT = 55;
	
	public static WordleGameBoardUI ui;
	
	JPanel displayPanel;
	JPanel keyboardPanel;
	JPanel activeRowIndicator;
	
	GameBoardUITile[][] gameBoardTiles;
	Font gameBoardFont, keyboardFont;
	JLabel notification;
	public int activeRow, activeCol;
	Dimension size;
	
	String[][] keyboardButtonCharacters;
	KeyboardUITile[][] keyboardTiles;
	
	public WordleGameBoardUI(Dimension size) {
		this.size = size;
		ui = this;
		wordleLogic.beginGame();
		
		// setup myself
		setLayout(null);
		setBackground(getBackgroundColor());
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);

		this.keyboardButtonCharacters = new String[][] {
			{"Q","W","E","R","T","Y","U","I","O","P"},
			{"A","S","D","F","G","H","J","K","L"},
			{"ENTER","Z","X","C","V","B","N","M","BACK"}
		};
		this.activeRow = 0;
		this.activeCol = 0;
		this.gameBoardFont = new Font("Arial", Font.BOLD, 55);
		this.keyboardFont = new Font("Arial", Font.BOLD, 20);
		
		// setup the displayPanel
		add(setupDisplayPanel());
		add(setupKeyboardPanel());
		createExitButton();
	}
	
	/**
	 * This method will setup the display panel and create the letter tiles
	 * It also creates the notification
	 * @author Ethan Rees
	 * @return The JPanel that was setup
	 */
	JPanel setupDisplayPanel() {
		// setup the jpanel
		int panelWidth = size.width - BOARD_HORI_PADDING*2;
		int panelHeight = toInt(size.height * 0.61);
		displayPanel = new JPanel();
		displayPanel.setBounds(BOARD_HORI_PADDING-15, 30, panelWidth, panelHeight+35);
		displayPanel.setBackground(getBackground());
		displayPanel.setLayout(null);
		
		// Create the display cells
		// 
		// I am not using any grid layouts or anything, I'm placing them on the screen by hand
		// I'm doing this because swing grid layouts are awful and confusing and I've spent
		// way too long trying to figure them out, so instead of that I'll just
		// place the tiles manually on the screen w some simple math
		
		int cellWidth = toInt(panelWidth / (double)WORD_SIZE);
		int cellHeight = toInt(panelHeight / (double)ATTEMPT_AMOUNT);
		
		// fill in the display cells
		gameBoardTiles = new GameBoardUITile[ATTEMPT_AMOUNT][WORD_SIZE];
		for(int y = 0; y < ATTEMPT_AMOUNT; y++) {
			for(int x = 0; x < WORD_SIZE; x++) {
				gameBoardTiles[y][x] = new GameBoardUITile(this, gameBoardFont);
				
				// TILE BOUNDS:
				// x: x*cellWidth + padding
				// y: y*cellHeight + padding
				// width: cellWidth - padding*2
				// height: cellHeight - padding*2
				gameBoardTiles[y][x].setBounds(x * cellWidth+BOARD_CELL_PADDING, y * cellHeight+BOARD_CELL_PADDING, cellWidth-BOARD_CELL_PADDING*2, cellHeight-BOARD_CELL_PADDING*2);
				displayPanel.add(gameBoardTiles[y][x]);
			}
		}
		
		activeRowIndicator = new JPanel();
		add(activeRowIndicator);
		updateActiveRowIndicator();

		// create the notification
		notification = new JLabel("");
		pushNotification("Welcome to Wordle!");
		displayPanel.add(notification);
		
		return displayPanel;
	}
	
	/**
	 * This method will create the keyboard panel and setup the keys.
	 * @author Ethan Rees 
	 * @return the new panel created
	 */
	JPanel setupKeyboardPanel() {
		// create the main panel
		keyboardPanel = new JPanel();
		keyboardPanel.setBackground(getBackgroundColor());
		keyboardPanel.setLayout(null);
		
		// place the panel
		int panelWidth = size.width - KEYBOARD_HORI_PADDING*2;
		int panelHeight = toInt(size.height - 610);
		keyboardPanel.setBounds(KEYBOARD_HORI_PADDING-15, 530, panelWidth, panelHeight+35);
		
		
		// PLACE THE KEYS
		//
		// Placing the keys uses a custom built grid placer, as the Jpanel's grid system
		// is very confusing and difficult
		// It goes through all of the rows, finds the width, then centers the row,
		// then starts placing the keys
		
		keyboardTiles = new KeyboardUITile[keyboardButtonCharacters.length][0];
		for(int r = 0; r < keyboardButtonCharacters.length; r++) {
			int y = r * KEY_TILE_HEIGHT;  // the y position
			
			// create the tiles and calculate the total width
			int rowWidth = 0;
			keyboardTiles[r] = new KeyboardUITile[keyboardButtonCharacters[r].length];
			for(int c = 0; c < keyboardButtonCharacters[r].length; c++) {
				// create the tile and add the row width
				keyboardTiles[r][c] = new KeyboardUITile(this, keyboardButtonCharacters[r][c], keyboardFont);
				rowWidth += keyboardTiles[r][c].getNewWidth(KEY_TILE_DEFAULT_WIDTH);
			}

			// then iterate through the row and add to the UI
			int x = 0;	// the x position
			for(int c = 0; c < keyboardButtonCharacters[r].length; c++) {
				
				// grab the tile from the array
				KeyboardUITile tile = keyboardTiles[r][c];
				
				// find the centered offset
				int xCenterOffset = toInt(panelWidth*0.5 - rowWidth*0.5);
				
				// place the key on the UI
				tile.setBounds(x + xCenterOffset, y, tile.getNewWidth(KEY_TILE_DEFAULT_WIDTH), KEY_TILE_HEIGHT);
				keyboardPanel.add(tile);
				
				// because tile size may be different, increment the x position instead of calculating it before hand
				x += tile.getNewWidth(KEY_TILE_DEFAULT_WIDTH);
			}
		}
		
		return keyboardPanel;
	}
	
	// -------------------
	//   UI INTERACTION
	// -------------------
	
	/***
	 * This method will push a notification out to the screen!
	 * @param text The message that should be pushed out
	 * @author Ethan Rees
	 */
	public void pushNotification(String text) {
		// set the text color
		notification.setForeground(getTextColor());
		
		// place the text at the center of the screen using the font metrics
		int width = size.width - BOARD_HORI_PADDING*2;
		int textWidth = getFontMetrics(notification.getFont()).stringWidth(text);
		notification.setBounds(toInt(width * 0.5 - textWidth * 0.5), toInt(size.height * 0.61)-35, textWidth, 100);
		
		notification.setText(text);
	}
	
	/**
	 * This method will add a letter to the grid
	 * @param letter the letter to add
	 * @return True: the letter was added, False: the letter was not added because the row was full
	 * @author Ethan Rees
	 */
	public boolean addLetter(char letter) {
		// if the row is full
		if(activeCol >= WORD_SIZE)
			return false;
		
		if(activeCol < 0) activeCol = 0;
		gameBoardTiles[activeRow][activeCol].setCharacter(letter + "");
		activeCol++;
		
		return true;
	}
	
	/**
	 * This method will remove the last letter from the grid
	 * @return True: the letter was removed, False: there was no letter to remove in this row
	 * @author Ethan Rees
	 */
	public boolean removeLetter() {
		// if the row is empty
		if(activeCol <= 0)
			return false;
		
		activeCol--;
		gameBoardTiles[activeRow][activeCol].removeCharacter();
		return true;
	}

	/**
	 * This method will attempt to add a new row to the grid
	 * @return True: a new row was added and it passed the game logic, 
	 * False: There wasn't enough letters or the game logic failed or there were no rows left
	 * @author Ethan Rees
	 */
	public boolean enterNewRow() {
		// not enough letters
		if(activeCol < WORD_SIZE) {
			pushNotification("Not enough letters, you need 5!");

			// begin shake animations
			for(int c = 0; c < gameBoardTiles[activeRow].length; c++) {
				UIAnimator.beginAnimation(gameBoardTiles[activeRow][c], "shake", c * 0.02, .7);
			}
			
			return false;
		}
		
		// This is where the UI will run the game logic and stuff, then return here
		// if a new line is valid
		String currentRow = "";
		for(int i = 0; i < WORD_SIZE; i++)
			currentRow += gameBoardTiles[activeRow][i].getCharacter();
		
		// TODO check word stored in currentRow
		System.out.println(currentRow);
		
		// check if the word exists
		if(!wordleLogic.check_for_word(currentRow)) {
			pushNotification("Word doesn't exist!");
			
			// begin shake animations
			for(int c = 0; c < gameBoardTiles[activeRow].length; c++) {
				UIAnimator.beginAnimation(gameBoardTiles[activeRow][c], "shake", c * 0.02, .7);
			}
			
			return false;
		}
		
		wordleLogic.getTheWord(wordleLogic.correctWord, currentRow);
		
		// there aren't enough rows left
		if(activeRow+1 >= ATTEMPT_AMOUNT) {
			// game over
			pushNotification("Game Over!");
			return false;
		}
		
		// begin bounce animations
		for(int c = 0; c < gameBoardTiles[activeRow].length; c++) {
			UIAnimator.beginAnimation(gameBoardTiles[activeRow][c], "bounce", c * 0.05, .3);
		}
		
		activeCol = 0;
		activeRow++;
		
		updateActiveRowIndicator();
		pushNotification("Your Results...");
		return true;
	}
	
	/**
	 * This method will change the tile's stage at a particular spot
	 * @param row the row of the tile
	 * @param col the col of the tile
	 * @param newStage the stage to set.
	 * @author Ethan Rees
	 */
	public void setLetterStage(int row, int col, KeyStage newStage) {
		gameBoardTiles[row][col].setStage(newStage, true);
		
		// set the right keyboard letter to the right stage
		String character = gameBoardTiles[row][col].getCharacter();
		for(int r = 0; r < keyboardTiles.length; r++) {
			for(int c = 0; c < keyboardTiles[r].length; c++) {
				if(keyboardTiles[r][c].getContent().equals(character))
					keyboardTiles[r][c].setStage(newStage);
			}
		}
	}
	
	// -------------------
	//      EVENTS
	// -------------------

	@Override
	public void onThemeChange(boolean isDarkMode) {
		setBackground(getBackgroundColor());
		displayPanel.setBackground(getBackground());
		displayPanel.repaint();
		keyboardPanel.setBackground(getBackground());
		keyboardPanel.repaint();
		updateActiveRowIndicator();
		activeRowIndicator.repaint();
	}
	
	/**
	 * A key has been pressed!
	 *
	 * @author Ethan Rees 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// handle backspace
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			removeLetter();
		// handle enter
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			enterNewRow();
		// handle every other alphabetical character
		else if(Character.isAlphabetic(e.getKeyCode())) {
			addLetter(Character.toUpperCase(e.getKeyChar()));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	
	// -------------------
	//        UTIL
	// -------------------

	/**
	 * This will update row indicator's position
	 *
	 * @author Ethan Rees
	 */
	void updateActiveRowIndicator() {
		int panelHeight = toInt(size.height * 0.61);
		int cellHeight = toInt(panelHeight / (double)ATTEMPT_AMOUNT);
		
		activeRowIndicator.setBackground(UIAnimator.lerpColor(getHylightColor(), getBackgroundColor(), 0.7));
		activeRowIndicator.setBounds(160, 30+activeRow * cellHeight+BOARD_CELL_PADDING*2, 5, cellHeight-BOARD_CELL_PADDING*4);
	}

}
