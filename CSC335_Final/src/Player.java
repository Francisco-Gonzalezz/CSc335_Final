
public class Player {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	// profile pic?
	private String bio;

	private int wins;

	public Player(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		wins = 0;
		bio = "";
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
}
