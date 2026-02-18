package irctc;

import irctc.localDB.ServiceLocator;

public class App {
    static void main(String[] args) {
        ServiceLocator serviceLocator = new ServiceLocator();
        IRCTC irctc = IRCTC.getInstance(serviceLocator);
        irctc.start();
    }
}
