package db;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import player.Player;

public class testDB {

	public static void main( String[] args ) {
		LocalDate date = LocalDate.now();
		String month = date.getMonth().toString();
		System.out.println( month.substring( 0, 1 ).toUpperCase() + month.substring( 1 ).toLowerCase() );
		System.out.println( date.getDayOfMonth() );
		System.out.println( date.getYear() );
	}

}
