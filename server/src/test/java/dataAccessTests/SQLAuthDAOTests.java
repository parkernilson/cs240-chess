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
import model.AuthData;
import service.AdminService;
import service.GameService;
import service.UserService;

public class SQLAuthDAOTests {
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
    public void testGetAuth() throws ResponseException, DataAccessException {
        final var username = "username";
        final var authToken = "authtoken";
        final var authData = new AuthData(authToken, username);

        authDAO.createAuth(username, authToken);
        final var result = authDAO.getAuth(authToken);
        Assertions.assertEquals(result, authData);
    }

    @Test
    public void testGetAuthNotFound() throws ResponseException, DataAccessException {
        final var authToken = "authtoken";
        final var result = authDAO.getAuth(authToken);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testGetAuthByUsername() throws ResponseException, DataAccessException {
        final var username = "username";
        final var authToken = "authtoken";
        final var authData = new AuthData(authToken, username);

        authDAO.createAuth(username, authToken);
        final var result = authDAO.getAuthByUsername(username);
    
        Assertions.assertEquals(result, authData);
    }

    @Test
    public void testGetAuthByUsernameNotFound() throws ResponseException, DataAccessException {
        final var username = "username";
        final var result = authDAO.getAuthByUsername(username);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testCreateAuth() throws ResponseException, DataAccessException {
        final var username = "username";
        final var authToken = "authtoken";
        final var authData = new AuthData(authToken, username);

        authDAO.createAuth(username, authToken);
        final var result = authDAO.getAuth(authToken);

        Assertions.assertEquals(result, authData);
    }

    @Test
    public void testCreateAuthInvalid() throws ResponseException, DataAccessException {
        final var username = "username";
        final var authToken = "authtoken";

        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(username, null);
        });

        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(null, authToken);
        });
    }

    @Test
    public void testDeleteAuth() throws ResponseException, DataAccessException {
        final var username = "username";
        final var authToken = "authtoken";

        authDAO.createAuth(username, authToken);
        authDAO.deleteAuth(authToken);

        final var result = authDAO.getAuth(authToken);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testDeleteNonExistentAuth() throws ResponseException, DataAccessException {
        final var authToken = "authtoken";

        authDAO.deleteAuth(authToken);

        final var result = authDAO.getAuth(authToken);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testDeleteAllAuth() throws ResponseException, DataAccessException {
        final var username = "username";
        final var username2 = "username2";
        final var authToken = "authtoken";
        final var authToken2 = "authtoken2";
        
        authDAO.createAuth(username, authToken);
        authDAO.createAuth(username2, authToken2);
        authDAO.deleteAllAuth();

        final var result = authDAO.getAuth(authToken);
        final var result2 = authDAO.getAuth(authToken2);

        Assertions.assertEquals(result, null);
        Assertions.assertEquals(result2, null);
    }
}
