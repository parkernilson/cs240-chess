package dataAccess;

import model.UserData;

public interface UserDAO {
    public UserData getUser(String username);
    public UserData createUser(UserData user);
    public void deleteUser(String username);
    public UserData getUserByToken(String authToken);
    public void deleteAllUsers();
}
