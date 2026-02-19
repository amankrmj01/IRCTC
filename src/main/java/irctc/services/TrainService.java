package irctc.services;

import irctc.entities.TicketType;
import irctc.entities.Train;
import irctc.localDB.ServiceLocator;
import irctc.localDB.TrainDBService;

import java.util.List;

public class TrainService {
    private final TrainDBService trainDBService;

    public TrainService() {
        this.trainDBService = ServiceLocator.getTrainDBService();
    }

    public String displayTrainsDetails() {
        StringBuilder result = new StringBuilder();
        result.append("Available Trains:\n");
        result.append("========================================\n");

        trainDBService.getAllTrains().forEach(train -> {
            result.append(String.format("Train ID        : %s\n", train.getId()));
            result.append(String.format("Train Name      : %s\n", train.getName()));
            result.append(String.format("Train Number    : %s\n", train.getTrainNumber()));
            result.append(String.format("Source          : %s\n", train.getSource()));
            result.append(String.format("Destination     : %s\n", train.getDestination()));
//            result.append(String.format("Price per KM    : $%.2f\n", train.getPricePerKm()));
            result.append("\nTicket Types and Prices:\n");
            for (var type : train.getTicketTypes()) {
                Double price = train.getTicketPrices().get(type);
                if (price != null) {
                    result.append(String.format("  - %-12s: $%-8.2f\n", type, price));
                }
            }
            result.append("\n").append(getAvailableSeatsInfo(train));
            result.append("----------------------------------------\n");
        });

        return result.toString();
    }

    public Train getTrainDetailsByTrainNumber(String trainNumber) {
        return trainDBService.getTrainByNameOrNumber(trainNumber);
    }

    public String getTrainDetails(Train train) {
        StringBuilder details = new StringBuilder();
        details.append("========================================\n");
        details.append(String.format("Train No.       : %s\n", train.getTrainNumber()));
        details.append(String.format("Name            : %s\n", train.getName()));
        details.append(String.format("Source          : %s\n", train.getSource()));
        details.append(String.format("Destination     : %s\n", train.getDestination()));
//        details.append(String.format("Price per KM    : $%.2f\n", train.getPricePerKm()));
        details.append("----------------------------------------\n");
        details.append("Ticket Types and Prices:\n");
        details.append(String.format("%-15s %-12s %-15s\n", "Type", "Price", "Available Seats"));
        for (var type : train.getTicketTypes()) {
            Double price = train.getTicketPrices().get(type);
            Integer seats = train.getAvailableSeats().get(type);
            if (price != null && seats != null) {
                details.append(String.format("%-15s $%-11.2f %-15d\n", type, price, seats));
            }
        }
        details.append("----------------------------------------\n");
        details.append("Stations and Timings:\n");
        details.append(String.format("%-20s %-12s %-12s %-12s\n", "Station", "Arrival", "Departure", "Distance (km)"));
        for (String station : train.getStations()) {
            String arrival = train.getArrivalTimes().getOrDefault(station, "-");
            String departure = train.getDepartureTimes().getOrDefault(station, "-");
            Double distance = (train.getStationDistances() != null && train.getStationDistances().get(station) != null)
                    ? train.getStationDistances().get(station) : 0.0;
            details.append(String.format("%-20s %-12s %-12s %-12.2f\n",
                    station, arrival, departure, distance));
        }
        details.append("========================================\n");
        return details.toString();
    }

    public String getAvailableSeatsInfo(Train train) {
        StringBuilder info = new StringBuilder();
        info.append("Available Seats:\n");
        List<TicketType> types = train.getTicketTypes();
        if (types != null && !types.isEmpty()) {
            for (var type : types) {
                Integer seats = train.getAvailableSeats().get(type);
                if (seats != null) {
                    info.append(String.format("  %-12s: %d seats available\n", type, seats));
                }
            }
        } else {
            info.append("  No ticket types available\n");
        }
        return info.toString();
    }

    /**
     * Validates that both source and destination are in the train's route and source comes before destination.
     * Returns null if valid, otherwise an error message.
     */
    public String validateSourceAndDestination(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        int sourceIdx = -1, destIdx = -1;
        for (int i = 0; i < stations.size(); i++) {
            if (stations.get(i).equalsIgnoreCase(source)) sourceIdx = i;
            if (stations.get(i).equalsIgnoreCase(destination)) destIdx = i;
        }
        if (sourceIdx == -1) {
            return "Source station not found in the train route. Please try again.";
        }
        if (destIdx == -1) {
            return "Destination station not found in the train route. Please try again.";
        }
        if (sourceIdx >= destIdx) {
            return "Invalid source/destination order. Source must come before destination.";
        }
        return null;
    }
}
