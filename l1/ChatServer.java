import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private ServerSocket serverSocket; // object for listening to incoming connections
    private List<ClientHandler> clients = new ArrayList<>(); //a list clients to keep track of connected clients.

    /**
     *  constructor for the ChatServer class.
     * @param port: initializes the server socket on the specified port
     */
    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(8080); //create an instance of ChatServer on port 8080
        server.start();
    }

    /**for accepting incoming client connections in a loop
     * When a client connects, it accepts the socket connection and creates a Socket object.
    */
    public void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                System.out.println("Host Address: " + socket.getLocalAddress());
                System.out.println("Client Address: " + socket.getInetAddress());
                System.out.println("Host port: " + socket.getLocalPort());
                System.out.println("Client port: " + socket.getPort());
                
                //object to handle the client and adds it to the list of connected clients.
                ClientHandler client = new ClientHandler(socket, this);
                clients.add(client);
                // This allows the server to handle multiple clients concurrently.
                new Thread(client).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //The broadcastMessage method is used to send a message from one client to all other connected clients
    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(sender.getClientName() + ": " + message);
            }
        }
    }

    //The removeClient method is used to remove a client from the list of connected clients
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }


}


//represents a client and handles communication with individual clients
//whose instances are intended to be executed by a thread
class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private String clientName;
    private PrintWriter out;

    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public String getClientName() {
        return clientName;
    }
    
    /*
     * The sendMessage method is used to send a message to the client. It writes the message to the PrintWriter associated with the client's socket.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);// for sending messages to the client
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//reading messages from the client.

            out.println("Welcome to the chat. Please enter your name:");
            out.flush();
            clientName = in.readLine();
            out.println("Hello, " + clientName + "! Type 'leave' to leave the chat.");
            String message;
            while ((message = in.readLine()) != null) {
                if ("leave".equals(message)) {
                    break;
                }
                server.broadcastMessage(message, this);
            }

            out.println("Goodbye, " + clientName + "!");
            server.removeClient(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
