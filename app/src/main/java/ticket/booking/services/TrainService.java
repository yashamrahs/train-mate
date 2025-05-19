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

    public TrainService() throws IOException {
        File trains = new File(TRAIN_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {}) ;
    }

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

    public void updateTrain(Train train) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(train.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), train);
            saveTrainList();
        } else {
            addTrain(train);
        }
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    private void saveTrainList() {
        try {
            objectMapper.writeValue(new File(TRAIN_PATH), trainList);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> allStations = train.getStations();

        int sourceIndex = allStations.indexOf(source.toLowerCase());
        int destinationIndex = allStations.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
