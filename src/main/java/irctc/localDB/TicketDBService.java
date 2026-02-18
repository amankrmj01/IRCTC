package irctc.localDB;

import irctc.entities.Ticket;
import irctc.entities.TicketStatus;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class TicketDBService {
    private static TicketDBService ticketDBServiceInstance;
    private final String ticketFilePath = "src/main/java/irctc/localDB/Tickets.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Ticket> ticketList;

    private TicketDBService() {
        File ticketFile = new File(ticketFilePath);
        try {
            if (ticketFile.exists()) {
                ticketList = objectMapper.readValue(ticketFile, new TypeReference<List<Ticket>>() {
                });
            } else {
                ticketList = List.of();
            }
        } catch (Exception e) {
            ticketList = List.of();
            IO.println("Error initializing ticket DB service: " + e.getMessage());
        }
    }

    public static TicketDBService getInstance() {
        if (ticketDBServiceInstance == null) {
            ticketDBServiceInstance = new TicketDBService();
        }
        return ticketDBServiceInstance;
    }

    public List<Ticket> getAllTickets() {
        return ticketList;
    }

    public String getTicketById(String ticketId) {
        for (Ticket ticket : ticketList) {
            if (ticket.getId().equals(ticketId)) {
                return ticket.getId();
            }
        }
        return null;
    }

    public void addTicket(Ticket ticket) {
        ticketList.add(ticket);
        saveTicketsToFile();
    }

    private void saveTicketsToFile() {
        objectMapper.writeValue(new File(ticketFilePath), ticketList);
    }

}
