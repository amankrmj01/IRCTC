package irctc.services;

import irctc.localDB.ServiceLocator;
import irctc.localDB.UserDBService;
import irctc.entities.User;

import java.util.List;

public class UserService {
    private final UserDBService userDBService;

    public UserService() {
        this.userDBService = ServiceLocator.getUserDBService();
    }

    public void addBookedTicketToUser(String userId, String ticketId) {
        List<User> users = userDBService.getAllUsers();
        for (User u : users) {
            if (u.getId().equals(userId)) {
                List<String> booked = u.getBookedTickets();
                if (booked == null) {
                    booked = new java.util.ArrayList<>();
                }
                booked.add(ticketId);
                u.setBookedTickets(booked);
                break;
            }
        }
        userDBService.save();
    }
}
