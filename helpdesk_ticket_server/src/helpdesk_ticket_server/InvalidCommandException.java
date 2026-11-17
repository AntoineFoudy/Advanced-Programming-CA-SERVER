/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpdesk_ticket_server;

/**
 *
 * @author afoud
 */
public class InvalidCommandException extends Exception{
    
    public InvalidCommandException(String message) {
        super(message);
    }
}
