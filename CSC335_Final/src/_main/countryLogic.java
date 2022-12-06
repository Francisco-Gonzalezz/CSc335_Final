package _main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class countryLogic
{
	static String correctWord;
	static String[] names = 
		{
			"CHILE", "CHINA", "HAITI", "INDIA", 
			"ITALY", "JAPAN", "MALTA", "NAURU", 
			"NEPAL", "PALAU", "QATAR", "SAMOA", 
			"SPAIN", "SYRIA", "TONGA", "YEMEN", 
			"BENIN", "EGYPT", "GABON", "GHANA", 
			"KENYA", "LIBYA", "NIGER", "SUDAN"
		};

	public static void countryReader()
	{		
		List<String> listOfString = new ArrayList<String>(Arrays.asList(names));

		String[] words = listOfString.toArray(new String[0]);
		int wordIndex = (int)(Math.random() * words.length);
		correctWord = words[wordIndex];
		
		System.out.println(correctWord);
	}
	
	public static void main(String[] args)
	{
		countryReader();
		
		for(int i = 0; i < 5; i++)
		{
			System.out.println(i);
			getTheWord();
		}
	}
	
	public static void getTheWord()
	{
		final String bgGreen = "\u001B[42m";
		final String BgYellow = "\u001B[43m";
		final String reset = "\u001B[0m";

		Scanner sc = new Scanner(System.in);
		String guess = sc.nextLine().toUpperCase();
		System.out.print(check_for_word(guess));
		if(checkWord(guess) && check_for_word(guess))
		{
			System.out.print(check_for_word(guess));
			//loop iterating through each letter of word
			for(int i = 0; i < 5; i++)
			{
				if(guess.substring(i, i + 1).equals(correctWord.substring(i, i + 1)))
				{
					//characters match
					System.out.print(bgGreen + guess.substring(i, i + 1) + reset);
				}
				else if(correctWord.indexOf(guess.substring(i, i + 1)) > -1)
				{
					//character match but not location
					System.out.print(BgYellow + guess.substring(i, i + 1) + reset);
				}
				else
				{
					//letter doesn't exist
					System.out.print(guess.substring(i, i + 1));
				}
			}
			System.out.println("");
		}


	}
	
	//REMOVE AFTER ADDING W UI
	public static boolean checkWord(String word) 
	{
		return word.length()>=5 && word.matches("[a-zA-Z]+");
	}
	
    public static boolean check_for_word(String word) 
    {
    	if(Arrays.asList(names).contains(word.toUpperCase()))
    	{
    		return true;
    	}
    	else
    		return false;
    }

}
