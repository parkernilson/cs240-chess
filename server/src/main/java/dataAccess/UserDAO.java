package dataAccess;

import exceptions.ResponseException;
import model.UserData;

public interface UserDAO {
    public UserData getUser(String username) throws ResponseException;
    public UserData createUser(UserData user) throws ResponseException, DataAccessException;
    public void deleteUser(String username) throws ResponseException, DataAccessException;
    public void deleteAllUsers() throws ResponseException, DataAccessException;
}
