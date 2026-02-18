package irctc;

import irctc.entities.TicketType;
import irctc.entities.Train;
import irctc.entities.User;
import irctc.localDB.ServiceLocator;
import irctc.services.TicketService;
import irctc.services.TrainService;
import irctc.services.UserAuthService;

import java.util.List;

import static java.lang.IO.*;

public class IRCTC {

    private static IRCTC instance;
    private static User user;
    private final ServiceLocator serviceLocator;

    private IRCTC(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public static IRCTC getInstance(ServiceLocator serviceLocator) {
        if (instance == null) {
            instance = new IRCTC(serviceLocator);
        }
        return instance;
    }

    public static IRCTC getInstance() {
        if (instance == null) {
            throw new IllegalStateException("IRCTC is not initialized. Call getInstance(ServiceLocator) first.");
        }
        return instance;
    }

    public void start() {
        while (true) {
            println("Welcome to IRCTC!");
            println("1. Login");
            println("2. Register");
            println("3. Exit");
            println("Please select an option:");
            String choice = readln();
            switch (choice) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> {
                    println("Thank you for using IRCTC. Goodbye!");
                    return;
                }
                default -> println("Invalid option. Please try again.");
            }
        }
    }

    private void login() {
        println("Enter email:");
        String email = readln();
        println("Enter password:");
        String password = readln();
        UserAuthService userAuthService = new UserAuthService();
        user = userAuthService.loginUser(email, password);
        if (user == null) {
            println("Invalid email or password.");
            return;
        } else {
            println("Login successful!");
        }

        while (true) {
            println("1. View Trains");
            println("2: Get Train Details by Train Number");
            println("3. Book Ticket");
            println("4. Logout");
            println("Please select an option:");
            String choice = readln();
            switch (choice) {
                case "1" -> viewTrains();
                case "2" -> displayTrainDetailsByNumber();
                case "3" -> bookTicket();
                case "4" -> {
                    println("Logged out successfully.");
                    return;
                }
                default -> println("Invalid option. Please try again.");
            }
        }

    }

    private void displayTrainDetailsByNumber() {
        println("Enter Train Name or Number:");
        String train = readln();
        TrainService trainService = new TrainService();
        Train selectedTrain = trainService.getTrainDetailsByTrainNumber(train);
        if (selectedTrain == null) {
            println("Train not found. Please try again.");
        } else {
            println(trainService.getTrainDetails(selectedTrain));
        }
    }

    private void viewTrains() {
        println("Available Trains:");
        TrainService trainService = new TrainService();
        trainService.displayTrainsDetails();
    }

    private void bookTicket() {
        println("Enter Train Name or Number:");
        String train = readln();
        TrainService trainService = new TrainService();
        Train selectedTrain = trainService.getTrainDetailsByTrainNumber(train);
        if (selectedTrain == null) {
            println("Train not found. Please try again.");
            return;
        } else {
            println(trainService.getTrainDetails(selectedTrain));
        }
        println("Enter Source:");
        String source = readln();
        if (selectedTrain.getStations().stream().noneMatch(
                station -> station.equalsIgnoreCase(source)
        )) {
            println("Source station not found in the train route. Please try again.");
            return;
        }
        println("Enter Destination:");
        String destination = readln();
        if (selectedTrain.getStations().stream().noneMatch(
                station -> station.equalsIgnoreCase(destination)
        ))
            println("Enter number of Passengers:");
        int numTickets;
        try {
            numTickets = Integer.parseInt(readln());
        } catch (NumberFormatException e) {
            println("Invalid number. Please try again.");
            return;
        }
        String[] passengerNames = new String[numTickets];
        for (int i = 0; i < numTickets; i++) {
            println("Enter name of Passenger " + (i + 1) + ":");
            passengerNames[i] = readln();
        }
        println("Enter Ticket Type :\n available ticket types for selected train are:");
        List<TicketType> availableTypes = selectedTrain.getTicketTypes();
        for (int i = 0; i < availableTypes.size(); i++) {
            println((i + 1) + " : " + availableTypes.get(i).toString());
        }
        int typeChoice;
        try {
            typeChoice = Integer.parseInt(readln());
        } catch (NumberFormatException e) {
            println("Invalid ticket type selected. Please try again.");
            return;
        }
        if (typeChoice >= 1 && typeChoice <= availableTypes.size()) {
            bookTicketForType(selectedTrain, availableTypes.get(typeChoice - 1), passengerNames);
        } else {
            println("Invalid ticket type selected. Please try again.");
        }

        println("Ticket booking functionality is under development.");
    }

    private void bookTicketForType(Train selectedTrain, TicketType ticketType, String[] passengerNames) {

    }

    private void register() {
        println("Enter name:");
        String name = readln();
        println("Enter email:");
        String email = readln();
        println("Enter password:");
        String password = readln();
        UserAuthService userAuthService = new UserAuthService();
        user = userAuthService.registerUser(name, email, password);
        if (user == null) {
            println("Email already exists. Please try again.");
        } else {
            println("Registration successful!");
        }
    }
}
