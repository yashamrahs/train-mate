package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private static final String TRAIN_PATH = "app/src/main/java/ticket/booking/localDB/trains.json";

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initializes TrainService by loading train data from local JSON database.
     *
     * @throws IOException if there's an issue reading from the file
     */
    public TrainService() throws IOException {
        File trains = new File(TRAIN_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    /**
     * Adds a train to the system. If a train with the same ID already exists,
     * it updates the train details instead.
     *
     * @param train the Train object to add or update
     */
    public void addTrain(Train train) {
        Optional<Train> existingTrain = trainList.stream()
                .filter(t -> t.getTrainId().equalsIgnoreCase(train.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            updateTrain(train);
        } else {
            trainList.add(train);
            saveTrainList();
        }
    }

    /**
     * Updates an existing train in the train list. If the train does not exist,
     * it adds it as a new train.
     *
     * @param train the Train object with updated details
     */
    public void updateTrain(Train train) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(train.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), train);
            saveTrainList();
        } else {
            // Train not found by ID, add as new
            addTrain(train);
        }
    }

    /**
     * Searches and returns all trains that go from the given source to destination
     * in the correct travel order.
     *
     * @param source      the source station
     * @param destination the destination station
     * @return list of matching Train objects
     */
    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    /**
     * Saves the current train list to the JSON file.
     */
    private void saveTrainList() {
        try {
            objectMapper.writeValue(new File(TRAIN_PATH), trainList);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save train list", e);
        }
    }

    /**
     * Checks if a train travels from source to destination in order.
     *
     * @param train       the Train object
     * @param source      the source station name
     * @param destination the destination station name
     * @return true if the route is valid, false otherwise
     */
    private boolean validTrain(Train train, String source, String destination) {
        List<String> allStations = train.getStations();

        int sourceIndex = allStations.indexOf(source.toLowerCase());
        int destinationIndex = allStations.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    /**
     * Checks if a train with the given train number exists and returns it.
     *
     * @param trainNumber the train number to look for
     * @return the Train object if found, else null
     */
    public Train trainExist(String trainNumber) {
        Optional<Train> findTrain = trainList.stream()
                .filter(train -> train.getTrainNumber().equals(trainNumber))
                .findFirst();

        return findTrain.orElse(null);
    }
}
