package db;

import java.util.List;
import player.Player;

public class testDB {

	public static void main( String[] args ) {
		/*
		 * Testing on registering new users and updating user profiles. Player frankie = new Player(
		 * "Francisco-Gonzalezz", "SuperCoolPassWord", "Frankie", "Gonzalez" ); frankie.setBio(
		 * "Put this in the DB please." ); Player brian = new Player( "Briann-vu", "TopSecret", "Brian", "Vu" );
		 * brian.switchTheme(); brian.setBio( "CHANGED" ); DBAdaptor.registerNewUser( frankie ); DBAdaptor.updateUser(
		 * brian );
		 */

		// Ethan Run the below code to add to DB!

		// Look at leaderboard before adding user
		DBAdaptor.getLeaderBoard();
		System.out.println( "\n\n" );
		Player ethan = new Player( "Ethan-Rees", "ThisIsASecret", "Ethan", "Rees" );
		DBAdaptor.registerNewUser( ethan );

		// Your name should be on it after!
		DBAdaptor.getLeaderBoard();

		// Delete Ethan from dB and check leaderboard after.
		DBAdaptor.deleteUser( ethan );
		System.out.println( "\n\n" );
		DBAdaptor.getLeaderBoard();

		Player frankie = DBAdaptor.loginToUser( "Francisco-Gonzalezz", "SuperCoolPassWord" );
		System.out.println( frankie.getUsername() );
	}

}
