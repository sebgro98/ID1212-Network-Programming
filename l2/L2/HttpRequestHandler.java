import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.net.SocketException;


public class HttpRequestHandler extends Thread{
     // a list of gameModel to save en ny instans av spelet
    //synchronizedList to not get errors with threds
    Socket clientSocket;
 List<GameModel> gameModels;
    ArrayList<String> lines = new ArrayList<String>();
    String cookie = null;
    GameModel currGameModel = null; 
    Integer guessNum = null; 
    String guessResult = null; 
    boolean foundFavicon = false;
    boolean newGame = false;
    
    public HttpRequestHandler(Socket clientSocket, List<GameModel> gameModels) {
        this.clientSocket = clientSocket;
        this.gameModels = gameModels; 
    }

    /*
     * This methods will be called.
     */
    public void run() {
        try (
            BufferedReader request = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            PrintWriter response = new PrintWriter(this.clientSocket.getOutputStream(), false)) {
            processRequest(request, response);
            clientSocket.close();
        } catch (SocketException se) {
            // Handle SocketException (connection closed by client) gracefully
            System.out.println("Connection closed by client.");
        }    
            catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processRequest(BufferedReader request, PrintWriter response) throws IOException {
        System.out.println("Request: " + request);
        
        /**
         * the issue is related to the fact that the 
         * BufferedReader is reading the input stream indefinitely until it 
         * reaches the end. In HTTP, the end of the request is typically 
         * identified by an empty line. The loop while ((line = request.readLine()) != null) 
         * will keep reading until the end of the stream, and it seems like the end of the 
         * stream might not be reached in your current implementation.
         */
        try {
            String line;
            while ((line = request.readLine()) != null && !line.isEmpty()) {
                // Save the line in the list
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the IOException appropriately
        }

        System.out.println("Request2: " + lines);

        
        
        /*
         * we go through the string list line for line. 
         */
        for (String line : lines) {
    
            if (line.contains("favicon.ico")) {
                foundFavicon = true;
                System.out.println("Found 'favicon.ico' in the line: " + line);
                // Do something when 'favicon.ico' is found
                break; // If you want to stop searching after the first occurrence
            }
            if (line.startsWith("Cookie:")) {
                String[] keyValue = line.substring("Cookie:".length()).trim().split(";")[0].split("=", 2);
                if (keyValue.length == 2) {
                    cookie = keyValue[1].trim();
                    break; // Stop searching once the cookie is found
                }
            }
            // the user has subbmited a guess number 
            if (line.contains("GET /?guess=") && !newGame) {
                String[] parts = line.split(" ");
                System.out.println(line);
                System.out.println(parts[1]);
                int index = parts[1].indexOf("/?guess=") + "/?guess=".length();
                guessNum = Integer.parseInt( parts[1].substring(index));
                System.out.println(guessNum);
            }

            //if the requsest contains newgame the we start a newgame 
            if (line.contains("/newGame")) {
                newGame = true; 
            }
        }
        System.out.println("cookie: " + cookie + foundFavicon);

        synchronized (gameModels) {
            if (!foundFavicon) {
                // Do something when 'favicon.ico' is not found in any line
                // pekare for att gå igenom alla gememodeller. 
                Iterator<GameModel> iterator = gameModels.iterator();
                while (iterator.hasNext()) {
                    GameModel gameModel = iterator.next();

                    // same cookie meanes the user is already playing
                    if (gameModel.sessionId != null && gameModel.sessionId.equals(cookie)) {
                        currGameModel = gameModel;
                        System.out.println("Found the curr game model");
                    }

                    if (newGame && gameModel.sessionId.equals(cookie)) {
                        iterator.remove();
                    }
                }

                // when connecting for the first time. 
                if (currGameModel == null || newGame) {
                    cookie = UUID.randomUUID().toString();
                    currGameModel = new GameModel(cookie);
                    System.out.println("created new game model");
                    gameModels.add(currGameModel);
                }

                // if the user has guessed a nubmer, 
                if (guessNum != null) {
                    guessResult = currGameModel.processGuess(guessNum);
                    System.out.println("Got the num: " + guessResult + " " + guessNum);
                    String htmlResponse = HTMLResponseGenerator.generateHTMLResponse(currGameModel, guessResult);
                    sendHttpResponse(response, "text/html", htmlResponse);
                } else {
                    // Send the initial HTML response
                    System.out.println("num is null");
                    String htmlResponse = HTMLResponseGenerator.generateHTMLResponse(currGameModel, null);
                    sendHttpResponse(response, "text/html", htmlResponse);
                }
            }
        }

           /*  if (lines != null && lines.contains("GET")) {
                // Extract the requested document
                requestedDocument = lines.contains(" ")[1];
                System.out.println("Request3: " + requestedDocument);

                // Ignore additional request for favicon.ico
                if (!requestedDocument.equals("/favicon.ico")) {
                    // Process the guess if the request contains a guess parameter
                    if (requestedDocument.contains("/?guess=")) {
                        int guess = Integer.parseInt(requestedDocument.substring(8));
                        String guessResult = gameModel.processGuess(guess);


                        String htmlResponse = HTMLResponseGenerator.generateHTMLResponse(gameModel, guessResult);
                        sendHttpResponse(response, "text/html", htmlResponse);
                    } else {
                        // Send the initial HTML response
                        String htmlResponse = HTMLResponseGenerator.generateHTMLResponse(gameModel, null);
                        sendHttpResponse(response, "text/html", htmlResponse);
                    }
                }
            }
            */
    
    }

    private void sendHttpResponse(PrintWriter response, String contentType, String content) {
        response.println("HTTP/1.1 200 OK");
        response.println("Set-Cookie: sessionId=" + cookie);
        response.println("Server: Guessing Game Server");
        response.println("Content-Type: " + contentType);
        response.println();
        response.println(content);  // Use the dynamic content generated by HTMLResponseGenerator
        response.flush();
    }
    
    
}
