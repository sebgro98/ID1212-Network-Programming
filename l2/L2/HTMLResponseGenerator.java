public class HTMLResponseGenerator {

    public static String generateHTMLResponse(GameModel model, String guessResult) {

        StringBuilder htmlResponse = new StringBuilder();
        //appentd string to the stringbuilder. 
        htmlResponse.append("<html><head><title>Guessing Game</title></head><body>");

        htmlResponse.append("<h1>Welcome to the Guessing Game! You can only guess three times. Try to guess a number between 1 to 10</h1>");
        htmlResponse.append("<p>Number of guesses: ").append(model.getNumberOfGuesses()).append("</p>");

        if (guessResult != null) {
            htmlResponse.append("<p>").append(guessResult).append("</p>");
        }

        if (model.isGameWon()) {
            htmlResponse.append("<form method=\"GET\">");
            htmlResponse.append("<p>Congratulations! You guessed the correct number in ").append(model.getNumberOfGuesses()).append(" guesses!</p>");
            htmlResponse.append("<p><a href=\"/newGame\">Play Again</a></p>");      
            htmlResponse.append("</form>");
        }
        else if (model.isGameLost()){
            htmlResponse.append("<form method=\"GET\">");
            htmlResponse.append("<p>You have guessed 3 times and lost the game </p>");
            htmlResponse.append("<p>The corret number was: ").append(model.secretNumber).append(" </p>");
            htmlResponse.append("<p><a href=\"/newGame\">Play Again</a></p>");
            htmlResponse.append("</form>");
        }
        else {
            htmlResponse.append("<form method=\"GET\" action=\"/\" >");
            htmlResponse.append("Enter your guess: <input type=\"text\" name=\"guess\">");
            htmlResponse.append("<input type=\"submit\" value=\"Submit\">");
            htmlResponse.append("</form>");
        }

        htmlResponse.append("</body></html>");

        return htmlResponse.toString();
    }
}
