import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Base64;

public class SMTPClient {

    private static BufferedReader inputReader;
    private static String recipientEmail;
    private static String emailSubject;
    private static PrintWriter outputWriter;
    private static Socket socket;
    private static String[] credParts;

    public static void main(String[] args) {
        try {
            // Initialize the SMTP client
            initializeSMTPClient();

            // Perform the SMTP handshake
            performSMTPHandshake();

            // Send an email
            sendEmail();

            // Close connections
            closeConnections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialize the SMTP client
    private static void initializeSMTPClient() throws IOException {
        // Read credentials from a file
        String credentials = readPasswordFromFile("C:\\Users\\Sushil's pc\\OneDrive\\Skrivbord\\ID1212\\l3\\cred.txt");
        System.out.println(credentials);
        credParts = credentials.split(" ");

        // Set recipient email and email subject
        recipientEmail = "sushilkc@kth.se";
        emailSubject = "Test SMTP";

        // Create a socket and initialize input and output streams
        socket = new Socket("smtp.kth.se", 587);
        inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Print the first line from the server to initialize the connection
        System.out.println("Starting SMTP mail service...");
        System.out.println("S: " + inputReader.readLine());
    }

    // Perform the SMTP handshake
    private static void performSMTPHandshake() {
        // Send EHLO and STARTTLS commands SMTP command to identify the client and request extended features.
        sendToServer("EHLO smtp.kth.se", true);
        //process of upgrading the connection to use SSL/TLS.
        sendToServer("STARTTLS", true);

        try {
            // Upgrade to SSL/TLS connection
            //This process upgrades the existing connection (socket) to use SSL/TLS. 
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket smtpSocket = (SSLSocket) sslSocketFactory.createSocket(socket, "smtp.kth.se", 587, true);

            // Reinitialize input and output streams with the SSL/TLS socket
            inputReader = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream()));
            outputWriter = new PrintWriter(smtpSocket.getOutputStream(), true);

            // Continue with the SMTP handshake
            sendToServer("EHLO smtp.kth.se", true);
            // initiate the login/authentication process.
            sendToServer("AUTH LOGIN", true);

            // Encode and send the username and password
            Base64.Encoder encoder = Base64.getEncoder();
            sendToServer(encoder.encodeToString(credParts[0].getBytes()), false);
            sendToServer(encoder.encodeToString(credParts[1].getBytes()), false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Send an email
    private static void sendEmail() {
        // Create the email message
        String message = "Hello, this is for testing SMTP" + "\r\n."; // <CRLF>. to end the message

        // Send various SMTP commands to compose and send the email
        sendToServer("MAIL FROM:<sushilkc@kth.se>", false);
        sendToServer("RCPT TO:<" + recipientEmail + ">", false);
        sendToServer("DATA", false);
        sendToServer(getHeaders() + message, false);
    }

    // Close connections
    private static void closeConnections() {
        // Send QUIT command to terminate the session
        sendToServer("QUIT", false);

        try {
            // Close the sockets
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Compose email headers
    private static String getHeaders() {
        String headers = "Date: " + LocalTime.now() + "\r\n" +
                "From: SMTP User <sushilkc@kth.se>\r\n" +
                "Subject: " + emailSubject + "\r\n" +
                "To: " + recipientEmail + "\r\n";

        return headers;
    }

    // Send a message to the server
    private static void sendToServer(String message, boolean read) {
        try {
            // Send the message to the server
            outputWriter.println(message);
            outputWriter.flush();

            // Print the sent message
            System.out.println("C: " + message);

            // Read and handle the server's response if needed
            if (read) {
                handleServerResponse(message);
            } else {
                System.out.println("S: " + inputReader.readLine());
            }

        } catch (IOException | RuntimeException exception) {
            // Handle exceptions during message sending/receiving
            handleException(exception, "Error sending/receiving message");
        }
    }

    // Handle the server's response
    private static void handleServerResponse(String message) throws IOException {
        String line;
        while ((line = inputReader.readLine()) != null) {
            // Check if the server's response matches the expected pattern
            if (isExpectedResponse(message, line)) {
                System.out.println("S: " + line);
                break;
            }
        }
    }

    // Check if the server's response matches the expected pattern for different commands
    private static boolean isExpectedResponse(String message, String line) {
        if (message.startsWith("EHLO") || message.startsWith("HELO")) {
            return line.startsWith("250 ");
        } else if (message.startsWith("STARTTLS")) {
            return line.startsWith("220 ");
        } else if (message.startsWith("AUTH LOGIN")) {
            return line.startsWith("235 ") || line.startsWith("334 ");
        }
        return false;
    }

    // Handle exceptions during message sending/receiving
    private static void handleException(Exception exception, String errorMessage) {
        System.err.println(errorMessage + ": " + exception.getMessage());
    }

    // Read a password from a file
    private static String readPasswordFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
