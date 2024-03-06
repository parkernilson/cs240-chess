package dataAccessTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import dataAccess.ResponseException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.UserData;
import service.AdminService;
import service.GameService;
import service.UserService;

public class SQLUserDAOTests {
    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static UserService userService;
    private static GameService gameService;
    private static AdminService adminService;

    @BeforeEach
    public void beforeEach() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();

        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO);
        adminService = new AdminService(userService, gameService);

        adminService.clearApplication();
    }
    
    @Test
    public void testGetUser() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";

        final var user = new UserData(username, email, password);
        userDAO.createUser(user);
        final var result = userDAO.getUser(username);
        Assertions.assertEquals(result, user);
    }

    @Test
    public void testGetUserNotFound() throws ResponseException, DataAccessException {
        final var username = "username";
        final var result = userDAO.getUser(username);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testCreateUser() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";

        final var user = new UserData(username, email, password);
        userDAO.createUser(user);
        final var result = userDAO.getUser(username);
        Assertions.assertEquals(result, user);
    }

    @Test
    public void testCreateInvalidUser() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";

        final var user1 = new UserData(null, email, password);
        final var user2 = new UserData(username, null, password);
        final var user3 = new UserData(username, email, null);

        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(user1));
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(user2));
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(user3));
    }

    @Test
    public void testDeleteUser() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";

        final var user = new UserData(username, email, password);
        userDAO.createUser(user);
        userDAO.deleteUser(username);
        final var result = userDAO.getUser(username);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testDeleteNonExistentUser() throws ResponseException, DataAccessException {
        final var username = "username";
        userDAO.deleteUser(username);
        final var result = userDAO.getUser(username);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testDeleteAllUsers() throws ResponseException, DataAccessException {
        final var username1 = "username1";
        final var email1 = "email1";
        final var password1 = "password1";
        final var user1 = new UserData(username1, email1, password1);
        userDAO.createUser(user1);

        final var username2 = "username2";
        final var email2 = "email2";
        final var password2 = "password2";
        final var user2 = new UserData(username2, email2, password2);
        userDAO.createUser(user2);

        userDAO.deleteAllUsers();
        final var result1 = userDAO.getUser(username1);
        final var result2 = userDAO.getUser(username2);
        Assertions.assertEquals(result1, null);
        Assertions.assertEquals(result2, null);
    }
    
}
