import java.io.Serializable;

public class GuessBean implements Serializable {
    private int randomNumber;
    private int userGuess;

    public GuessBean() {
        // Generera ett slumpmässigt nummer vid skapandet av instansen
        randomNumber = (int) (Math.random() * 100) + 1;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public int getUserGuess() {
        return userGuess;
    }

    public void setUserGuess(int userGuess) {
        this.userGuess = userGuess;
    }

    public String getResult() {
        if (userGuess == randomNumber) {
            return "Correct! The number was " + randomNumber;
        } else if (userGuess < randomNumber) {
            return "Try higher!";
        } else {
            return "Try lower!";
        }
    }
}
