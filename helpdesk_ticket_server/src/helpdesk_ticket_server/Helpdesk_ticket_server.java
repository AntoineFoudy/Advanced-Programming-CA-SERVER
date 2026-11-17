/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package helpdesk_ticket_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author afoud
 */
public class Helpdesk_ticket_server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Server's port number
        int port = 1818;
        // Instantiate new ServerSocket in try with resources (implements java.io.Closeable)
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            // Console output to check if server started on specified port
            System.out.println("Listening on port: " + port);
            
            // Blocks excecution until client connects
            // Set up ability to read what client enters
            // Set up ability to write to the client
            try(Socket socket = serverSocket.accept();
                    BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(),true)) {
                
                String clientMessage;
                
                // Check if clientMessage is null where the client has disconnected
                while((clientMessage = in.readLine()) != null) {
                    // Trim clientMessage to prevent issues with commands being misinterpreted 
                    clientMessage = clientMessage.trim();
                    
                    // Client sends stop command or client disconnects
                    if(clientMessage.equals("STOP")) {
                        out.println("Terminated");
                        System.out.println("Terminated");
                        break;
                    }
                    
                }
            }
            
        }
        catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
}
