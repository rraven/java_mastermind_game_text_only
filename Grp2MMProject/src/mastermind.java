import java.util.Scanner; // for scanner class
import java.io.*; // needed for File I/O classes p. 266 +++
import java.util.Arrays; // needed to work with arrays
import java.util.HashSet;
import java.util.Random; // for random number

/**
 * 
 * Write a program to implement a text-based/menu-driven Mastermind game.
 * The game will include a menu from which the user will choose:
 * See Rules, Play Game, See Highest Score, Quit
 * When the user chooses to play a game, the computer will generate a 
 * 3 position code using 5 colors (Red, Blue, Green, Yellow, White).
 * The player has 10 guesses to break the code and with each guess the program
 * will provide feedback via a pin system.
 * If they win, their score is compared to the previous best (if it exists)
 * and updated if their score is better or if they are the first to win.
 * 						
 * 
 * Algorithm used:
 * use do while loop to continually obtain menu choice from user
 * use switch case framework to respond to user's choices
 * 1. show rules
 * 		call method to display rules
 * 2. play game
 * 		call method to get player's name
 * 		call method to get randomly generated computer code
 * 		initialize 2D string to store guess and pin feedback
 * 		initialize score = 0 (score is number of rounds)
 * 		while loop for 10 guesses
 * 			call method to get guess
 * 				call method to validate guess
 * 			put guess into array
 * 			call method to compare guess with code returns
 * 				feedback in an array
 * 			add guess array and pin array to 2D array 
 * 			provide feedback on success of guess with 2D array
 * 		if win, display congrats message
 * 			call method to check for high score file &
 * 				create it if need & display creation message
 * 				compare current winning score and update
 * 			call method to display high score
 * 		if lose (10th guess), display losing message
 * 3. display high score
 * 		call method to display high score & display high
 * 			score or message that no one has won yet
 * 4. exit display goodbye message
 * default display try again message
 * 
 *     Input          |     Processing           |          Output
 *     
 * menu choice as int | launch menu choice       | none
 * name as String     | store name with score    | display with score
 * guess as String    | compare w/code		     | provide pincode feedback
 * 					  | determine win/lose       | provide win/lose feedback	
 * 
 * @author rraven
 *
 */
public class mastermind {

	public static void main(String[] args) throws IOException {
		
		// create a scanner object for keyboard input
	    Scanner input = new Scanner(System.in);
	    
	    // declare variables
	    int  choice; // menu choice
		int score = 0; // initialize score/number of guesses 
		
        do {
            showMenu(); // call method to show menu
            choice=chooseOption(input); // get user's choice
            
	    	// use switch statement for menu choices
            switch (choice) {
                case 1: // display rules & return to main menu
                	showRules();
                    break;
                    
                case 2: // play game & return to main menu
                	String name = getName(input); // gets player's name
                	String[] computerCode = getRandomCode(); // gets 3 color computer code   	
        	    	
                	// initialize 2D array with 10 row and 2 columns
                	String[][] guessAndPinCode = new String[10][2];
                	
                	// while loop gets guess, checks it, gives feedback
                    while (score < 10 ) { // limits to 10 guesses
                    	score++; 
                    	
                    	// call method guess() to get user's guess and validate
                    	String guess = guess(input);
                    	System.out.println("\nGuess # " + score + " : " + guess + "\n");

                    	// puts letters from guess into array
                    	String[] guessArray = guess.split(""); 
                    	
                    	// call method to get pin code feedback
                    	String[] pinCode = pinCode(computerCode, guessArray);

                    	// add guessArray and pinCode to 2D array
                    	guessAndPinCode[score-1][0] = Arrays.toString(guessArray);
                    	guessAndPinCode[score-1][1] = Arrays.toString(pinCode);
                    	// provides feedback 
                    	System.out.println("Your Guess" + "    Pin Feedback");
                    	for (int i = 0; i < score ; i++) {
                    	    for (int j = 0 ; j < guessAndPinCode[i].length ; j++) {
                    	        System.out.print(guessAndPinCode[i][j] + "     ");
                    	    }
                    	    System.out.println();
                    	} // closes for loop
                	// if win call win method
                    if (Arrays.equals(computerCode, guessArray)) {
                    	//win = true;
                        System.out.println("\nCongrats on your win, " + name + ". You scored: " + score+".");
                    	highScore(score, name);
                    	displayHighScore();
                    	break;
                        } // closes if
                    // if lose display message
                	if(score==10)
                		System.out.println("\nBetter luck next time!");
                    } // closes while loop
                    break;
                    
                case 3: // display high score & return to main menu         	
                	displayHighScore();
                	break;
             	
                case 4: // exit
                    System.out.println("\nSee you next time.");
                    break;
                default:
                    System.out.println("\nInvalid option, please try again.");
                    break;
            } // closes switch cases
        } while (choice != 4); // shows the menu again for all other choices !=4
	} // closes main method
	
	/**
	 * This method asks a user for their menu choice   	    		
	 * @param input is the user input 1, 2, 3, 4
	 * @return choice the menu choice of the user
	 */
	public static int chooseOption(Scanner input) {
		int choice;
		System.out.print("\nPlease enter your choice by typing 1, 2, 3, or 4: ");
		choice = input.nextInt();
		while(choice<1 || choice>4) {
			System.out.println("\nThat isn't a valid choice, please try again.");
			System.out.println("Please choose 1, 2, 3, or 4: ");
			choice=input.nextInt();		
	    } // closes while loop
	    return choice;
	} // closes chooseOption method
	
	/**
	 * This method asks user for their name and returns it
	 * @param input from Scanner
	 * @return name to switch case 2
	 */
	public static String getName(Scanner input) {
		// ask player for their name and wish them luck 
		System.out.println("\nWhat is your name? ");
		String name = input.next();
		System.out.println("Good Luck, " + name + ".\n");
    	return name; // returns player's name to switch case 2
	} // closes getName method
	
	/**
	 * This method generates a 3 color code and puts it into an array
	 * @return array computerCode is returned to switch case 2
	 */
	static Random rng=new Random();
	public static String[] getRandomCode() {
		String[] colorKey = {"R", "G", "B", "Y", "W"};
		String[] computerCode = new String[3];
		for(int i=0;i<3;i++) {
			computerCode[i] = colorKey[rng.nextInt(5)];
		} // close for loop   	
		return computerCode; // returns 3 color code array to switch case 2
	} // closes getRandomCode
	
/**
 * This method obtains player's guess
 * @param input a String
 * @return guess a String
 */
    public static String guess(Scanner input) {
    	String guess;	
    	System.out.println("\nR = Red, B = Blue, G = Green, W = White, Y = Yellow");
		System.out.println("Please enter your 3 letter guess (RBB for example): ");
		guess = input.next(); // gets 3 letter guess
		guess = guess.toUpperCase(); // puts in all upper case
		int guessLength = guess.length();
		while (guessLength != 3 || !validLetters(guess)) {
			System.out.println("Invalid guess. Please try again.");
	    	System.out.println("R = Red, B = Blue, G = Green, W = White, Y = Yellow");
			System.out.println("Please enter your 3 letter guess (RBB for example): ");
			guess = input.next(); // gets 3 letter guess
			guess = guess.toUpperCase(); // puts in all upper case
			guessLength = guess.length();
		}
		return guess;
    } // closes guess method   
  
  /**
   * This method checks if guess is valid
   * @param guess 
   * @return true if guess valid, false if not
   */
  public static boolean validLetters(String guess) {
  for(int i = 0; i < 3; i++) {
      if (guess.charAt(i) != 'R' &&
              guess.charAt(i) != 'G' &&
              guess.charAt(i) != 'B' &&
              guess.charAt(i) != 'W' &&
              guess.charAt(i) != 'Y') {
          return false;
      } // closes if
  } // closes for
  return true;
} // closes validLetters method
    
	/**
	 * This checks for highScore.txt file, creates it, reads and writes to it  
	 * @param score the score for the game won
	 * @param name the player's name for the game won
	 * @throws IOException in case their is an input/output problem
	 */
    public static void highScore(int score, String name) throws IOException {
    	// check if highScore.txt exists & create it if it doesn't
    	File file = new File("highScore.txt");
    	if (!file.exists()) {
    		System.out.println("\nFile not found so new highScore.txt file created");
        	FileWriter fwriter = new FileWriter("highScore.txt", true);
        	fwriter.close();
        	// open the file for writing and enter score and name 
        	PrintWriter outputFile = new PrintWriter("highScore.txt");
        	outputFile.print(score + " ");
        	outputFile.print(name + " ");
        	outputFile.close();
    	} else {
    		// compare scores and replace if needed
        	Scanner inputFile = new Scanner(file);
        	int leaderboardScore = inputFile.nextInt();
        	// if current game score is less or equal to leaderboard score
        	if (score <= leaderboardScore) {
        		// overwrite current file and enter score and name of current game
            	PrintWriter outputFile = new PrintWriter("highScore.txt");
            	outputFile.print(score + " ");
            	outputFile.print(name + " ");
            	outputFile.close();
        	} // closes nested if loop
    	inputFile.close();	
    	} //closes if-else loop
    }//closes method
    
    /**
     * This displays the highscore name and score
     * @throws IOException in case their is a problem with the file
     */
    public static void displayHighScore() throws IOException {
    	// if file doesn't exist, display message to user
    	File file = new File("highScore.txt");
    	if (!file.exists()) {
    		System.out.println("\nNo one has won yet.");
    	} else {
    	// read and display high score
    	Scanner inputFile = new Scanner(file);
    	String leaderboardScore = inputFile.next();
    	String leaderboardName = inputFile.next();
    	System.out.println("\n" + leaderboardName + " is the current leader with a score of " + leaderboardScore + ".");
    	inputFile.close();
    	} // closes if-else    	
    } //closes displayHighScore method
     
    /**
     * This shows the main menu to the user
     */
	public static void showMenu() {
    	System.out.println("\nMenu:");
        System.out.println("1) See rules");
        System.out.println("2) Play Game");
        System.out.println("3) See Highest Score");
        System.out.println("4) Quit");
	} // closes showMenu method
	
	/**
	 * This displays the rules.txt file to the user
	 * @throws FileNotFoundException in case problem getting/reading file
	 */
	public static void showRules() throws FileNotFoundException {
		// open "rules.txt" 
    	File file = new File("rules.txt");
    	Scanner inputFile = new Scanner(file);
		// read lines from the file until no more are left 
    	while (inputFile.hasNext()) {
		// read the next name using ruleLine as temp variable                		
        String ruleLine = inputFile.nextLine();
        // display the last name read
    	System.out.println(ruleLine); }
    // close file	
	inputFile.close();
	} // closes showRules method
	
	/**
	 * This method tests the guess against the computer's code
	 * @param computerCode the array of the computer's code
	 * @param guessArray the array of the user's guess
	 * @return pinCode, an array of R,W,O feedback for guess
	 */
	public static String [] pinCode(String[] computerCode, String[] guessArray) {
		int redPin = exactMatches(computerCode, guessArray);
		// call remove duplicates to get unique elements in arrays
    	String [] cc = removeDuplicates(computerCode);
    	String [] ga = removeDuplicates(guessArray);
    	int letters = exactLetters(cc, ga);

    	int whitePin = letters - redPin;
    	// initialize pinCode array
    	String pinCode [] = {"O", "O", "O"};
    	// use values of redPin and whitePin to populate pinCode
    	if(redPin>0) {
    		for(int i=0; i < redPin; i++)
    			pinCode[i] = "R";
    	}
    	if(whitePin>0) {
    		for(int i = redPin; i < redPin+whitePin; i++)
    			pinCode[i] = "W";
    	}
		return pinCode;
	} // closes pinCode method
	
	/**
	 * This program checks if guess is right about any letters
	 * @param computerCode the array of the computer's code
	 * @param guessArray the array of the user's guess
	 * @return exactLetters, int of number of letters
	 */
	public static int exactLetters(String[] computerCode, String[] guessArray) {
		int exactLetters = 0;
		for(int i= 0; i < guessArray.length; i++) {
			for(int j=0; j < computerCode.length; j++) {
				if(computerCode[j].equals(guessArray[i])) {
					exactLetters++;				
				} // closes if loop				
			} // closes inner for
		} // closes for		
		return exactLetters;		
	} // closes exactLetters method
	
	/**
	 * This method removes the duplicates from the arrays
	 * @param distinctArray, an array that may have duplicates
	 * @return temp, an array withou duplicates
	 */
	public static String[] removeDuplicates(String[] distinctArray) {
			String [] temp = new HashSet<String>(Arrays.asList(distinctArray)).toArray(new String[0]);	    	
	    	return temp;
		} // closes removeDuplicates method
	
	/**
	 * This method checks for exact letter/position matches
	 * @param computerCode the array of the computer's code
	 * @param guessArray the array of the user's guess
	 * @return exactMatches, an int of # of exact matches
	 */
	public static int exactMatches(String[] computerCode, String[] guessArray) {
		int exactMatches = 0;
		for(int i= 0; i < guessArray.length; i++) {
			//System.out.println(computerCode[i]+guessArray[i]);
			if(computerCode[i].equals(guessArray[i])) {
				exactMatches++;				
			} // closes if loop
		} // closes for		
		return exactMatches;		
	} // closes exactMatches method
} // program close
