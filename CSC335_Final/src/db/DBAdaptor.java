package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import player.Player;

/**
 * @author frankiegonzalez This class is the connection between the Java Program and the remote DB, it is able to
 *         interact with the remote MySQL DB by sending over SQL Commands through the Internet.
 */

public class DBAdaptor {

	/**
	 * This method selects the entire username column from MySQL and parses it to return a username list.
	 * @author frankiegonzalez
	 * @return A List of Strings containing the usernames of all registered users.
	 */
	public static List<String> getAllUsers() {
		// TODO: Change to List of Players
		List<String> users = new ArrayList<>();
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement statement = DBConnection.createStatement();
			try ( ResultSet result = statement.executeQuery( "SELECT UserName FROM Users;" ) ) {
				while ( result.next() ) {
					users.add( result.getString( "UserName" ) );
				}
			}
			DBConnection.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * Queries DB to see how many wins a specific player has
	 * @param String: playerName
	 * @return an int that has the amount of wins a player has.
	 */
	public static int getWinsForPlayer( String playerName ) {
		int playerWins = 0;
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement statement = DBConnection.createStatement();
			try ( ResultSet result = statement
				.executeQuery( "SELECT Wins FROM Users WHERE UserName = '" + playerName + "';" ) ) {
				while ( result.next() ) {
					playerWins = result.getInt( "Wins" );
				}
			} catch ( SQLException e ) {
				e.printStackTrace();
			}
			DBConnection.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return playerWins;
	}

	/**
	 * Compares the credentials that were given to DB to see if login successful.
	 * @param String: username
	 * @param String: password
	 * @return A filled out player object if credentials are correct, null otherwise.
	 */
	public static Player loginToUser( String username, String password ) {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement statement = DBConnection.createStatement();
			String sql = "SELECT UserName, Password FROM Users WHERE userName = '" + username + "';";
			// Grab username and password from DB
			try ( ResultSet result = statement.executeQuery( sql ) ) {
				while ( result.next() ) {
					String retUser = result.getString( "UserName" );
					String retPass = result.getString( "Password" );
					if ( retUser != null & !retUser.isEmpty() ) {
						if ( password.equals( retPass ) ) {
							return getUser( username, statement );
						}
					}
				}
			}
			DBConnection.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a new player object with information stored from DB. Returns null if user doesn't exist.
	 * @return Player object containing information stored for that user
	 * @param String: username..The user you want to create from DB
	 */
	private static Player getUser( String username, Statement stmt ) {
		String sql = "SELECT * FROM Users WHERE UserName = '" + username + "';";
		try ( ResultSet result = stmt.executeQuery( sql ) ) {
			while ( result.next() ) {
				String user = result.getString( "UserName" );
				String password = result.getString( "Password" );
				String firstName = result.getString( "FirstName" );
				String lastName = result.getString( "LastName" );
				String bio = result.getString( "Bio" );
				boolean theme = result.getBoolean( "LightOrDark" );
				int gamesPlayed = result.getInt( "GamesPlayed" );
				int wins = result.getInt( "Wins" );
				Player player = new Player( user, password, firstName, lastName );
				player.setBio( bio );
				player.setTheme( theme );
				player.setGamesPlayed( gamesPlayed );
				player.setWins( wins );
				return player;
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Registers a new user into the DB using a Player Object.
	 * @param String: user
	 * @return returns true if registering a user was successful, false if it failed.
	 */
	public static boolean registerNewUser( Player user ) {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement statement = DBConnection.createStatement();
			// Get values from Player Object
			if ( !doesUserExist( user.getUsername(), statement ) ) {
				int theme = user.getTheme() ? 1 : 0;
				String sql = "INSERT INTO Users ";
				String values = "VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "', '"
					+ user.getFirstName() + "', '" + user.getLastName() + "', '" + user.getBio() + "', " + theme
					+ ", 0, 0);";
				sql = sql + values;
				System.out.println( sql );
				statement.execute( sql );
				registerUserIntoStats( user.getUsername(), statement );
				DBConnection.close();
				return true;
			} else {
				System.out.println( "This user already exists." );
				return false;
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return false;
	}

	private static void registerUserIntoStats( String username, Statement stmt ) throws SQLException {
		String sql = "INSERT INTO Stats (Username) VALUES ('" + username + "');";
		System.out.println( sql );
		stmt.execute( sql );
	}

	/**
	 * Helper function that checks to make sure user does not exist before registering. Didn't set this up in SQL.
	 * Checks for user in Stats adn Users Table, although both should have the same usernames.
	 * @param String username
	 * @param Statement stmt
	 * @return true if user exists, false otherwise.
	 * @throws SQLException
	 */
	private static boolean doesUserExist( String username, Statement stmt ) throws SQLException {
		try ( ResultSet result = stmt
			.executeQuery( "SELECT UserName FROM Users WHERE UserName = '" + username + "';" ) ) {
			while ( result.next() ) {
				return true;
			}
		}
		try ( ResultSet result = stmt
			.executeQuery( "SELECT Username FROM Stats WHERE UserName = '" + username + "';" ) ) {
			while ( result.next() ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates DB to save the current state of the player.
	 * @param Player: user
	 * @return true if update was successful, false if update failed.
	 */
	public static boolean updateUser( Player user ) {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			int theme = user.getTheme() ? 1 : 0;
			Statement stmt = DBConnection.createStatement();
			if ( !doesUserExist( user.getUsername(), stmt ) ) {
				return false;
			}
			String sql = "UPDATE Users SET UserName = '" + user.getUsername() + "' Password = '" + user.getPassword()
				+ "' FirstName = '" + user.getFirstName() + "', LastName = '" + user.getLastName() + "', Bio = '"
				+ user.getBio() + "', LightOrDark = " + theme + ", GamesPlayed = " + user.getGamesPlayed() + ", Wins = "
				+ user.getWins() + " WHERE UserName = '" + user.getUsername() + "';";
			stmt.execute( sql );
			// TODO: Add call to updateStats
			DBConnection.close();
			return true;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Initial implementation of updating game stats for user.
	 * Needs to use player object to update everything.
	 */
	public static void updateStats() {
		ArrayDeque<Integer> guesses = new ArrayDeque<>();
		ArrayDeque<String> words = new ArrayDeque<String>();
		guesses.add( 1 );
		guesses.add( 2 );
		guesses.add( 3 );
		guesses.add( 4 );
		guesses.add( 5 );
		guesses.add( 6 );
		guesses.add( 5 );
		words.add( "foo" );
		words.add( "bar" );
		words.add( "CBA" );
		words.add( "Creation" );
		words.add( "Center" );
		words.add( "Component" );
		words.add( "Java" );
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			List<String> toInsert = new ArrayList<>();
			Statement stmt = DBConnection.createStatement();
			// Assumes that length of both deques are equal length.
			for ( int i = 7 ; i >= 1 ; i-- ) {
				String together = words.pop() + "," + guesses.pop();
				String sql = "UPDATE Stats SET game" + i + " = '" + together + "' WHERE Username = 'panchothecool'";
				System.out.println( sql );
				stmt.execute( sql );
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
		}

	}

	/**
	 * Initial implementation of getting Individual player stats. Returns null if player doesn't exist in Stats table.
	 * @return
	 */
	public static List<String> getStats() {
		List<String> stats = new ArrayList<>();
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement stmt = DBConnection.createStatement();
			if ( !doesUserExist( "panchothecool", stmt ) ) {
				return null;
			}
			String sql = "SELECT * FROM Stats WHERE Username = 'panchothecool'";
			try ( ResultSet result = stmt.executeQuery( sql ) ) {
				while ( result.next() ) {
					String game1 = result.getString( "game1" );
					String game2 = result.getString( "game2" );
					String game3 = result.getString( "game3" );
					String game4 = result.getString( "game4" );
					String game5 = result.getString( "game5" );
					String game6 = result.getString( "game6" );
					String game7 = result.getString( "game7" );
					stats.add( game1 );
					stats.add( game2 );
					stats.add( game3 );
					stats.add( game4 );
					stats.add( game5 );
					stats.add( game6 );
					stats.add( game7 );
				}
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		return stats;
	}

	/**
	 * Returns a ordered list of Users and their wins to display on the leaderboard.
	 * @return A List of Strings in the format "Username, wins" where the ordering is descending using number of wins.
	 */
	public static List<String> getLeaderBoard() {
		List<String> list = new ArrayList<>();
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement stmt = DBConnection.createStatement();
			String sql = "SELECT UserName, Wins FROM Users ORDER BY Wins DESC;";
			try ( ResultSet result = stmt.executeQuery( sql ) ) {
				while ( result.next() ) {
					String username = result.getString( "UserName" );
					int wins = result.getInt( "Wins" );
					list.add( username + "," + wins );
				}
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Sends SQL Command to delete the user passed in the parameter user.
	 * @param Player: user
	 * @return true if deletion is successful and false otherwise.
	 */
	public static boolean deleteUser( Player user ) {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement stmt = DBConnection.createStatement();
			String sql = "DELETE FROM Users WHERE UserName = '" + user.getUsername() + "';";
			stmt.execute( sql );
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return false;
	}

}
