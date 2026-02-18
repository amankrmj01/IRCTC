package irctc.localDB;

import irctc.entities.User;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class UserDBService {

    private static final String usersFile = "src/main/resources/Users.json";
    private static List<User> userList;
    private static UserDBService userDBServiceInstance;
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();

    private UserDBService() {
        File usersFileObj = new File(UserDBService.usersFile);
        try {
            if (usersFileObj.exists()) {
                userList = objectMapper.readValue(usersFileObj, new TypeReference<List<User>>() {
                });
            } else {
                userList = List.of();
            }
        } catch (Exception e) {
            userList = List.of();
            IO.println("Error initializing user DB service: " + e.getMessage());
        }
    }

    public static UserDBService getInstance() {
        if (userDBServiceInstance == null) {
            userDBServiceInstance = new UserDBService();
        }
        return userDBServiceInstance;
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public void addUser(User user) {
        user.setId(UUID.randomUUID().toString());
        userList.add(user);
        saveUsersToFile();
    }

    private void saveUsersToFile() {
        try {
            objectMapper.writeValue(new File(usersFile), userList);
        } catch (Exception e) {
            IO.println("Error saving users to file: " + e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

}
