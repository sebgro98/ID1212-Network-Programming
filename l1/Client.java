import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        //a Socket object called s that will be used to establish a socket connection with the server.
        Socket s;
        OutputStreamWriter osw = null;
        InputStreamReader isr = null;
        Scanner scanner = new Scanner(System.in);
        BufferedWriter bfw = null;
        BufferedReader bfr = null;

        try {
            s = new Socket("localhost", 8080); // Creates a Socket object and establishes a socket connection with a server running on localhost at port 8080.
            osw = new OutputStreamWriter(s.getOutputStream()); // to send the message
            isr = new InputStreamReader(s.getInputStream()); // to receive the message
            bfw = new BufferedWriter(osw);
            bfr = new BufferedReader(isr);
            System.out.println("From server: "+ bfr.readLine());
            String message = scanner.nextLine();
            bfw.write(message);
            bfw.newLine(); // Add a newline character to separate messages
            bfw.flush(); // Flush the writer to send the message immediately
            System.out.println("From server: "+ bfr.readLine());
            // Create a thread to listen to incoming messages from the server
            Thread messageListener = new Thread(new MessageListener(bfr));
            messageListener.start();

            while (true) {
                message = scanner.nextLine();
                bfw.write(message);
                bfw.newLine();
                bfw.flush();
                if (message.equals("leave")) {
                    s.close();
                    scanner.close();
                    System.out.println("you have left the chat");
                    break;
                }
            }
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage()+ " server down");
        }
    }
}

/*
 * this class is responsible for running a background thread that continuously listens for incoming messages from the server
 */
class MessageListener implements Runnable {
    private BufferedReader reader;

    public MessageListener(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     *  method is an overridden method from the Runnable interface.
     */
    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readLine();
                if (message != null) {
                    System.out.println(message);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading server message server down: " + e.getMessage());
        }
    }
}
