package player;

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

	public Player(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		gamesPlayed = 0;
		wins = 0;
		bio = "";
		truncatedBio = "";
		this.lightOrDark = true; // true = white, false dark
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
		if (bio.length() > 1000) {
			this.truncatedBio = bio.substring(0, 998) + "..."; // max chars = 1000
		}
		this.bio = bio;
	}

	public String getBio() {
		return bio;
	}

	public String getTruncatedBio() {
		return truncatedBio;
	}

	public int getWinRate() {
		return (this.wins / this.gamesPlayed);
	}

	public int getGamesPlayed() {
		return this.gamesPlayed;
	}

	public void setGamesPlayed(int games) {
		gamesPlayed = games;
	}

	public void switchTheme() {
		this.lightOrDark = !this.lightOrDark;
	}

	public boolean getTheme() {
		return lightOrDark;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setTheme(boolean theme) {
		lightOrDark = theme;
	}
}
