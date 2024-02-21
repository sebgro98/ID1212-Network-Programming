import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.Scanner;

public class client2 {
    public static void main(String[] args) {
        Socket s;
        OutputStreamWriter osw = null;
        InputStreamReader isr = null;
        Scanner scanner = new Scanner(System.in);
        BufferedWriter bfw = null;
        BufferedReader bfr = null;

        try {
            s = new Socket("localhost", 8080); // Socket object
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
                    
                if (message.equals("Exit")) {
                    break;
                }
            }
        } catch (java.net.UnknownHostException e) {
            System.out.print(e.getMessage());
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        } finally {
            try {
                if (bfw != null) {
                    bfw.close();
                }
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }
        }
    }
}

class MessageListener implements Runnable {
    private BufferedReader reader;

    public MessageListener(BufferedReader reader) {
        this.reader = reader;
    }

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
            System.out.println("Error reading server message: " + e.getMessage());
        }
    }
}
