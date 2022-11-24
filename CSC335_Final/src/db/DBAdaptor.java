package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import player.Player;

/**
 * @author frankiegonzalez This class is the connection between the Java Program and the remote DB, it is able to
 *         interact with the remote MySQL DB by sending over SQL Commands through the Internet.
 */

public class DBAdaptor {

	/**
	 * @author frankiegonzalez Example on how to start a connection to the DB
	 */
	public static void connectToDataBase() {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {

		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

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
	 * @return true if login credentials were correct, false otherwise.
	 */
	public static boolean loginToUser( String username, String password ) {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement statement = DBConnection.createStatement();
			// Grab username and password from DB
			try ( ResultSet result = statement
				.executeQuery( "SELECT UserName, Password FROM Users WHERE userName = '" + username + "';" ) ) {
				while ( result.next() ) {
					String retUser = result.getString( "UserName" );
					String retPass = result.getString( "Password" );
					if ( retUser != null & !retUser.isEmpty() ) {
						if ( password.equals( retPass ) ) {
							return true;
						}
					}
				}
			}
			DBConnection.close();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return false;
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
				statement.execute( sql );
				return true;
			} else {
				System.out.println( "This user already exists." );
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Helper function that checks to make sure user does not exist before registering. Didn't set this up in SQL
	 * @param String username
	 * @param Statement stmt
	 * @return true if user exists, false otherwise.
	 */
	public static boolean doesUserExist( String username, Statement stmt ) {
		try ( ResultSet result = stmt
			.executeQuery( "SELECT UserName FROM Users WHERE UserName = '" + username + "';" ) ) {
			while ( result.next() ) {
				return true;
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
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
			String sql = "UPDATE Users SET FirstName = '" + user.getFirstName() + "', LastName = '" + user.getLastName()
				+ "', Bio = '" + user.getBio() + "', LightOrDark = " + theme + ", GamesPlayed = "
				+ user.getGamesPlayed() + ", Wins = " + user.getWins() + " WHERE UserName = '" + user.getUsername()
				+ "';";
			stmt.execute( sql );
			return true;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Grabs a map of users to their current total of wins.
	 * @return Returns a HashMap that connects a username to total wins
	 */
	public static Map<String, Integer> getLeaderBoard() {
		Map<String, Integer> map = new HashMap<>();
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement stmt = DBConnection.createStatement();
			String sql = "SELECT UserName, Wins FROM Users ORDER BY Wins DESC;";
			try ( ResultSet result = stmt.executeQuery( sql ) ) {
				while ( result.next() ) {
					String username = result.getString( "UserName" );
					int wins = result.getInt( "Wins" );
					System.out.println( username + ": " + wins );
				}
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return map;
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

	/**
	 * Creates a new player object with information stored from DB. Returns null if user doesn't exist.
	 * @return Player object containing information stored for that user
	 * @param String: username..The user you want to create from DB
	 */
	public static Player getUser( String username ) {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://69.244.24.13:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement stmt = DBConnection.createStatement();
			String sql = "SELECT * FROM Users WHERE UserName = '" + username + "';";
			try ( ResultSet result = stmt.executeQuery( sql ) ) {

			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return null;
	}

}
