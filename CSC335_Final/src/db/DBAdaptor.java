package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author frankiegonzalez This class is the connection between the Java Program and the remote DB, it is able to
 *         interact with the remote MySQL DB by sending over SQL Commands through the internet.
 */

public class DBAdaptor {

	/**
	 * @author frankiegonzalez Example on how to start a connection to the DB
	 */
	public static void connectToDataBase() {
		// 192.168.1.141
		// jdbc:sqlserver://69.244.24.13:3306;databaseName=wordle
		// jdbc:mysql://69.244.24.13:3306/wordle
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://192.168.1.141:3306/wordle", "admin",
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

		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://192.168.1.141:3306/wordle", "admin",
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
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://192.168.1.141:3306/wordle", "admin",
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

	public static boolean loginToUser( String username, String password ) {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://192.168.1.141:3306/wordle", "admin",
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

	// TODO: Need to use Player object
	public static void registerNewUser() {
		try ( Connection DBConnection = DriverManager.getConnection( "jdbc:mysql://192.168.1.141:3306/wordle", "admin",
			"passw0rd" ) ) {
			Statement statement = DBConnection.createStatement();
			// Get values from Player Object
			if ( !doesUserExist( "wow", statement ) ) {
				String sql = "INSERT INTO Users VALUES ('wow', 'lame', 'Frankie', 'Gonzalez', 'I am super cool', 1, 0, 0);";
				statement.execute( sql );
			}
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper function that checks to make sure user does not exist before registering.
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

}
