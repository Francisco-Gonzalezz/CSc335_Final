package db;

import java.util.List;
import java.util.Map;

import player.Player;

public class testDB {

	public static void main( String[] args ) {
		/*
		Player frankie = new Player( "Francisco-Gonzalezz", "SuperCoolPassWord", "Frankie", "Gonzalez" );
		frankie.setBio( "Put this in the DB please." );
		Player brian = new Player( "Briann-vu", "TopSecret", "Brian", "Vu" );
		brian.switchTheme();
		brian.setBio( "CHANGED" );
		DBAdaptor.registerNewUser( frankie );
		DBAdaptor.updateUser( brian );
		*/

		// Look at leaderboard before adding user
		List<String> list = DBAdaptor.getLeaderBoard();
		for (int i = 0; i < list.size(); i++) {
			String[] split = list.get( i ).split( "," );
			System.out.println( (i + 1) + ") " + split[0] + " with a score of " + split[1] );
		}
		System.out.println( "\n\n" );
		Player ethan = new Player( "Ethan-Rees", "ThisIsASecret", "Ethan", "Rees" );
		DBAdaptor.registerNewUser( ethan );
		ethan.setWins( 100 );
		DBAdaptor.updateUser( ethan );

		// Name should be added to leaderboard
		list = DBAdaptor.getLeaderBoard();
		for (int i = 0; i < list.size(); i++) {
			String[] split = list.get( i ).split( "," );
			System.out.println( (i + 1) + ") " + split[0] + " with a score of " + split[1] );
		}
		// Delete Ethan from dB and check leaderboard after.
		DBAdaptor.deleteUser( ethan );
		System.out.println( "\n\n" );
		list = DBAdaptor.getLeaderBoard();
		for (int i = 0; i < list.size(); i++) {
			String[] split = list.get( i ).split( "," );
			System.out.println( (i + 1) + ") " + split[0] + " with a score of " + split[1] );
		}

//		Player frankie = DBAdaptor.loginToUser( "Francisco-Gonzalezz", "SuperCoolPassWord" );
//		System.out.println( frankie.getUsername() );
	}

}
