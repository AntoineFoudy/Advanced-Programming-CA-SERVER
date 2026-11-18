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
import java.util.List;

/**
 *
 * @author afoud
 */
public class ClientConnectionRun implements Runnable {

    private Socket clientSocket = null;
    private final TicketManagement ticketManagement;
    private final List<String> acceptedCommands = List.of("open", "close", "list");

    public ClientConnectionRun(Socket connection, TicketManagement ticketManagement) {
        this.clientSocket = connection;
        this.ticketManagement = ticketManagement;
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
                // out.println(clientMessage);

                
                try {
                    // Parse clientMessage into a format that allows the system to handle the request
                    String[] clientCommands = clientMessage.split(";", -1);
                    
                    // Eeach command requires 5 fields, if only 2 fields are provided the rest of the fields must be "-;"
                     if(!(clientCommands.length == 5)) {
                        throw new InvalidCommandException("There must be 5 fields in the command");
                    }
                     
                    for(int i = 0; i < clientCommands.length; i++) {
                        clientCommands[i] = clientCommands[i].trim();
                        
                        // Make sure there are no empty fields
                        if(clientCommands[i].isEmpty()) {
                            throw new InvalidCommandException("All 5 fields must not be left blank");
                        }
                    }
                     
                    // Action is case-insensitive
                    clientCommands[0] = clientCommands[0].toLowerCase();
                    
                    // Check which command is being called and throw InvalidCommandException if command is not valid
                    if(clientCommands[0].equals(acceptedCommands.get(0))) {
                        // open command
                        out.println(ticketManagement.open(clientCommands[1], clientCommands[2], clientCommands[3], clientCommands[4]));
                    } 
                    else if(clientCommands[0].equals(acceptedCommands.get(1))) {
                        // close command
                        out.println(ticketManagement.close(clientCommands[1]));
                    }
                    else if(clientCommands[0].equals(acceptedCommands.get(2))) {
                        // list command
                        out.println(ticketManagement.list(clientCommands[2]));
                    }
                    else {
                        throw new InvalidCommandException("Command must be: " + acceptedCommands.toString());
                    }                     
                } catch (InvalidCommandException e) {
                    out.println("InvalidCommandException: " + e.getMessage());
                } catch (Exception e) {
                    out.println("Something unexpected occured: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

}
