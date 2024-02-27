package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public UserData getUser(String username) {
        return userDAO.getUser(username);
    }

    public AuthData getAuth(String authToken) {
        return authDAO.getAuth(authToken);
    }

    public UserData createUser(UserData userData) {
        if (userData.username() == null || userData.email() == null || userData.password() == null)
            throw new IllegalArgumentException("Invalid user data");
        return userDAO.createUser(userData);
    }

    public UserData getByAuthToken(String authToken) {
        final var auth = authDAO.getAuth(authToken);
        if (auth == null) {
            return null;
        }
        return userDAO.getUser(auth.username());
    }

    public AuthData createAuth(String username) throws DataAccessException {
        final var user = userDAO.getUser(username);
        if (user == null) throw new DataAccessException("User not found");
        return authDAO.createAuth(username);
    }

    public void deleteAuth(String authToken) {
        authDAO.deleteAuth(authToken);
    }

    public AuthData getAuthByUsername(String username) {
        return authDAO.getAuthByUsername(username);
    }

    public void deleteUser(String username) {
        final var auth = authDAO.getAuthByUsername(username);
        userDAO.deleteUser(username);
        if (auth != null) authDAO.deleteAuth(auth.authToken());
    }

    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuth();
    }
}
