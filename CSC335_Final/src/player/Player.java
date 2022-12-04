package player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * @author Brian Vu
 */
public class Player {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String bio;
	private String truncatedBio;
	private boolean lightOrDark;
	private int gamesPlayed;
	private int wins;
	private ArrayDeque<Integer> guesses;
	private ArrayDeque<ArrayList<String>> allWordsGuesed;
	

	public Player(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.gamesPlayed = 0;
		this.wins = 0;
		this.bio = "";
		this.truncatedBio = "";
		this.lightOrDark = true; // true = white, false dark
		this.guesses = new ArrayDeque<>();
		this.allWordsGuesed = new ArrayDeque<>();
		
	}

	public Player(String username, String password) {
		this.username = username;
		this.password = password;
		this.gamesPlayed = 0;
		this.wins = 0;
		this.bio = "";
		this.truncatedBio = "";
		this.lightOrDark = true; // true = white, false dark
		this.firstName = "";
		this.lastName = "";
		this.guesses = new ArrayDeque<>();
		this.allWordsGuesed = new ArrayDeque<>();
	}
	
	public ArrayDeque<ArrayList<String>> getAllWordsGuessed() {
		return this.allWordsGuesed;
	} 
	
	public void addWordsGuessed(ArrayList<String> words) {
		if (this.allWordsGuesed.size() == 7) {
			this.allWordsGuesed.pop();
		}
		this.allWordsGuesed.push(words);
	}
	
	public void removeWordsGuessed() {
		this.allWordsGuesed.pop();
	}

	public ArrayDeque<Integer> getQuesses() {
		return this.guesses;
	}

	public void addGuess(int guess) {
		if (this.guesses.size() == 7) {
			this.guesses.pop();
		}
		this.guesses.push(guess);
	}

	public void removeGuess() {
		this.guesses.pop();
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void addWin() {
		this.wins++;
	}

	public int getWins() {
		return this.wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setBio(String bio) {
		if (bio.length() == 1000) {
			this.truncatedBio = bio.substring(0, 998) + "..."; // max chars = 1000
			this.bio = bio;
		} else {
			this.truncatedBio = bio;
			this.bio = bio;
		}
	}

	public String getBio() {
		return this.bio;
	}

	public String getTruncatedBio() {
		return this.truncatedBio;
	}

	public int getWinRate() {
		return (this.wins / this.gamesPlayed);
	}

	public int getGamesPlayed() {
		return this.gamesPlayed;
	}

	public void setGamesPlayed(int games) {
		this.gamesPlayed = games;
	}

	public void switchTheme() {
		this.lightOrDark = !this.lightOrDark;
	}

	public boolean getTheme() {
		return this.lightOrDark;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setTheme(boolean theme) {
		this.lightOrDark = theme;
	}
}
