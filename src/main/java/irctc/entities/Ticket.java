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

    public String getTicketDetails() {
        return String.format("Ticket ID: %s\nTrain ID: %s\nSource: %s\nDestination: %s\nDate of Journey: %s\nDeparture Time: %s\nSeat Numbers: %s\nPassenger Names: %s\nPrice: %.2f\nStatus: %s",
                id, trainId, source, destination, dateOfJourney, departureTime, seatNumber.toString(), passengerNames.toString(), price, status);
    }
}
