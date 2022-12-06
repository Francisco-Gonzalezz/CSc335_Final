/**
 * This class will hold the game results and will be used by the leaderboard/stats
 * 
 * @author Ethan Rees
 */
package _main;

import java.text.SimpleDateFormat;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.util.Calendar;

import db.DBAdaptor;
import player.Player;
import ui.titleScreen.TitleScreenUI;

public class WordleGameResult {
	public String word;
	public boolean isGuest;
	public boolean didWin;
	public int guessAmount;
	
	public int gamesPlayed, gamesWon, gamesLost;
	public double winRate;
	int totalGuessSlots;
	public KeyStage[][] tiles;
	
	/**
	 * This will save the results to the profile if it exists
	 *
	 * @author Ethan Rees 
	 * @param totalGuessSlots The total number of attempted slots
	 */
	public void publishResults(int totalGuessSlots) {
		this.totalGuessSlots = totalGuessSlots;
		if(!isGuest) {
			Player player = TitleScreenUI.loggedInPlayer;
			player.addGameWord(word);
			player.addGuess(guessAmount);
			player.addDate(new SimpleDateFormat("MMM dd yyyy").format(Calendar.getInstance().getTime()));
			
			if(didWin)
				player.addWin();
			
			player.addGamesPlayed();
			DBAdaptor.updateUser(player);
			
			gamesWon = player.getWins();
			gamesLost = player.getGamesPlayed() - player.getWins();
			gamesPlayed = player.getGamesPlayed();
			winRate = player.getWinRate();
		} else {
			gamesWon = didWin ? 1 : 0;
			winRate = didWin ? 1 : 0;
			gamesLost = didWin ? 0 : 1;
			gamesPlayed = 1;
		}
	}
	
	/**
	 * This will get the bar stats seen on the stat page, it just collects how many times
	 * someone guessed a certian amount
	 *
	 * @author Ethan Rees 
	 */
	public int[] getDistroStats() {
		int[] results = new int[totalGuessSlots];
		
		if(!isGuest) {
			Player player = TitleScreenUI.loggedInPlayer;
			for(int guesses : player.getGuesses()) {
				if(guesses-1 < totalGuessSlots)
				results[Math.max(0, guesses-1)]++;
			}
		} else if(guessAmount-1 < totalGuessSlots) {
			results[Math.max(0, guessAmount-1)]++;
		}
		
		
		return results;
	}
	
	/**
	 * Using the getDistroStats, this will find the normalized % amounts
	 *
	 * @author Ethan Rees 
	 */
	public double[] getDistroPercentages(int[] stats) {
		double[] percents = new double[stats.length];
		
		// find the largest first
		double largest = 0;
		for(int value : stats) {
			if(value > largest)
				largest = value;
		}

		// then normalize the rest
		for(int i = 0; i < stats.length; i++) {
			percents[i] = stats[i] / largest;
		}
		
		return percents;
	}
	
	public void shareToCopy() {
		String soFar = "Wordle ";
		soFar += (guessAmount) + "/" + totalGuessSlots;
		soFar += " ";
		for(int r = 0; r < Math.min(guessAmount, tiles.length); r++) {
			for(int c = 0; c < tiles[r].length; c++) {
				if(tiles[r][c] == KeyStage.InWordRightPlace)
					soFar += "🟩";
				else if(tiles[r][c] == KeyStage.InWordWrongPlace)
					soFar += "🟨";
				else
					soFar += "⬛";
			}
			soFar += "\n";
		}
		
		StringSelection stringSelection = new StringSelection(soFar);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
		
		System.out.println(soFar);
	}
}