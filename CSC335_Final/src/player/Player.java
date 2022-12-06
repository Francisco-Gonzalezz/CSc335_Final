package player;

import java.util.ArrayDeque;

/**
 * @author Brian Vu
 * 
 *         This class represents Player objects and keeps track of information
 *         like; username, password, name, bio, light/dark mode preference,
 *         number of games played, number of wins, recent number of guesses, recent 
 *         words and recent game dates.
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

	/**
	 * This is a constructor for the Player object and sets many fields like;
	 * username, password, name, bio, light/dark mode preference, number of games
	 * played, number of wins, recent number of guesses, recent words and recent game
	 * dates.
	 * 
	 * @param String: username
	 * @param String: password
	 * @param String: firstName
	 * @param String: lastName
	 */
	public Player(String username, String password, String firstName, String lastName) {
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

	/**
	 * This is a constructor for the Player object and sets many fields like;
	 * username, password, name, bio, light/dark mode preference, number of games
	 * played, number of wins, recent number of guesses, recent words and recent game
	 * dates.
	 * 
	 * @param String: username
	 * @param String: password
	 */
	public Player(String username, String password) {
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

	/**
	 * Method returns the ArrayDeque of dates that represents the 7 most recent
	 * games played.
	 * 
	 * @return ArrayDeque<String>
	 */
	public ArrayDeque<String> getDates() {
		return dates.clone();
	}

	/**
	 * Method pushes a date into the dates ArrayDeque and pops before adding if
	 * there are already 7 dates in it.
	 * 
	 * @param String: date
	 */
	public void addDate(String date) {
		if (dates.size() == 7) {
			dates.removeLast();
		}
		dates.push(date);
	}

	/**
	 * Method returns the ArrayDeque of gameWords that represents the 7 most recent
	 * words guessed on.
	 * 
	 * @return ArrayDeque<String>
	 */
	public ArrayDeque<String> getGameWords() {
		return gameWords.clone();
	}

	/**
	 * Method removes the last date from the dates ArrayDeque.
	 */
	public void removeDate() {
		dates.removeLast();
	}

	/**
	 * Method pushes a word into the gameWords ArrayDeque and pops before adding if
	 * there are already 7 words in it.
	 * 
	 * @param String: word
	 */
	public void addGameWord(String word) {
		if (gameWords.size() == 7) {
			gameWords.removeLast();
		}
		gameWords.push(word);
	}

	/**
	 * Method removes the last word from the gameWords ArrayDeque.
	 */
	public void removeGameWord() {
		gameWords.removeLast();
	}

	/**
	 * Method returns the ArrayDeque of guesses that represents the 7 most recent
	 * amounts of guesses it took to finish.
	 * 
	 * @return ArrayDeque<Integer>
	 */
	public ArrayDeque<Integer> getGuesses() {
		return guesses.clone();
	}

	/**
	 * Method pushes a guess into the guesses ArrayDeque and pops before adding if
	 * there are already 7 guesses in it.
	 * 
	 * @param int: guess
	 */
	public void addGuess(int guess) {
		if (guesses.size() == 7) {
			guesses.removeLast();
		}
		guesses.push(guess);
	}

	/**
	 * Method removes the last guess from the guesses ArrayDeque.
	 */
	public void removeGuess() {
		guesses.removeLast();
	}

	/**
	 * Method increments the win field.
	 */
	public void addWin() {
		this.wins++;
	}

	/**
	 * Method returns the wins field.
	 * 
	 * @return int: wins
	 */
	public int getWins() {
		return this.wins;
	}

	/**
	 * Method sets the wins field with the wins parameter.
	 * 
	 * @param int: wins
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}

	/**
	 * Method returns the firstName field.
	 * 
	 * @return String: firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Method returns the lastName field.
	 * 
	 * @return String: lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Method returns the username field or the firstName field if it is set.
	 * 
	 * @return String: username or firstName
	 */
	public String getDisplayName() {
		return firstName.isEmpty() ? username : firstName;
	}

	/**
	 * Method sets the bio field with the bio parameter, if the bio is longer than
	 * 1000 chars, the truncatedBio field is set to the bio (first 998 chars) with
	 * ellipsis. If the bio is shorter than 1000 chars, the truncatedBio and bio are
	 * both set to the bio parameter.
	 * 
	 * @param String: bio
	 */
	public void setBio(String bio) {
		if (bio.length() >= 1000) {
			this.truncatedBio = bio.substring(0, 998) + "..."; // max chars = 1000
			this.bio = bio;
		} else {
			this.truncatedBio = bio;
			this.bio = bio;
		}
	}

	/**
	 * Method returns bio field.
	 * 
	 * @return String: bio
	 */
	public String getBio() {
		return this.bio;
	}

	/**
	 * Method returns truncatedBio field.
	 * 
	 * @return String: truncatedBio
	 */
	public String getTruncatedBio() {
		return this.truncatedBio;
	}

	/**
	 * Method returns the win rate by dividing the wins field by gamesPlayed field.
	 * 
	 * @return double: this.wins / (double)this.gamesPlayed
	 */
	public double getWinRate() {
		return (this.wins / (double) this.gamesPlayed);
	}

	/**
	 * Method returns gamesPlayed field.
	 * 
	 * @return int: gamesPlayed
	 */
	public int getGamesPlayed() {
		return this.gamesPlayed;
	}

	/**
	 * Method sets gamesPlayed field with games parameter
	 * 
	 * @param int: games
	 */
	public void setGamesPlayed(int games) {
		this.gamesPlayed = games;
	}

	/**
	 * Method increments gamesPlayed field.
	 */
	public void addGamesPlayed() {
		this.gamesPlayed++;
	}

	/**
	 * Method negates the lightOrDark boolean field; light = true, dark = false.
	 */
	public void switchTheme() {
		this.lightOrDark = !this.lightOrDark;
	}

	/**
	 * Method returns lightOrDark field;
	 * 
	 * @return boolean: lightOrDark
	 */
	public boolean getTheme() {
		return this.lightOrDark;
	}

	/**
	 * Method returns username field.
	 * 
	 * @return String: username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Method sets the username field with username parameter.
	 * 
	 * @param String: username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Method returns password field.
	 * 
	 * @return Sring: password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Method sets the lightOrDark field with theme parameter.
	 * 
	 * @param boolean: theme
	 */
	public void setTheme(boolean theme) {
		this.lightOrDark = theme;
	}

	/**
	 * Method sets password field with password parameter.
	 * 
	 * @param String: password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Method sets firstName field with firstName parameter.
	 * 
	 * @param String: firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Method sets lastName field with lastName parameter.
	 * 
	 * @param String: lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
