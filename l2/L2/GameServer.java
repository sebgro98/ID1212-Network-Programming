import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

public class GameServer {

    public static void main(String[] args) {
        System.out.println("Creating SSLContext");

        // Load the keystore containing the self-signed certificate
        String keystorePath = "C:\\Users\\Sushil's pc\\OneDrive\\Skrivbord\\ID1212\\l2\\L2\\keystore.jks";
        char[] keystorePassword = "your_keystore_password".toCharArray();

        try {
            //obtain an instance of SSLContext using the "TLS"
            SSLContext sslContext = SSLContext.getInstance("TLS");

            // Initialize the KeyManagerFactory with the keystore
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            //to load the keystore from the specified path and password.
            kmf.init(KeyStoreUtil.getKeyStore(keystorePath, keystorePassword), keystorePassword);

            // Initialize the SSLContext with the KeyManagerFactory
            //SSLContext with the key managers obtained from the KeyManagerFactory.
            sslContext.init(kmf.getKeyManagers(), null, null);

            // Create a SSLServerSocketFactory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            System.out.println("Creating SSL ServerSocket");
            List<GameModel> gameModels = Collections.synchronizedList(new ArrayList<>());

            try (ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(1234)) {
                while (true) {
                    System.out.println("Waiting for client...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected");

                    // Create a new thread for each client
                    new HttpRequestHandler(clientSocket, gameModels).start();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//OLD GAMESERVER 
/*import java.io.IOException;
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
}*/