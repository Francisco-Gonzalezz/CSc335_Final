package db;

import java.util.List;

import player.Player;

public class testDB {

	public static void main( String[] args ) {
		Player user = DBAdaptor.loginToUser( "Francisco-Gonzalezz", "SuperCoolPassWord" );
		System.out.println( user.getGameWords() );
		System.out.println( user.getGuesses() );
		System.out.println( user.getDates() );
		
		// Initial implementation on how to parse
//		List<String> stats = DBAdaptor.getStats( user );
//		for (String stat : stats) {
//			String [] data = stat.split( "," );
//			String word = data[0];
//			int attempts = Integer.valueOf( data[1] );
//			String date = data[2];
//			System.out.println( word + " " + attempts + " " + date );
//		}

	}

}
