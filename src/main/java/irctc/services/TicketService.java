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

    public String getAllTicketsFormatted() {
        List<Ticket> tickets = ticketDBService.getAllTickets();
        if (tickets == null || tickets.isEmpty()) {
            return "No tickets found.\n";
        }

        StringBuilder result = new StringBuilder();
        result.append("All Tickets:\n");
        result.append("========================================\n");
        for (Ticket ticket : tickets) {
            result.append(formatTicket(ticket));
            result.append("----------------------------------------\n");
        }
        return result.toString();
    }

    public String getTicketByIdFormatted(String ticketId) {
        String id = ticketDBService.getTicketById(ticketId);
        if (id == null) {
            return String.format("Ticket with ID '%s' not found.\n", ticketId);
        }
        return String.format("Ticket ID: %s\n", id);
    }

    public String bookTicket(String source, String destination, String dateOfJourney, String departureTime,
                             Train train, String userId, List<String> seatNumber, List<String> passengerNames,
                             double price, TicketType ticketType) {
        if (source == null || destination == null || dateOfJourney == null || departureTime == null ||
                train == null || userId == null || seatNumber == null || passengerNames == null || ticketType == null) {
            return "Error: All ticket details must be provided.\n";
        }

        Integer available = train.getAvailableSeats().get(ticketType);
        if (available == null || available < seatNumber.size()) {
            return String.format("Error: Not enough seats available. \nRequested: %d, Available: %d\n",
                    seatNumber.size(), available != null ? available : 0);
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

        // Update user's bookedTickets using UserService
        irctc.services.UserService userService = new irctc.services.UserService();
        userService.addBookedTicketToUser(userId, ticket.getId());

        return formatTicketConfirmation(ticket);
    }

    public String cancelTicket(String ticketId) {
        String existingTicketId = ticketDBService.getTicketById(ticketId);
        if (existingTicketId == null) {
            return String.format("Error: Ticket with ID '%s' does not exist.\n", ticketId);
        }
        // Implement cancellation logic here (e.g., update ticket status, refund process, etc.)
        return String.format("Ticket '%s' has been cancelled successfully.\n", ticketId);
    }

    /**
     * Handles the full ticket booking process: validation, seat assignment, price calculation, and booking.
     * Returns a result message (success or error).
     */
    public String bookTicketFull(String source, String destination, String date, String time, Train train, String userId, List<String> passengerNames, TicketType ticketType) {
        // Validate ticket type
        if (!train.getTicketTypes().contains(ticketType)) {
            return "Invalid ticket type selected.";
        }
        // Validate seats
        Integer available = train.getAvailableSeats().get(ticketType);
        if (available == null || available < passengerNames.size()) {
            return String.format("Error: Not enough seats available. Requested: %d, Available: %d", passengerNames.size(), available != null ? available : 0);
        }
        // Generate seat numbers
        List<String> seatNumbers = new java.util.ArrayList<>();
        for (int i = 0; i < passengerNames.size(); i++) {
            seatNumbers.add("A" + (i + 1));
        }
        // Calculate price
        Double ticketPrice = train.getTicketPrices().get(ticketType);
        // Normalize destination for lookup
        String matchedDestination = null;
        for (String key : train.getStationDistances().keySet()) {
            if (key.equalsIgnoreCase(destination.trim())) {
                matchedDestination = key;
                break;
            }
        }
        if (matchedDestination == null) {
            return "Error: Destination not found in train's station distances. Available destinations: " + train.getStationDistances().keySet();
        }
        Double distance = train.getStationDistances().get(matchedDestination);
        if (ticketPrice == null || distance == null) {
            return "Error: Price or distance not available for selected ticket type or destination.";
        }
        double totalPrice = ticketPrice * passengerNames.size() * 10 * distance;
        // Book ticket (calls existing bookTicket method)
        return bookTicket(
                source,
                matchedDestination,
                date,
                time,
                train,
                userId,
                seatNumbers,
                passengerNames,
                totalPrice,
                ticketType
        );
    }

    private String formatTicket(Ticket ticket) {
        String sb = String.format("Ticket ID       : %s\n", ticket.getId()) +
                String.format("User ID         : %s\n", ticket.getUserId()) +
                String.format("Train ID        : %s\n", ticket.getTrainId()) +
                String.format("Source          : %s\n", ticket.getSource()) +
                String.format("Destination     : %s\n", ticket.getDestination()) +
                String.format("Date of Journey : %s\n", ticket.getDateOfJourney()) +
                String.format("Departure Time  : %s\n", ticket.getDepartureTime()) +
                String.format("Ticket Type     : %s\n", ticket.getType()) +
                String.format("Price           : $%.2f\n", ticket.getPrice()) +
                String.format("Status          : %s\n", ticket.getStatus()) +
                String.format("Passengers      : %s\n", String.join(", ", ticket.getPassengerNames())) +
                String.format("Seat Numbers    : %s\n", String.join(", ", ticket.getSeatNumber()));
        return sb;
    }

    private String formatTicketConfirmation(Ticket ticket) {
        String sb = "========================================\n" +
                "       TICKET BOOKING CONFIRMATION      \n" +
                "========================================\n" +
                formatTicket(ticket) +
                "========================================\n" +
                "Thank you for booking with IRCTC!\n";
        return sb;
    }
}
