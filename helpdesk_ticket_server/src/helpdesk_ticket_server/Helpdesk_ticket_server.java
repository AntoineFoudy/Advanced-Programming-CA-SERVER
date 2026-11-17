/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package helpdesk_ticket_server;

import java.io.IOException;
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
            while(true){
                try{
                    Socket link = serverSocket.accept();
                    // Start a new Runnable class
                    ClientConnectionRun clientConnectionRun = new ClientConnectionRun(link);
                    // Puts class into its own thread
                    Thread thread = new Thread(clientConnectionRun);
                    // Starts class excecution on thread
                    thread.start();
                }
                catch(IOException e) {
                    System.out.println("Error:" + e);
                }
            }            
        }
        catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }
    
}
