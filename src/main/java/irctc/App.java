package irctc;

import irctc.localDB.TicketDBService;
import irctc.localDB.TrainDBService;
import irctc.localDB.UserDBService;
import irctc.services.UserAuthService;

import java.util.Scanner;

import static java.lang.IO.*;

public class App {
    static void main(String[] args) {
        IRCTC irctc = IRCTC.getInstance();
        while (true) {
            println("Welcome to IRCTC!");
            println("1. Login");
            println("2. Register");
            println("3. Exit");
            println("Please select an option:");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> irctc.login();
                case "2" -> irctc.register();
                case "3" -> {
                    println("Thank you for using IRCTC. Goodbye!");
                    return;
                }
                default -> println("Invalid option. Please try again.");
            }
        }
    }
}

class IRCTC {

    private static final Scanner scanner = new Scanner(System.in);
    private static IRCTC instance;
    private static TrainDBService trainDBService;
    private static TicketDBService ticketDBService;
    private static UserDBService userDBService;

    private IRCTC() {
        trainDBService = TrainDBService.getInstance();
        ticketDBService = TicketDBService.getInstance();
        userDBService = UserDBService.getInstance();
    }

    public static IRCTC getInstance() {
        if (instance == null) {
            instance = new IRCTC();
        }
        return instance;
    }

    public void login() {
        println("Enter email:");
        String email = scanner.nextLine();
        println("Enter password:");
        String password = scanner.nextLine();
        UserAuthService userAuthService = new UserAuthService(userDBService);
        if (userAuthService.loginUser(email, password)) {
            println("Login successful!");
        } else {
            println("Invalid email or password.");
        }

    }

    public void register() {
        println("Enter name:");
        String name = scanner.nextLine();
        println("Enter email:");
        String email = scanner.nextLine();
        println("Enter password:");
        String password = scanner.nextLine();
        UserAuthService userAuthService = new UserAuthService(userDBService);
        if (userAuthService.registerUser(name, email, password)) {
            println("Registration successful!");
        } else {
            println("Email already exists. Please try again.");
        }
    }
}
