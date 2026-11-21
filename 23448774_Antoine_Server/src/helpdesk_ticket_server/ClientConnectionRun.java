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
            
            // Shows when client connects
            System.out.println("New Client: " + socket.getRemoteSocketAddress()); 

            String clientMessage;

            // Check if clientMessage is null where the client has disconnected
            while ((clientMessage = in.readLine()) != null) {
                // Trim clientMessage to prevent valid commands being rejected 
                clientMessage = clientMessage.trim();
                
                // Shows when a message is recived + from which client
                System.out.println("Client: " + socket.getRemoteSocketAddress() + "; Message: " + clientMessage);

                // Client sends stop command or client disconnects
                if (clientMessage.equalsIgnoreCase("STOP")) {
                    out.println("TERMINATE");
                    break;
                }
                
                
                try {
                    // Parse clientMessage into a format that allows the system to handle the request
                    String[] clientCommands = clientMessage.split(";", -1);
                                        
                    // Action is case-insensitive
                    clientCommands[0] = clientCommands[0].toLowerCase().trim();
                    
                    // Check if action provided is valid
                    if(!acceptedCommands.contains(clientCommands[0])) {
                        throw new InvalidCommandException("action must be open/close/list");
                    }
                    
                    // Eeach command requires 5 fields, if only 2 fields are provided the rest of the fields must be "-;"
                     if(!(clientCommands.length == 5)) {
                        throw new InvalidCommandException("There must be 5 fields in the command");
                    }
                     
                    for(int i = 1; i < clientCommands.length; i++) {
                        clientCommands[i] = clientCommands[i].trim();
                        
                        // Make sure there are no empty fields
                        if(clientCommands[i].isEmpty()) {
                            throw new InvalidCommandException("All 5 fields must not be left blank");
                        }
                    }
                     
                    
                    
                    // Check which command is being called and excecute correct method
                    if(clientCommands[0].equals(acceptedCommands.get(0))) {
                        // open command
                        // Check that fields for open action do not contain just "-"
                        for(int i = 1; i < clientCommands.length; i++) {
                            if(clientCommands[i].equals("-")) {
                                throw new InvalidCommandException("Open action requires: ticket ID, student name, category, and description");
                            }
                        }
                        out.println(ticketManagement.open(clientCommands[1], clientCommands[2], clientCommands[3], clientCommands[4]));
                    } 
                    else if(clientCommands[0].equals(acceptedCommands.get(1))) {
                        // close command
                        // Check that ticket ID is provieded and not "-"
                        if(clientCommands[1].equals("-")) {
                            throw new InvalidCommandException("Close action requires a valid ticket id");
                        }
                        out.println(ticketManagement.close(clientCommands[1]));
                    }
                    else if(clientCommands[0].equals(acceptedCommands.get(2))) {
                        // list command
                        // Check that student name is provided and not "-"
                        if(clientCommands[2].equals("-")) {
                            throw new InvalidCommandException("List action requires a valid student name");
                        }
                        out.println(ticketManagement.list(clientCommands[2]));
                    }                     
                } catch (InvalidCommandException e) {
                    out.println("InvalidCommandException: " + e.toString());
                } catch (Exception e) {
                    out.println("Something unexpected occurred: " + e.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("I/O Error: " + e.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

}
