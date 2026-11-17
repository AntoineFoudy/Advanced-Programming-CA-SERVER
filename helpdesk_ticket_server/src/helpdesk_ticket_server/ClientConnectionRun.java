/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpdesk_ticket_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author afoud
 */
public class ClientConnectionRun implements Runnable {

    Socket clientSocket = null;

    public ClientConnectionRun(Socket connection) {
        this.clientSocket = connection;
    }

    @Override
    public void run() {
        try (Socket socket = clientSocket;
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {

            String clientMessage;

            // Check if clientMessage is null where the client has disconnected
            while ((clientMessage = in.readLine()) != null) {
                // Trim clientMessage to prevent issues with commands being misinterpreted 
                clientMessage = clientMessage.trim();

                // Client sends stop command or client disconnects
                if (clientMessage.equals("STOP")) {
                    out.println("Terminated");
                    System.out.println("Terminated");
                    break;
                }
                
                // Test if clients mssage is being recived and if each link is ran on its own thread
                out.println(clientMessage);

            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

}
