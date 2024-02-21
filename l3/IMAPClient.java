import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.net.ssl.*;

public class IMAPClient {
    private static BufferedReader in;

    public static void main(String[] args) {
        try {
            String credentials = readPasswordFromFile("C:\\Users\\Sushil's pc\\OneDrive\\Skrivbord\\ID1212\\l3\\cred.txt");
            //A string is created for the IMAP LOGIN command with the provided credentials.
            String userCred = "ads1 LOGIN " + credentials;
            // Skapa en SSL-anslutning för IMAP med avancerad konfiguration
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("webmail.kth.se", 993);
            // Skapa BufferedReader och PrintWriter för att läsa och skriva data
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Läs serverhälsningen efter att TLS har upprättats
            String response = in.readLine();
            System.out.println("Server: " + response);

            out.println(userCred);
            readMessage();
            // System.out.println("logedin"); 
            //tag
            out.println("ads2 SELECT INBOX");
            readMessage();
            // System.out.println("mail");
            out.println("ads3 FETCH 1805 BODY[TEXT]");
            readMessage();
            // Fortsätt med IMAP-interaktionen här...

            // Stäng anslutningen
            out.println("999 LOGOUT");
            readMessage();
            System.out.println("logout");
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // taemot responsen from server.

    public static void readMessage() {
        try {
            String message;
            while ((message = in.readLine()) != null && !message.contains("ads")) {
                System.out.println("From Server: " + message);
            }
        } catch (IOException exception) {
            System.err.println("Server error: " + exception.getMessage());
        }
    }

    private static String readPasswordFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * // Läs in lösenorden från filen
     * String keystorePassword = readPasswordFromFile("password.txt");
     * 
     * // Ladda in din nyckelbutik (keystore) och konfigurera nyckelhanteraren
     * KeyStore keyStore = KeyStore.getInstance("JKS");
     * keyStore.load(new FileInputStream("path/to/your/keystore.jks"),
     * keystorePassword.toCharArray());
     * 
     * KeyManagerFactory keyManagerFactory =
     * KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
     * keyManagerFactory.init(keyStore, keystorePassword.toCharArray());
     * 
     * // Konfigurera SSLContext med nyckelhanteraren
     * SSLContext sslContext = SSLContext.getInstance("TLS");
     * sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
     */

}
