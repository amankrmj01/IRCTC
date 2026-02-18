package irctc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Train {
    private String id;
    private String name;
    private String source;
    private String destination;
    private List<TicketType> ticketTypes;
    private EnumMap<TicketType, Double> ticketPrices;
    private EnumMap<TicketType, Integer> totalSeats;
    private EnumMap<TicketType, Integer> availableSeats;
    private List<String> stations;
    private Map<String, String> arrivalTimes;
    private Map<String, String> departureTimes;

    public String getTrainDetails() {
        StringBuilder details = new StringBuilder();
        details.append(String.format("Train ID: %s\nName: %s\nSource: %s\nDestination: %s\n", id, name, source, destination));
        details.append("Ticket Types and Prices:\n");
        for (TicketType type : ticketTypes) {
            details.append(String.format("- %s: %.2f (Available Seats: %d)\n", type, ticketPrices.get(type), availableSeats.get(type)));
        }
        details.append("Stations and Timings:\n");
        for (String station : stations) {
            details.append(String.format("- %s: Arrival Time: %s, Departure Time: %s\n", station, arrivalTimes.get(station), departureTimes.get(station)));
        }
        return details.toString();
    }
}
