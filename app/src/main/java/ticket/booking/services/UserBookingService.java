package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private void loadUserList() throws IOException {
        File users = new File(USERS_PATH);
        usersList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    private void saveUserList() throws IOException {
        File users = new File(USERS_PATH);
        objectMapper.writeValue(users, usersList);
    }


}