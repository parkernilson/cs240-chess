package service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import util.TokenGenerator;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public UserData getUser(String username) throws ResponseException {
        return userDAO.getUser(username);
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        return authDAO.getAuth(authToken);
    }

    public UserData createUser(UserData userData) throws ResponseException, DataAccessException {
        if (userData.username() == null || userData.email() == null || userData.password() == null)
            throw new IllegalArgumentException("Invalid user data");
        return userDAO.createUser(userData);
    }

    public UserData getByAuthToken(String authToken) throws ResponseException {
        final var auth = authDAO.getAuth(authToken);
        if (auth == null) {
            return null;
        }
        return userDAO.getUser(auth.username());
    }

    public AuthData createAuth(String username) throws DataAccessException, ResponseException {
        final var user = userDAO.getUser(username);
        if (user == null) throw new DataAccessException("User not found");
        String authToken = TokenGenerator.generateToken();
        return authDAO.createAuth(username, authToken);
    }

    public void deleteAuth(String authToken) throws ResponseException, DataAccessException {
        authDAO.deleteAuth(authToken);
    }

    public AuthData getAuthByUsername(String username) throws ResponseException {
        return authDAO.getAuthByUsername(username);
    }

    public void deleteUser(String username) throws ResponseException, DataAccessException {
        final var auth = authDAO.getAuthByUsername(username);
        userDAO.deleteUser(username);
        if (auth != null) authDAO.deleteAuth(auth.authToken());
    }

    public void deleteAllUsers() throws ResponseException, DataAccessException {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuth();
    }

    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
