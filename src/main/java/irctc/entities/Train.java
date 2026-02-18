package irctc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Train {
    private String id;
    private String trainNumber;
    private String name;
    private String source;
    private String destination;
    @Builder.Default
    private List<TicketType> ticketTypes = new ArrayList<>();
    @Builder.Default
    private Map<TicketType, Double> ticketPrices = new HashMap<>();
    @Builder.Default
    private Map<TicketType, Integer> totalSeats = new HashMap<>();
    @Builder.Default
    private Map<TicketType, Integer> availableSeats = new HashMap<>();
    @Builder.Default
    private List<String> stations = new ArrayList<>();
    @Builder.Default
    private Map<String, String> arrivalTimes = new LinkedHashMap<>();
    @Builder.Default
    private Map<String, String> departureTimes = new LinkedHashMap<>();
    @Builder.Default
    private Map<String, Double> stationDistances = new LinkedHashMap<>();
}
