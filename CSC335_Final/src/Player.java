/**
 * 
 * @author Brian Vu
 *
 */
public class Player {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	// profile pic?
	private String bio;
	private boolean lightOrDark;

	private int gamesPlayed;
	private int wins;

	public Player(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		gamesPlayed = 0;
		wins = 0;
		bio = "";
		this.lightOrDark = true; // true = white, false dark
	}

	public void addWin() {
		this.wins++;
	}

	public int getWins() {
		return this.wins;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setBio(String bio) {
		this.bio = bio;
		// max chars?
	}
	
	public int getWinRate() {
		return (this.wins / this.gamesPlayed);
	}
	
	public int getGamesPlayed() {
		return this.gamesPlayed;
	}
	
	public void switchTheme() {
		this.lightOrDark = !this.lightOrDark; 
	}
}
