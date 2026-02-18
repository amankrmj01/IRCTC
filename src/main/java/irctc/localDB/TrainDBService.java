package irctc.localDB;

import irctc.entities.Train;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.util.List;

public class TrainDBService {
    private static TrainDBService trainDBServiceInstance;
    private final String trainsFilePath = "src/main/resources/Trains.json";
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();
    private List<Train> trainList;

    private TrainDBService() {
        File trainsFile = new File(trainsFilePath);
        try {
            trainList = objectMapper.readValue(trainsFile, new TypeReference<List<Train>>() {
            });
        } catch (Exception e) {
            trainList = List.of();
            IO.println("Error initializing train DB service: " + e.getMessage());
        }

    }

    public static TrainDBService getInstance() {
        if (trainDBServiceInstance == null) {
            trainDBServiceInstance = new TrainDBService();
        }
        return trainDBServiceInstance;
    }

    public List<Train> getAllTrains() {
        return trainList;
    }

    public Train getTrainById(String Id) {
        for (Train train : trainList) {
            if (train.getId().equals(Id)) {
                return train;
            }
        }
        return null;
    }

    public Train getTrainByNameOrNumber(String trainNameOrNumber) {
        for (Train train : trainList) {
            if (train.getName().equalsIgnoreCase(trainNameOrNumber) || train.getTrainNumber().equals(trainNameOrNumber)) {
                return train;
            }
        }
        return null;
    }
}
