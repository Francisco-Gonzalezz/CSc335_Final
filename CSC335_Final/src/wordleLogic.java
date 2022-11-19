import java.util.Scanner;

class wordleLogic
{
    public static void main(String[] args)
    {
        final String bgGreen = "\u001B[42m";
        final String BgYellow = "\u001B[43m";
        final String reset = "\u001B[0m";
        
        System.out.println("Let's play WORDLE!");
        
        //List of words!
        String[] words = {"DERBY", "FLANK", "GHOST", "WINCH", "JUMPS"};
        
        //picking a random word
        int wordIndex = (int)(Math.random() * words.length);
        String correct = words[wordIndex];
        
        
        Scanner sc = new Scanner(System.in);
        String guess = ""; 
        
        //loop for guesses
        for(int round = 0; round < 6; round++)
        {
            System.out.print("Guess the word!");
            guess = sc.nextLine().toUpperCase();
            
            //loop iterating through each letter of word
            for(int i=0;i<5;i++)
            {
                if(guess.substring(i, i+1).equals(correct.substring(i, i+1)))
                {
                	//characters match
                    System.out.print(bgGreen + guess.substring(i, i+1) + reset);
                }
                else if(correct.indexOf(guess.substring(i, i+1)) > -1)
                {
                	//character match but not location
                    System.out.print(BgYellow + guess.substring(i, i+1) + reset);
                }
                else
                {
                	//letter doesn't exist
                    System.out.print(guess.substring(i, i+1));
                }
            }
            
            System.out.println("");
            
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
}