package db;

import java.util.List;
import player.Player;

public class testDB {

	public static void main( String[] args ) {
		DBAdaptor.getLeaderBoard();
		Player frankie = new Player( "panchothecool852", "SuperCoolPassWord", "Frankie", "Gonzalez" );
		Player brian = new Player( "Briann-vu", "TopSecret", "Brian", "Vu" );
		brian.switchTheme();
		brian.setBio( "This is something about me" );
		DBAdaptor.registerNewUser( frankie );
		DBAdaptor.registerNewUser( brian );
		DBAdaptor.getLeaderBoard();
		// DBAdaptor.updateUser( player );
	}

}
