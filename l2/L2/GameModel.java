import java.util.Random;

public class GameModel {
    String sessionId;
    int numberOfGuesses;
    int secretNumber;
    boolean gameWon;
    boolean gameLost;

    //cookie 
    public GameModel(String generateSessionId) {
        this.sessionId = generateSessionId;
        this.numberOfGuesses = 0;
        this.secretNumber = generateSecretNumber();
        this.gameWon = false;
        this.gameLost = false; 
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    // to process the game if we have won or not. 
    public String processGuess(int guess) {
        numberOfGuesses++;

        if (guess == secretNumber) {
            gameWon = true;
            return "Congratulations! You guessed the correct number.";
        } 
        else if(numberOfGuesses >= 100){
             gameLost = true; 
            return "You have lost the game"; 
        }
        else if (guess < secretNumber) {
            return "low! Try again.";
        }  
        else {
            return "high! Try again.";
        }
    }

    private int generateSecretNumber() {
        // Generate a random secret number between 1 and 10
        return new Random().nextInt(100) + 1;
    }
}
