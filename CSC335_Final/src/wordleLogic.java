import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class wordleLogic 
{
	static String correctWord;
	static String filename = "src/words.txt";
	
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
	
	public static void main(String[] args) throws IOException
	{
		fileReader(filename);
		
		for(int i = 0; i < 5; i++)
		{
			System.out.println(i);
			getTheWord(correctWord);
		}
	}
	
	public static void getTheWord(String correctWord)
	{
		final String bgGreen = "\u001B[42m";
		final String BgYellow = "\u001B[43m";
		final String reset = "\u001B[0m";

		Scanner sc = new Scanner(System.in);
		String guess = sc.nextLine().toUpperCase();
		
		if(checkWord(guess) && check_for_word(guess))
		{
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
