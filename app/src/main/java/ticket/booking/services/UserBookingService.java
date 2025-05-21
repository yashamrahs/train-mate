package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDB/users.json";

    private User user;
    private List<User> usersList;
    private ObjectMapper objectMapper = new ObjectMapper();

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserList();
    }

    public UserBookingService() throws IOException {
        loadUserList();
    }

    public boolean signUp(User user) throws IOException {
        try {
            usersList.add(user);
            saveUserList();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean login() {
        Optional<User> foundUser = usersList.stream().filter(u -> {
            return u.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), u.getPassword());
        }).findFirst();

        return foundUser.isPresent();
    }

    public void fetchBookings() {
        Optional<User> userFetched = usersList.stream().filter(u -> {
            return u.getName() == user.getName() && UserServiceUtil.checkPassword(user.getPassword(), u.getPassword());
        }).findFirst();

        userFetched.ifPresent(User::printTickets);
    }

    public List<Train> searchTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<Integer>> findSeats(Train train) {
        return train.getSeats();
    }

    public boolean bookTrainTicket(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if (row >= 0 && row < seats.size() && seat > 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.updateTrain(train);
                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private void loadUserList() throws IOException {
        System.out.println("Reading from: " + USERS_PATH);
        File users = new File(USERS_PATH);
        usersList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    private void saveUserList() throws IOException {
        File users = new File(USERS_PATH);
        objectMapper.writeValue(users, usersList);
    }
}