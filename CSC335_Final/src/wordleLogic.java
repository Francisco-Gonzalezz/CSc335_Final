import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class wordleLogic
{
	public static void main(String[] args) throws IOException
	{
		final String bgGreen = "\u001B[42m";
		final String BgYellow = "\u001B[43m";
		final String reset = "\u001B[0m";

		System.out.println("Let's play WORDLE!");
		
		List<String> listOfString = new ArrayList<String>();
		
		BufferedReader bf = new BufferedReader(new FileReader("src/words.txt"));

		String line = bf.readLine();

		while (line != null)
		{
			listOfString.add(line);
			line = bf.readLine();
		}

		// closing bufferreader object
		bf.close();

		// storing the data in arraylist to array
		String[] words
		= listOfString.toArray(new String[0]);


		//List of words!
		//Read from textfile
		//Input exception, check if word exists in game - TODO

		//String[] words = {"DERBY", "FLANK", "GHOST", "WINCH", "JUMPS"};

		//picking a random word
		int wordIndex = (int)(Math.random() * words.length);
		String correct = words[wordIndex];

		Scanner sc = new Scanner(System.in);
		String guess = ""; 

		//Input exception, add only characters
		//Input exception, cut when exceeds more than 5 characters
		//Input exception, add more words to complete 5 characters

		System.out.print("Guess the word!\n");

		//loop for guesses
		for(int round = 0; round < 6; round++)
		{
			//System.out.println();
			guess = sc.nextLine().toUpperCase();

			if(checkWord(guess))
			{
				//loop iterating through each letter of word
				for(int i = 0; i < 5; i++)
				{
					if(guess.substring(i, i + 1).equals(correct.substring(i, i + 1)))
					{
						//characters match
						System.out.print(bgGreen + guess.substring(i, i + 1) + reset);
					}
					else if(correct.indexOf(guess.substring(i, i + 1)) > -1)
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
			else
			{
				round--;
				System.out.println("Invalid String");
			}
			
			//if guess is correct
			if(guess.equals(correct))
			{
				System.out.println("Win Win!");
				break;
			}

		}

		//print if guess is wrong
		if(!guess.equals(correct))
		{
			System.out.println("Wrong! Correct word: " + correct);
		}
	}

	public static boolean checkWord(String word) 
	{
		return word.length()>=5 && word.matches("[a-zA-Z]+");
	}

}