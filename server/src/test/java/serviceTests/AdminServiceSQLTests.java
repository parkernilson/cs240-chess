package serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.AdminService;
import service.GameService;
import service.UserService;

public class AdminServiceSQLTests {

    private static MemoryAuthDAO authDAO;
    private static MemoryUserDAO userDAO;
    private static MemoryGameDAO gameDAO;
    private static UserService userService;
    private static GameService gameService;
    private static AdminService adminService;

    @BeforeEach
    public void beforeEach() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO);
        adminService = new AdminService(userService, gameService);
    }

    @Test
    public void shouldClearApplication() throws DataAccessException, ResponseException {
        final String username = "username";
        final String password = "password";
        final String email = "email";
        final UserData user1 = new UserData(username, password, email);

        final int gameID = 1;
        final String gameName = "gameName";
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        userService.createUser(user1);
        final AuthData auth1 = userService.createAuth(username);
        gameService.createGame(game1);

        adminService.clearApplication();

        final var userResult = userService.getUser(username);
        final var authResult = userService.getAuth(auth1.authToken());
        final var gameResult = gameService.getGame(gameID);

        assertEquals(null, userResult);
        assertEquals(null, authResult);
        assertEquals(null, gameResult);
    }
}
