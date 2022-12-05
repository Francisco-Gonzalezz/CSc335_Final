package _main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ui.wordleGameBoard.WordleGameBoardUI;

public class wordleLogic 
{
	public static String correctWord;
	static String filename = "/Users/adityagupta/Desktop/Wordle/words.txt";
	
	public static void fileReader(String filename) throws IOException
	{
		List<String> listOfString = new ArrayList<String>();

		BufferedReader bf = new BufferedReader(new FileReader(filename));

		String line = bf.readLine();

		while (line != null)
		{
			listOfString.add(line.toUpperCase());
			line = bf.readLine();
		}

		// closing bufferreader object
		bf.close();

		// storing the data in arraylist to array
		String[] words = listOfString.toArray(new String[0]);
		int wordIndex = (int)(Math.random() * words.length);
		correctWord = words[wordIndex];
		
		
		System.out.println(correctWord);
	}
	
	public static void beginGame() {
		try {
			fileReader(filename);
		} catch (Exception e) {
			
		}
	}
	
	public static void mains(String[] args) throws IOException
	{
		
		for(int i = 0; i < 5; i++)
		{
			System.out.println(i);
			getTheWord(correctWord, null);
		}
	}
	
	public static void getTheWord(String correctWord, String guess)
	{
		final String bgGreen = "\u001B[42m";
		final String BgYellow = "\u001B[43m";
		final String reset = "\u001B[0m";

		//loop iterating through each letter of word
		for(int i = 0; i < 5; i++)
		{
			if(guess.substring(i, i + 1).equals(correctWord.substring(i, i + 1)))
			{
				//characters match
				WordleGameBoardUI.ui.setLetterStage(WordleGameBoardUI.ui.activeRow, i, KeyStage.InWordRightPlace);
			}
			else if(correctWord.indexOf(guess.substring(i, i + 1)) > -1)
			{
				//character match but not location
				WordleGameBoardUI.ui.setLetterStage(WordleGameBoardUI.ui.activeRow, i, KeyStage.InWordWrongPlace);
			}
			else
			{
				//letter doesn't exist
				WordleGameBoardUI.ui.setLetterStage(WordleGameBoardUI.ui.activeRow, i, KeyStage.NotInWord);
			}
		}
	}
	
	public static boolean checkWord(String word) 
	{
		return word.length()>=5 && word.matches("[a-zA-Z]+");
	}
	
    public static boolean check_for_word(String word) 
    {
        try 
        {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String str;
            while ((str = in.readLine()) != null) 
            {
                if (str.indexOf(word.toLowerCase()) != -1) 
                {
                    return true;
                }
            }
            in.close();
        } catch (IOException e)
        {
        	
        }
        return false;
    }
	
}
