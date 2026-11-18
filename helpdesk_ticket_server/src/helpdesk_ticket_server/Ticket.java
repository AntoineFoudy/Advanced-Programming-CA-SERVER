/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpdesk_ticket_server;

/**
 *
 * @author afoud
 */
public class Ticket {
    private String ticketId;
    private String studentName;
    private String category;
    private String description;

    public Ticket(String ticketId, String studentName, String category, String description) {
        this.ticketId = ticketId;
        this.studentName = studentName;
        this.category = category;
        this.description = description;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString(){
        return ticketId + " - " + category + " - " + description;
    }
}
