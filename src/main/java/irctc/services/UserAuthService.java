package irctc.services;

import irctc.entities.User;
import irctc.localDB.ServiceLocator;
import irctc.localDB.UserDBService;

import java.util.List;

public class UserAuthService {

    private final UserDBService userDBService;

    public UserAuthService() {
        this.userDBService = ServiceLocator.getUserDBService();
    }

    public User loginUser(String email, String password) {
        User user = userDBService.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User registerUser(String name, String email, String password) {
        if (userDBService.getUserByEmail(email) != null) {
            return null;
        }
        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .bookedTickets(List.of())
                .build();
        userDBService.addUser(newUser);
        return newUser;
    }
}
