package _main;

import java.text.SimpleDateFormat;
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
	
	public void publishResults() {
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
}