package bullscows;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Random;

public class Main {

    public static char indexToChar(int index) {      // Converts an index (0-35) to a character (0-9, a-z)       
        if (index < 10) {
            return (char) ('0' + index);             // 0-9
        } else {
            return (char) ('a' + (index - 10));      // a-z
        }
    }
    
    public static String generateSecretCode(int length, int possibleSymbols) {
        Random random = new Random();
        StringBuilder secretCode = new StringBuilder();
        HashSet<Integer> usedIndices = new HashSet<>();
        
        
        while (secretCode.length() < length) {              // Generate secretCode
            int index = random.nextInt(possibleSymbols);    // from 0 to possibleSymbols-1
            
            
            if (!usedIndices.contains(index)) {           // Check if in HashSet
                secretCode.append(indexToChar(index));
                usedIndices.add(index);
            }
        }
        
        return secretCode.toString();
    }
    
    public static String getPreparedMessage(int length, int possibleSymbols) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stars.append('*');
        }
        
        String range;
        if (possibleSymbols <= 10) {            
            range = "(0-" + indexToChar(possibleSymbols - 1) + ")";         // Only numbers (0-9)
        } else {           
            range = "(0-9, a-" + indexToChar(possibleSymbols - 1) + ")";    // Numbers and letters (0-9, a-z)   
        }
        
        return "The secret is prepared: " + stars + " " + range + ".";
    }
    
    public static int[] gradeGuess(String guess, String secretCode) {
        int bulls = 0;
        int cows = 0;        
        
        int[] secretFreq = new int[36];               // Count frequency of each digit in secret code and guess
        int[] guessFreq = new int[36];        
        
        for (int i = 0; i < secretCode.length(); i++) {     // First pass: count bulls and build frequency arrays
            char secretChar = secretCode.charAt(i);
            char guessChar = guess.charAt(i);
            
            if (secretChar == guessChar) {
                bulls++;
            } else {
                secretFreq[charToIndex(secretChar)]++;
                guessFreq[charToIndex(guessChar)]++;
            }
        }        
        
        for (int i = 0; i < 36; i++) {                    // Second pass: count cows
            cows += Math.min(secretFreq[i], guessFreq[i]);
        }
        
        return new int[]{bulls, cows};
    }
       
    public static int charToIndex(char c) {          // Converts a character to an index (0-9 -> 0-9, a-z -> 10-35)
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else {
            return 10 + (c - 'a');
        }
    }
    
    public static void printGrade(int bulls, int cows) {
        if (bulls == 0 && cows == 0) {
            System.out.println("Grade: None");
        } else if (bulls > 0 && cows > 0) {
            System.out.println("Grade: " + bulls + " bull(s) and " + cows + " cow(s)");
        } else if (bulls > 0) {
            System.out.println("Grade: " + bulls + " bull(s)");
        } else {
            System.out.println("Grade: " + cows + " cow(s)");
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
                
        System.out.println("Input the length of the secret code:");
        String lengthInput = scanner.nextLine();
               
        int length;
        try {
            length = Integer.parseInt(lengthInput);
        } catch (NumberFormatException e) {
            System.out.println("Error: \"" + lengthInput + "\" isn't a valid number.");
            scanner.close();
            return;
        }
               
        if (length <= 0) {
            System.out.println("Error: length must be greater than 0.");
            scanner.close();
            return;
        }
                
        System.out.println("Input the number of possible symbols in the code:");
        String symbolsInput = scanner.nextLine();
                
        int possibleSymbols;
        try {
            possibleSymbols = Integer.parseInt(symbolsInput);
        } catch (NumberFormatException e) {
            System.out.println("Error: \"" + symbolsInput + "\" isn't a valid number.");
            scanner.close();
            return;
        }
                
        if (possibleSymbols <= 0) {
            System.out.println("Error: number of possible symbols must be greater than 0.");
            scanner.close();
            return;
        }
               
        if (possibleSymbols > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            scanner.close();
            return;
        }
                
        if (length > possibleSymbols) {
            System.out.println("Error: it's not possible to generate a code with a length of " + length + " with " + possibleSymbols + " unique symbols.");
            scanner.close();
            return;
        }

        String secretCode = generateSecretCode(length, possibleSymbols);
                
        System.out.println(getPreparedMessage(length, possibleSymbols));
                
        System.out.println("Okay, let's start a game!");
        
        int turn = 1;
        boolean guessed = false;

        while (!guessed) {                                           // Game loop            
            System.out.println("Turn " + turn + ":");
            String guess = scanner.nextLine();
                        
            int[] result = gradeGuess(guess, secretCode);
            int bulls = result[0];
            int cows = result[1];
            
            printGrade(bulls, cows);
                        
            if (bulls == length) {
                guessed = true;
                System.out.println("Congratulations! You guessed the secret code.");
            }
            
            turn++;
        }
        
        scanner.close();
    }
}
