package serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;
import exceptions.ResponseException;
import model.UserData;
import service.UserService;

public class UserServiceMemoryTests {
    AuthDAO authDAO;
    UserDAO userDAO;
    UserService userService;

    @BeforeEach
    public void beforeEach() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void shouldGetUser() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        final var user = userService.getUser(username);

        assertEquals(username, user.username());
    }

    @Test
    public void shouldGetNullUserWhenNotExists() throws ResponseException {
        final var username = "username";
        final var user = userService.getUser(username);
        assertEquals(null, user);
    }

    @Test
    public void shouldGetAuth() throws DataAccessException, ResponseException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        final var auth = userService.createAuth(username);

        final var authData = userService.getAuth(auth.authToken());

        assertEquals(username, authData.username());
        assertEquals(auth.authToken(), authData.authToken());
    }

    @Test
    public void shouldGetNullAuthWhenNotExists() throws ResponseException {
        final var authToken = "authToken";
        final var authResult = userService.getAuth(authToken);
        assertEquals(null, authResult);
    }

    @Test
    public void shouldCreatUser() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        final var userResult = userService.getUser(username);

        assertEquals(userResult, user1);
    }

    @Test
    public void createUserShouldThrowIllegalArguments() {
        final String username = null;
        final String email = null;
        final String password = null;
        final var user1 = new UserData(username, email, password);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user1);
        });
    }

    @Test
    public void shouldGetUserByAuthToken() throws DataAccessException, ResponseException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        final var auth = userService.createAuth(username);

        final var userResult = userService.getByAuthToken(auth.authToken());

        assertEquals(userResult, user1);
    }

    @Test
    public void shouldGetNullUserByAuthTokenWhenNotExists() throws ResponseException {
        final var authToken = "authToken";
        final var userResult = userService.getByAuthToken(authToken);
        assertEquals(null, userResult);
    }

    @Test
    public void shouldCreateAuth() throws DataAccessException, ResponseException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        final var auth = userService.createAuth(username);

        final var authResult = userService.getAuth(auth.authToken());

        assertEquals(authResult, auth);
    }

    @Test
    public void shouldThrowDataAccessExceptionWhenUserNotFound() {
        final var username = "username";
        assertThrows(DataAccessException.class, () -> {
            userService.createAuth(username);
        });
    }

    @Test
    public void shouldDeleteAuth() throws DataAccessException, ResponseException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        final var auth = userService.createAuth(username);

        userService.deleteAuth(auth.authToken());

        final var authResult = userService.getAuth(auth.authToken());

        assertEquals(null, authResult);
    }

    @Test
    public void shouldNotThrowWhenAuthTokenDoesNotExist() throws ResponseException, DataAccessException {
        final var authToken = "authToken";
        userService.deleteAuth(authToken);
    }

    @Test
    public void shouldGetAuthByUsername() throws DataAccessException, ResponseException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        final var auth = userService.createAuth(username);

        final var authResult = userService.getAuthByUsername(username);

        assertEquals(authResult, auth);
    }

    @Test
    public void shouldGetNullWhenNoAuthByUsername() throws ResponseException {
        final var username = "username";
        final var authResult = userService.getAuthByUsername(username);
        assertEquals(null, authResult);
    }

    @Test
    public void shouldDeleteUser() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        userService.deleteUser(username);

        final var userResult = userService.getUser(username);

        assertEquals(null, userResult);
    }

    @Test
    public void shouldNotThrowWhenUserDoesNotExist() throws ResponseException, DataAccessException {  
        final var username = "username";
        userService.deleteUser(username);
    }

    @Test
    public void shouldDeleteAllUsers() throws ResponseException, DataAccessException {
        final var username = "username";
        final var email = "email";
        final var password = "password";
        final var user1 = new UserData(username, email, password);

        userService.createUser(user1);

        userService.deleteAllUsers();

        final var userResult = userService.getUser(username);

        assertEquals(null, userResult);
    }

    @Test
    public void shouldNotThrowWhenNoUsers() throws ResponseException, DataAccessException {
        userService.deleteAllUsers();
    }

}
