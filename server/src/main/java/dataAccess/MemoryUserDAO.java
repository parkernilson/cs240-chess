package dataAccess;

import java.util.HashMap;

import model.UserData;

public class MemoryUserDAO implements UserDAO {
    
    /**
     * A map of usernames to user data.
     */
    private HashMap<String, UserData> users = new HashMap<String, UserData>();

    public UserData getUser(String username) {
        return users.get(username);
    }

    public UserData createUser(UserData user) {
        users.put(user.username(), user);
        return user;
    }

    public void deleteUser(String username) {
        users.remove(username);
    }

    public void deleteAllUsers() {
        users.clear();
    }
}
