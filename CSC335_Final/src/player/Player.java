package player;

import java.util.ArrayDeque;

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
	private ArrayDeque<String> gameWords;
	private ArrayDeque<String> dates;

	public Player ( String username, String password, String firstName, String lastName ) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gamesPlayed = 0;
		this.wins = 0;
		this.bio = "";
		this.truncatedBio = "";
		this.lightOrDark = false; // true = light, false dark
		this.guesses = new ArrayDeque<>();
		this.gameWords = new ArrayDeque<>();
		this.dates = new ArrayDeque<>();

	}

	public Player ( String username, String password ) {
		this.username = username;
		this.password = password;
		this.gamesPlayed = 0;
		this.wins = 0;
		this.bio = "";
		this.truncatedBio = "";
		this.lightOrDark = true; // true = light, false dark
		this.firstName = "";
		this.lastName = "";
		this.guesses = new ArrayDeque<>();
		this.gameWords = new ArrayDeque<>();
		this.dates = new ArrayDeque<>();
	}

	public ArrayDeque<String> getDates() {
		return dates.clone();
	}

	public void addDate( String date ) {
		if ( dates.size() == 7 ) {
			dates.removeLast();
		}
		dates.push( date );
	}

	public ArrayDeque<String> getGameWords() {
		return gameWords.clone();
	}

	public void removeDate() {
		dates.removeLast();
	}

	public void addGameWord( String word ) {
		if ( gameWords.size() == 7 ) {
			gameWords.removeLast();
		}
		gameWords.push( word );
	}

	public void removeGameWord() {
		gameWords.removeLast();
	}

	public ArrayDeque<Integer> getGuesses() {
		return guesses.clone();
	}

	public void addGuess( int guess ) {
		if ( guesses.size() == 7 ) {
			guesses.removeLast();
		}
		guesses.push( guess );
	}

	public void removeGuess() {
		guesses.removeLast();
	}

	public void addWin() {
		this.wins++;
	}

	public int getWins() {
		return this.wins;
	}

	public void setWins( int wins ) {
		this.wins = wins;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}
	
	public String getDisplayName() {
		return firstName.isEmpty() ? username : firstName;
	}

	public void setBio( String bio ) {
		if ( bio.length() == 1000 ) {
			this.truncatedBio = bio.substring( 0, 998 ) + "..."; // max chars = 1000
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

	public double getWinRate() {
		return ( this.wins / (double)this.gamesPlayed );
	}

	public int getGamesPlayed() {
		return this.gamesPlayed;
	}

	public void setGamesPlayed( int games ) {
		this.gamesPlayed = games;
	}
	
	public void addGamesPlayed() {
		this.gamesPlayed++;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setTheme( boolean theme ) {
		this.lightOrDark = theme;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
