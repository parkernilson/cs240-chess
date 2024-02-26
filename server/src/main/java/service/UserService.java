package service;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;

public class UserService {
    public UserDAO userDAO;
    public AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuth();
    }
}
