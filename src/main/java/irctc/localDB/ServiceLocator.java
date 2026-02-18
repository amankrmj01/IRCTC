package irctc.localDB;

import lombok.Getter;


public class ServiceLocator {
    @Getter
    private static final TrainDBService trainDBService = TrainDBService.getInstance();
    @Getter
    private static final TicketDBService ticketDBService = TicketDBService.getInstance();
    @Getter
    private static final UserDBService userDBService = UserDBService.getInstance();
}
