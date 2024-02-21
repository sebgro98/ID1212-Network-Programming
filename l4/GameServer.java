import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameServer {

    public static void main(String[] args) {
        System.out.println("Creating Serversocket");
        // a list of gameModel to save en ny instans av spelet
        //synchronizedList to not get errors with threds
        List<GameModel> gameModels = Collections.synchronizedList(new ArrayList<>());
        ServerSocket serverSocket;
        Socket clientSocket;
        try{
            serverSocket = new ServerSocket(1234);
            while (true) {
                System.out.println("Waiting for client...");
                clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                // Create a new thread for each client
                new HttpRequestHandler(clientSocket, gameModels).start(); 
              
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}