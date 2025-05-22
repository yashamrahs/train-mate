package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.IntStream;

public class UserBookingService {

    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDB/users.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<User> usersList;
    private User user;

    /**
     * Constructs a UserBookingService with a given user.
     *
     * @param user the current user
     * @throws IOException if user data fails to load
     */
    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserList();
    }

    /**
     * Default constructor that loads user data from local storage.
     *
     * @throws IOException if user data fails to load
     */
    public UserBookingService() throws IOException {
        loadUserList();
    }

    /**
     * Returns the currently logged-in user.
     */
    public User getCurrentUser() {
        return this.user;
    }

    /**
     * Registers a new user and saves the data.
     *
     * @param user the user to be signed up
     * @return true if successful, false otherwise
     * @throws IOException if saving fails
     */
    public boolean signUp(User user) throws IOException {
        try {
            this.user = user;
            usersList.add(user);
            saveUserList();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Authenticates a user using username and password.
     *
     * @param username the username
     * @param password the raw password
     * @return true if login is successful, false otherwise
     */
    public boolean login(String username, String password) {
        Optional<User> foundUser = usersList.stream().filter(u ->
                u.getName().equals(username) && UserServiceUtil.checkPassword(password, u.getHashedPassword())
        ).findFirst();

        if (foundUser.isPresent()) {
            this.user = foundUser.get();
            return true;
        }

        return false;
    }

    /**
     * Displays all bookings made by the current user.
     */
    public void fetchBookings() {
        Optional<User> userFetched = usersList.stream().filter(u ->
                u.getName().equals(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), u.getHashedPassword())
        ).findFirst();

        userFetched.ifPresent(User::printTickets);
    }

    /**
     * Searches for available trains between source and destination.
     *
     * @param source      the departure station
     * @param destination the arrival station
     * @return list of matching trains
     */
    public List<Train> searchTrains(String source, String destination) {
        try {
            return new TrainService().searchTrains(source, destination);
        } catch (IOException e) {
            throw new RuntimeException("Error searching trains", e);
        }
    }

    /**
     * Finds a train by its train number.
     *
     * @param trainNumber the train number
     * @return the Train object if found
     */
    public Train findTrain(String trainNumber) {
        try {
            return new TrainService().trainExist(trainNumber);
        } catch (IOException e) {
            throw new RuntimeException("Train lookup failed", e);
        }
    }

    /**
     * Books a ticket for the specified user and train if the seat is available.
     *
     * @param userId      the user ID
     * @param source      journey start point
     * @param destination journey end point
     * @param travelDate  date of travel
     * @param train       train object
     * @param row         seat row
     * @param seat        seat column
     * @return true if booking is successful, false otherwise
     */
    public boolean bookTrainTicket(String userId, String source, String destination, String travelDate, Train train, int row, int seat) {
        try {
            if (!isSeatValidAndAvailable(train, row, seat)) {
                return false;
            }

            // Mark seat as booked
            train.getSeats().get(row).set(seat, 1);
            new TrainService().updateTrain(train);

            // Generate ticket and assign to user
            Ticket ticket = new Ticket(UUID.randomUUID().toString(), userId, source, destination, travelDate, train);
            this.user.getTicketsBooked().add(ticket);
            updateUser(this.user);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks whether a given seat is within valid bounds and available.
     */
    private boolean isSeatValidAndAvailable(Train train, int row, int seat) {
        List<List<Integer>> seats = train.getSeats();
        return row >= 0 && row < seats.size()
                && seat >= 0 && seat < seats.get(row).size()
                && seats.get(row).get(seat) == 0;
    }

    /**
     * Updates a user's data in the stored list and persists it.
     *
     * @param user the updated user
     * @throws IOException if saving fails
     */
    private void updateUser(User user) throws IOException {
        OptionalInt index = IntStream.range(0, usersList.size())
                .filter(i -> usersList.get(i).getUserId().equalsIgnoreCase(user.getUserId()))
                .findFirst();

        if (index.isPresent()) {
            usersList.set(index.getAsInt(), user);
            saveUserList();
        }
    }

    /**
     * Cancels a booked ticket by ticket ID.
     *
     * @param ticketId the ID of the ticket to be cancelled
     * @return true if ticket was found and removed, false otherwise
     * @throws IOException if saving user list fails
     */
    public boolean cancelTicket(String ticketId) throws IOException {
        OptionalInt index = IntStream.range(0, user.getTicketsBooked().size())
                .filter(i -> user.getTicketsBooked().get(i).getTicketId().equals(ticketId)).findFirst();

        if (index.isPresent()) {
            List<Ticket> tickets = user.getTicketsBooked();
            tickets.remove(index.getAsInt());
            user.setTicketsBooked(tickets);
            updateUser(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Loads user data from local storage (JSON file).
     *
     * @throws IOException if file reading or parsing fails
     */
    private void loadUserList() throws IOException {
        File users = new File(USERS_PATH);
        usersList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    /**
     * Saves user data to local storage (JSON file).
     *
     * @throws IOException if writing to file fails
     */
    private void saveUserList() throws IOException {
        File users = new File(USERS_PATH);
        objectMapper.writeValue(users, usersList);
    }
}
