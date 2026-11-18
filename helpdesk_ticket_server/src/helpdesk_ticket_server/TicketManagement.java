/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpdesk_ticket_server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author afoud
 */
public class TicketManagement {
    private final LinkedHashMap<String, Ticket> tickets = new LinkedHashMap<>();
    private final List<String> acceptedCategories = List.of("account", "network", "software", "other");
    
    
    // Open new ticket and save in LinkedHashMap
    public synchronized String open(String ticketId, String studentName, String category, String description) throws InvalidCommandException {
        // Check if tickets already contains a ticket with the same ID
        if(tickets.containsKey(ticketId)) {
            throw new InvalidCommandException("ticket ID " + ticketId + " is already open");
        }
        
        // Check if ticket category is correct
        if(!acceptedCategories.contains(category.toLowerCase())) {
            throw new InvalidCommandException("category must be " + acceptedCategories.toString());
        }
        
        // Create and add new ticket
        Ticket newTicket = new Ticket(ticketId, studentName, category.toLowerCase(), description);
        tickets.put(ticketId, newTicket);
        
        // Create return message
        return serverResponse(studentName);
    }
    
    // Close ticket and remove from LinkedHashMap
    public synchronized String close(String ticketId) throws InvalidCommandException{
        // Check if ticket exists
        if(!tickets.containsKey(ticketId)) {
            throw new InvalidCommandException("ticket ID " + ticketId + " does not exist");
        }
        
        // Get the name of student for formatted response
        String studentName = tickets.get(ticketId).getStudentName();
        
        // Remove ticket from LinkedHashMap
        tickets.remove(ticketId);
        
        // Send response
        return "Closed " + ticketId + ". " + serverResponse(studentName);
    }
    
    // Send list of tickets that student has open
    public synchronized String list(String student) {
        return serverResponse(student);
    }
    
    
    
    
    // Method to create the formatted response for at the end of every command
    private String serverResponse(String student) {
        // Create a collection of active tickets for spesified student
        ArrayList<String> activeTickets = new ArrayList<>();
        
        // Ticket count
        int ticketAmount = 0;
        
        // Loop through each ticket
        for(Ticket t : tickets.values()) {
            if(t.getStudentName().equalsIgnoreCase(student)) {
                ticketAmount += 1;
                activeTickets.add(t.toString());
            } 
        }
        
        // If student has no open ticekts
        if(ticketAmount == 0) {
            return student + " has no open tickets";
        }
        
        // Add Student name + formatted message (has x open ticket(s)
        String response = student + " has " + ticketAmount + " open ticket";
        if(ticketAmount > 1) {
            response += "s";
        }
        response += ": ";
        

        response += String.join("; ", activeTickets);
        
        return response;
    }
    
}
