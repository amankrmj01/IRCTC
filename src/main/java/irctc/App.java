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
        irctc.login();
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
}
