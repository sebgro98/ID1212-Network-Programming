import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
        

public class Server {
    public static void main(String[] args){
        ServerSocket ss;
        Socket socket;
        BufferedWriter bfw = null;
        BufferedReader bfr = null;
        InputStreamReader isr = null;
        
        while(true) {
            try{
                ss = new ServerSocket(8080);
                socket = ss.accept();
                System.out.println("Host Address: " + socket.getLocalAddress());
                System.out.println("Client Address: " + socket.getInetAddress());
                System.out.println("Host port: " + socket.getLocalPort());
                System.out.println("Client port: " + socket.getPort());
                //System.out.println(s.getLocalSocketAddress());
                //System.out.println(s.getRemoteSocketAddress());
                isr = new InputStreamReader(socket.getInputStream());
                bfr = new BufferedReader(isr);
                while(true){
                    String message = bfr.readLine();
                    if (message == null) {
                        break;  // Exit the loop when the client disconnects.
                    }
                    System.out.println("Client: " + message);
                    if (message.equals("Exit")) {
                        System.out.println("Client Disconected: " + socket.getInetAddress()); 
                        break;
                    }
                }
            
                socket.close(); 
                isr.close();
                ss.close();
            }
        
            catch(java.net.UnknownHostException e){
                System.out.print(e.getMessage());
            }
            catch(java.io.IOException e){
                System.out.print(e.getMessage());
            }
        }
        
    }
}
