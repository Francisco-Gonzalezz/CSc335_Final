/**
 * This class creates and hold the leaderboard page
 * 
 * @author Ethan Rees
 */
package ui.leaderboard;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import db.DBAdaptor;
import ui.Scene;
import ui.SceneManager;
import ui.titleScreen.TitleScreenUI;

public class LeaderboardUI extends Scene {
	
	JLabel label;
	JList<String> leaderboardList;
	DefaultListModel<String> leaderboardModel;
	
	Font titleFont;
	JButton refreshButton;
	JButton exitButton;
	
	public LeaderboardUI(Dimension size) {
		setLayout(null);

		// draw leaderboard text
		String labelText="Global Leaderboard:";
		titleFont = new Font("Arial", Font.BOLD, 32);
		int labelWidth = getFontMetrics(titleFont).stringWidth(labelText);
		label = new JLabel(labelText);
		label.setFont(titleFont);
		label.setBounds(centeredWidthXOffset(size, labelWidth), 100, labelWidth, 30);
		add(label);
		
		// draw leaderboard panel
		int leaderboardWidth = 256;
		int leaderboardHeight = 480;
		leaderboardModel = new DefaultListModel<String>();
		leaderboardList = new JList<String>(leaderboardModel);
		leaderboardList.setBounds(centeredWidthXOffset(size, leaderboardWidth), 150, leaderboardWidth, leaderboardHeight);
		add(leaderboardList);
		
		// refresh button
		int refreshButtonWidth = 100;
		refreshButton = new JButton();
		refreshButton.setBounds(centeredWidthXOffset(size, refreshButtonWidth), 640, refreshButtonWidth, 30);
		refreshButton.addActionListener(l -> updateLeaderboard());
		add(refreshButton);
		
		updateLeaderboard();
		createExitButton();
	}

	/**
	 * This method will ask the server for an updated leaderboard
	 *
	 * @author Ethan Rees
	 */
	void updateLeaderboard() {
		// remove the list elements
		leaderboardModel.removeAllElements();
		leaderboardModel.addElement("Loading...");
		refreshButton.setText("...");
		
		Thread waitThread = new Thread(() -> {
			// get the leaderboard data
			try {
				List<String> leaderboard = DBAdaptor.getLeaderBoard();
				
				setLeaderboardData(leaderboard);
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
	
	@Override
	public void onThemeChange(boolean isDarkMode) {
		setBackground(getBackgroundColor());
		label.setForeground(getTextColor());
		
		leaderboardList.setBackground(contrastColor(getHylightColor(), 3));
		leaderboardList.setForeground(getTextColor());
		leaderboardList.setBorder(BorderFactory.createLineBorder(getHylightColor(), 3));
		
		refreshButton.setBackground((contrastColor(getHylightColor(), 4)));
		refreshButton.setForeground(contrastColor(getTextColor(), 1));
		refreshButton.setBorder(BorderFactory.createLineBorder(contrastColor(getHylightColor(), 2), 1));
	}
}
