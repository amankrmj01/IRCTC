package irctc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private String id;
    private String source;
    private String destination;
    private String dateOfJourney;
    private String departureTime;
    private String trainId;
    private String userId;
    private List<String> seatNumber;
    private List<String> passengerNames;
    private double price;
    private TicketType type;
    private TicketStatus status;
}
