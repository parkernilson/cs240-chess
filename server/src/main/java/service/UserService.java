package service;

import dataAccess.AuthDAO;
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

    public UserData createUser(UserData userData) {
        return userDAO.createUser(userData);
    }

    public AuthData createAuth(String username) {
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
        authDAO.deleteAuth(auth.authToken());
    }

    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuth();
    }
}
