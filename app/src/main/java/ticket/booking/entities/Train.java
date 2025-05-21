package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Train {
    private String trainId;
    private String trainNumber;
    private List<List<Integer>> seats;
    private Map<String, String> stationArrivalTimes;
    private List<String> stations;

    public Train(String trainId, String trainNumber, List<List<Integer>> seats, Map<String, String> stationArrivalTimes, List<String> stations) {
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.seats = seats;
        this.stationArrivalTimes = stationArrivalTimes;
        this.stations = stations;
    }

    public Train() {}

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationArrivalTimes() {
        return stationArrivalTimes;
    }

    public void setStationArrivalTimes(Map<String, String> stationArrivalTimes) {
        this.stationArrivalTimes = stationArrivalTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}
