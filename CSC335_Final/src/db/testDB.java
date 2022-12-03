package db;

import java.util.List;
import java.util.Map;

import player.Player;

public class testDB {

	public static void main( String[] args ) {
		Player player = new Player( "test", "passw0rd", "First", "Last" );
		DBAdaptor.registerNewUser( player );
	}

}
