/**
 * This class creates and hold the leaderboard page
 * 
 * @author Ethan Rees
 */
package ui.leaderboard;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import _main._SceneManager;
import _main.WordleGameResult;
import db.DBAdaptor;
import player.Player;
import ui.Scene;
import ui.titleScreen.TitleScreenUI;

public class LeaderboardUI extends Scene {
	
	JLabel leaderboardLabel, lastGamesLabel, statsLabel;
	JList<String> leaderboardList, lastGamesList;
	DefaultListModel<String> leaderboardModel, lastGamesModel;
	
	LeaderboardStatsUI statsBox;
	
	Font titleFont;
	JButton refreshButton;
	JButton exitButton;
	
	WordleGameResult result;
	
	public LeaderboardUI(Dimension size, WordleGameResult result) {
		this.result = result;
		setLayout(null);
		
		int mainBoardOffset = result == null ? 0 : 175;
		int boardShifter = result == null ? 0 : 30;
		createLeaderboard(size, mainBoardOffset, boardShifter);
		
		if(result != null)
			createStatsBoard(size, mainBoardOffset, boardShifter);
		
		// refresh button
		int refreshButtonWidth = 100;
		refreshButton = new JButton();
		refreshButton.setBounds(centeredWidthXOffset(size, refreshButtonWidth), 640, refreshButtonWidth, 30);
		refreshButton.addActionListener(l -> updateLeaderboard());
		add(refreshButton);
		
		updateLeaderboard();
		createExitButton();
	}
	
	void createLeaderboard(Dimension size, int mainBoardOffset, int boardShifter) {
		// draw leaderboard text
		titleFont = new Font("Arial", Font.BOLD, 28);
		leaderboardLabel = createLabel(titleFont, "Global Leaderboard:", -mainBoardOffset, 110 - boardShifter, 30);
		add(leaderboardLabel);
		
		// draw leaderboard panel
		int leaderboardWidth = 256;
		int leaderboardHeight = 480;
		leaderboardModel = new DefaultListModel<String>();
		leaderboardList = new JList<String>(leaderboardModel);
		leaderboardList.setBounds(centeredWidthXOffset(size, leaderboardWidth) - mainBoardOffset, 150 - boardShifter, leaderboardWidth, leaderboardHeight);
		add(leaderboardList);
	}

	void createStatsBoard(Dimension size, int mainBoardOffset, int boardShifter) {

		// draw leaderboard text
		statsLabel = createLabel(titleFont, "Game Results:", mainBoardOffset, 110 - boardShifter, 30);
		add(statsLabel);
		
		// create the stats box
		int statsBoxWidth = 256;
		int statsBoxHeight = 280;
		statsBox = new LeaderboardStatsUI(result);
		statsBox.setBounds(centeredWidthXOffset(size, statsBoxWidth) + mainBoardOffset, 150 - boardShifter, statsBoxWidth, statsBoxHeight);
		add(statsBox);
		
		// draw leaderboard text
		lastGamesLabel = createLabel(titleFont, "Your Past Games:", mainBoardOffset, 455 - boardShifter, 30);
		add(lastGamesLabel);

		// draw leaderboard panel
		int leaderboardWidth = 256;
		int leaderboardHeight = 135;
		lastGamesModel = new DefaultListModel<String>();
		lastGamesList = new JList<String>(lastGamesModel);
		lastGamesList.setBounds(centeredWidthXOffset(size, leaderboardWidth) + mainBoardOffset, 495 - boardShifter, leaderboardWidth, leaderboardHeight);
		add(lastGamesList);
	}
	
	JLabel createLabel(Font font, String text, int x, int y, int height) {
		int labelWidth = getFontMetrics(font).stringWidth(text);
		JLabel label = new JLabel(text);
		label.setFont(font);
		label.setBounds(centeredWidthXOffset(_SceneManager.size, labelWidth) + x, y, labelWidth, height);
		return label;
	}
	
	/**
	 * This method will ask the server for an updated leaderboard
	 *
	 * @author Ethan Rees
	 */
	void updateLeaderboard() {
		// remove the list elements
		leaderboardModel.removeAllElements();
		if(result != null)
			lastGamesModel.removeAllElements();
		leaderboardModel.addElement("Loading...");
		if(result != null)
			lastGamesModel.addElement("Loading...");
		refreshButton.setText("...");
		
		if(result != null && result.isGuest) {
			lastGamesModel.removeAllElements();
			lastGamesModel.addElement("Login to see your past games!");
		}
		
		Thread waitThread = new Thread(() -> {
			// get the leaderboard data
			try {
				List<String> leaderboard = DBAdaptor.getLeaderBoard();
				setLeaderboardData(leaderboard);
				if(result != null && !result.isGuest)
					setStatsData(TitleScreenUI.loggedInPlayer);
				
			} catch (Exception error) {

				ArrayList<String> temp = new ArrayList<String>();
				temp.add("Error: " + error.getLocalizedMessage());
				setLeaderboardData(temp);
			}
		});
		waitThread.start();
	}
	
	/**
	 * Once the leaderboard has responded, here is the new leaderboard contents, add
	 * them to the leaderboard
	 *
	 * @author Ethan Rees 
	 * @param leaderboardList new data
	 */
	void setLeaderboardData(List<String> list) {
		leaderboardModel.removeAllElements();
		refreshButton.setText("Refresh List");
		for(int i = 0; i < list.size(); i++) {
			String[] splitdata = list.get(i).split(",");
			leaderboardModel.addElement((i+1) + ") " + splitdata[0] + "   -   " + splitdata[1]);
			if(TitleScreenUI.loggedInPlayer != null && TitleScreenUI.loggedInPlayer.getUsername().equals(splitdata[0])) {
				leaderboardList.setSelectedIndex(i);
			}
		}
	}
	
	void setStatsData(Player player) {
		lastGamesModel.removeAllElements();
		ArrayDeque<String> words = player.getGameWords();
		ArrayDeque<Integer> guesses = player.getGuesses();
		ArrayDeque<String> dates = player.getDates();
		
		if(words.size() == 0) {
			lastGamesModel.addElement("No stats found");
			return;
		}
		
		int wordSize = words.size();
		for(int i = 0; i < Math.min(7, wordSize); i++) {
			String word = words.remove();
			if(word != null) {
				int guess = guesses.remove();
				String date = dates.remove();
				String current = word + ", " + guess + " guesses | " + date;
				lastGamesModel.addElement(current);
			}
		}
	}
	
	@Override
	public void onThemeChange(boolean isDarkMode) {
		setBackground(getBackgroundColor());
		leaderboardLabel.setForeground(getTextColor());
		
		leaderboardList.setBackground(contrastColor(getHylightColor(), 3));
		leaderboardList.setForeground(getTextColor());
		leaderboardList.setBorder(BorderFactory.createLineBorder(getHylightColor(), 3));
		
		if(result != null) {
			lastGamesLabel.setForeground(getTextColor());
			statsLabel.setForeground(getTextColor());
			
			statsBox.setIsDarkMode(isDarkMode);
			
			lastGamesList.setBackground(contrastColor(getHylightColor(), 3));
			lastGamesList.setForeground(getTextColor());
			lastGamesList.setBorder(BorderFactory.createLineBorder(getHylightColor(), 3));
		}
		
		refreshButton.setBackground((contrastColor(getHylightColor(), 4)));
		refreshButton.setForeground(contrastColor(getTextColor(), 1));
		refreshButton.setBorder(BorderFactory.createLineBorder(contrastColor(getHylightColor(), 2), 1));
	}
}
