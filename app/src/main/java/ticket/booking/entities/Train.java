package ticket.booking.entities;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Train {
    private String trainId;
    private String trainNumber;
    private List<List<Integer>> seatLayout;
    private Map<String, LocalTime> stationArrivalTimes;
    private List<String> stations;

    public Train(String trainId, String trainNumber, List<List<Integer>> seatLayout, Map<String, LocalTime> stationArrivalTimes, List<String> stations) {
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.seatLayout = seatLayout;
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

    public List<List<Integer>> getSeatLayout() {
        return seatLayout;
    }

    public void setSeatLayout(List<List<Integer>> seatLayout) {
        this.seatLayout = seatLayout;
    }

    public Map<String, LocalTime> getStationArrivalTimes() {
        return stationArrivalTimes;
    }

    public void setStationArrivalTimes(Map<String, LocalTime> stationArrivalTimes) {
        this.stationArrivalTimes = stationArrivalTimes;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}
