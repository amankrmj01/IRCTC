package irctc.services;

import irctc.entities.User;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class UserBookingService {
    private final User user;

    UserBookingService(User user) {
        this.user = user;
    }

    public Boolean bookTicket() {
        return true;
    }

    public Boolean cancelTicket() {
        return true;
    }
}
