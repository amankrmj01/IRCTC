package irctc.services;

import irctc.entities.User;
import irctc.localDB.UserDBService;

public class UserAuthService {

    private final UserDBService userDBService;

    public UserAuthService(UserDBService userDBService) {
        this.userDBService = userDBService;
    }

    public Boolean registerUser() {
        // Implement user registration logic here
        return true; // Placeholder return value
    }

    public Boolean loginUser(String email, String password) {
        User user = userDBService.getUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
