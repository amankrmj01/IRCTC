package irctc.services;

import irctc.entities.Ticket;
import irctc.entities.Train;
import irctc.entities.TicketType;
import irctc.localDB.ServiceLocator;
import irctc.localDB.TicketDBService;

import java.util.List;
import java.util.UUID;

public class TicketService {

    private final TicketDBService ticketDBService;

    public TicketService() {
        this.ticketDBService = ServiceLocator.getTicketDBService();
    }

    public List<Ticket> getAllTickets() {
        return ticketDBService.getAllTickets();
    }

    public String getTicketById(String ticketId) {
        return ticketDBService.getTicketById(ticketId);
    }

    public void bookTicket(String source, String destination, String dateOfJourney, String departureTime, Train train, String userId, List<String> seatNumber, List<String> passengerNames, double price, TicketType ticketType) {
        if (source == null || destination == null || dateOfJourney == null || departureTime == null || train == null || userId == null || seatNumber == null || passengerNames == null || ticketType == null) {
            throw new IllegalArgumentException("All ticket details must be provided");
        }
        Integer available = train.getAvailableSeats().get(ticketType);
        if (available == null || available < seatNumber.size()) {
            throw new IllegalArgumentException("Not enough seats available on the train for the selected ticket type");
        }

        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID().toString())
                .source(source)
                .destination(destination)
                .dateOfJourney(dateOfJourney)
                .departureTime(departureTime)
                .trainId(train.getId())
                .userId(userId)
                .seatNumber(seatNumber)
                .passengerNames(passengerNames)
                .price(price)
                .type(ticketType)
                .status(irctc.entities.TicketStatus.BOOKED)
                .build();
        ticketDBService.addTicket(ticket);
    }

    public void cancelTicket(String ticketId) {
        String existingTicketId = ticketDBService.getTicketById(ticketId);
        if (existingTicketId == null) {
            throw new IllegalArgumentException("Ticket with ID " + ticketId + " does not exist");
        }
        // Implement cancellation logic here (e.g., update ticket status, refund process, etc.)
    }

}
