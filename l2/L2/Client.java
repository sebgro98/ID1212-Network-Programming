import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
    // Variable to store the average number of guesses across multiple games
    public static double avgNumberofGuess;

    public static void main(String[] args) {
        // Enable cookie management
        CookieHandler.setDefault(new CookieManager());

        // Play 100 games
        for (int gameCount = 1; gameCount <= 100; gameCount++) {
            System.out.println("Game " + gameCount);

            // Play a single game and get the number of guesses
            int correctGuess = playGame();

            // Accumulate the number of guesses for calculating the average later
            avgNumberofGuess += correctGuess;

            // Print the average after each game
            System.out.println("Average number of guesses so far: " + avgNumberofGuess / gameCount);

            // Print the number of guesses in the current game
            System.out.println("Number of guesses in this game: " + correctGuess);
        }

        // Print the final average number of guesses across all games
        System.out.println("Final Average number of guesses: " + avgNumberofGuess / 100);
    }

    // Method to play a single game
    private static int playGame() {
        // Initialize the range for guessing
        int low = 1;
        int high = 100;
        int guessCount = 0; // Counter for the number of guesses made in this game

        // Continue guessing until the correct number is found
        while (low <= high) {
            // Calculate the midpoint for the current range
            int mid = (low + high) / 2;

            // Make a guess and get the server's response
            System.out.println("Making a guess: " + mid);
            String response = makeHttpRequest(Integer.toString(mid));

            // Increment the guess count
            guessCount++;

            // Check the server's response and update the range accordingly
            if (response.contains("Congratulations!")) {
                System.out.println("Congratulations! Guessed the correct number: " + mid);

                // Start a new game after a correct guess
                makeHttpRequest2("newGame");

                // Return the number of guesses made in this game
                return guessCount;
            } else if (response.contains("low! Try again")) {
                low = mid + 1;
            } else if (response.contains("high! Try again")) {
                high = mid - 1;
            }
        }

        // This should not happen if the server responds correctly
        return -1;
    }

    // Method to make an HTTP request with a guess
    private static String makeHttpRequest(String guess) {
        try {
            // Create an HttpClient with cookie support
            HttpClient client = HttpClient.newBuilder()
                    .cookieHandler(CookieHandler.getDefault())
                    .build();

            // Define the URI for the guess endpoint
            URI uri = URI.create("http://localhost:1234/?guess=" + guess);

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Send the request and get the server's response
            /*specifies that the response body should be handled as a String. The HttpResponse object contains information about the server's response, including the status code, headers, and the response body. */
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); 

            // Print the response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body:\n" + response.body());

            // Return the server's response body
            return response.body();
        } catch (Exception e) {
            // Print the stack trace in case of an exception and return an empty string
            e.printStackTrace();
            return "";
        }
    }

    // Method to make an HTTP request for starting a new game
    private static void makeHttpRequest2(String newGame) {
        try {
            // Create an HttpClient with cookie support
            HttpClient client = HttpClient.newBuilder()
                    .cookieHandler(CookieHandler.getDefault())
                    .build();

            // Define the URI for the new game endpoint
            URI uri = URI.create("http://localhost:1234/" + newGame);

            // Build the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Send the request and get the server's response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the response details
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body:\n" + response.body());
        } catch (Exception e) {
            // Print the stack trace in case of an exception
            e.printStackTrace();
        }
    }
}
