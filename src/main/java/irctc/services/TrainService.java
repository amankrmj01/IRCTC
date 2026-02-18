package irctc.services;

import irctc.entities.TicketType;
import irctc.entities.Train;
import irctc.localDB.ServiceLocator;
import irctc.localDB.TrainDBService;

import java.lang.IO.*;
import java.util.List;

import static java.lang.IO.println;

public class TrainService {
    private final TrainDBService trainDBService;

    public TrainService() {
        this.trainDBService = ServiceLocator.getTrainDBService();
    }

    public void displayTrainsDetails() {
        trainDBService.getAllTrains().forEach(train -> {
            println("Train ID: " + train.getId());
            println("Train Name: " + train.getName());
            println("Train Number: " + train.getTrainNumber());
            println("Source: " + train.getSource());
            println("Destination: " + train.getDestination());
            println("Ticket Types and Prices:");
            for (var type : train.getTicketTypes()) {
                println(String.format("  - %s: $%.2f", type, train.getTicketPrices().get(type)));
            }
            println("Available Seats: \n" + getAvailableSeatsInfo(train));
            println("-----------------------------------");
        });
    }

    public Train getTrainDetailsByTrainNumber(String trainNumber) {
        return trainDBService.getTrainByNameOrNumber(trainNumber);
    }

    public String getTrainDetails(Train train) {
        StringBuilder details = new StringBuilder();
        details.append("========================================\n");
        details.append(String.format("Train No.   : %s\n", train.getTrainNumber()));
        details.append(String.format("Name        : %s\n", train.getName()));
        details.append(String.format("Source      : %s\n", train.getSource()));
        details.append(String.format("Destination : %s\n", train.getDestination()));
        details.append("----------------------------------------\n");
        details.append("Ticket Types and Prices/Km: \n");
        details.append(String.format("%-12s %-10s %-15s\n", "Type", "Price", "Available Seats"));
        for (var type : train.getTicketTypes()) {
            details.append(String.format("%-12s %-10.2f %-15d\n",
                    type,
                    train.getTicketPrices().get(type),
                    train.getAvailableSeats().get(type)));
        }
        details.append("----------------------------------------\n");
        details.append("Stations and Timings:\n");
        details.append(String.format("%-20s %-10s %-10s %-10s\n", "Station", "Arrival", "Departure", "Distance"));
        for (String station : train.getStations()) {
            String arrival = train.getArrivalTimes().getOrDefault(station, "-");
            String departure = train.getDepartureTimes().getOrDefault(station, "-");
            Double distance = (train.getStationDistances() != null && train.getStationDistances().get(station) != null)
                    ? train.getStationDistances().get(station) : 0.0;
            details.append(String.format("%-20s %-10s %-10s %-10.2f\n",
                    station, arrival, departure, distance));
        }
        details.append("========================================\n");
        return details.toString();
    }

    public String getAvailableSeatsInfo(Train train) {
        StringBuilder info = new StringBuilder();
        List<TicketType> types = train.getTicketTypes();
        if (types != null) {
            for (var type : types) {
                info.append(String.format("%-12s: %d seats available\n", type, train.getAvailableSeats().get(type)));
            }
        } else {
            info.append("No ticket types available\n");
        }
        return info.toString();
    }
}
