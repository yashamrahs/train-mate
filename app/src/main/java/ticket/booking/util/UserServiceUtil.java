package ticket.booking.util;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param password The plain-text password to be hashed.
     * @return The hashed password string.
     */
    public static String hashPassword(String password) {
        // Generate salt internally and hash the password
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifies a plain-text password against a previously hashed password.
     *
     * @param password       The plain-text password provided during login.
     * @param hashedPassword The hashed password stored in the system.
     * @return {@code true} if the password matches the hash, {@code false} otherwise.
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        // Compare raw input password with the stored hashed version
        return BCrypt.checkpw(password, hashedPassword);
    }

}
