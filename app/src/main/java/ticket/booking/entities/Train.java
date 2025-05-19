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
}
