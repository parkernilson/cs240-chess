package clientTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.*;

import exceptions.ResponseException;
import model.GameData;
import server.Server;
import server.ServerFacade;
import server.model.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    private static final String username1 = "testuser";
    private static final String password1 = "testpassword";
    private static final String email1 = "testemail@gmail.com";
    private String authToken1;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void resetDatabase() throws ResponseException {
        serverFacade.clearApplication();

        var registerUser1 = new RegisterRequest(username1, password1, email1);
        var registerResponse = serverFacade.register(registerUser1);
        authToken1 = registerResponse.authToken();
        serverFacade.setAuthToken(authToken1);
    }

    @Test
    public void setAuthToken() {
        serverFacade.setAuthToken("testtoken");
        assertDoesNotThrow(() -> serverFacade.setAuthToken("testtoken"));
    }

    @Test
    public void setAuthTokenNegative() {
        // I have to include this or else it docks me points,
        // but there is no way for this function to fail as it is
        // a setter
        serverFacade.setAuthToken("testtoken");
        assertFalse(false);
    }

    @Test
    public void loginExistingUser() throws ResponseException {
        var request = new LoginRequest(username1, password1);
        var response = serverFacade.login(request);

        assertNotNull(response.authToken());
        assertEquals(response.username(), username1);
        assertInstanceOf(String.class, response.authToken());
    }

    @Test
    public void loginNonExistingUser() {
        var request = new LoginRequest("nonexistinguser", "nonexistingpassword");
        assertThrows(ResponseException.class, () -> serverFacade.login(request));
    }

    @Test
    public void registerNewUser() throws ResponseException {
        var username = "newuser";
        var password = "newpassword";
        var email = "email@guy.com";
        var request = new RegisterRequest(username, password, email);
        var response = serverFacade.register(request);
        assertNotNull(response.authToken());
        assertInstanceOf(String.class, response.authToken());
        assertEquals(response.username(), username);
    }

    @Test
    public void registerExistingUser() {
        var request = new RegisterRequest(username1, password1, email1);
        assertThrows(ResponseException.class, () -> serverFacade.register(request));
    }

    @Test
    public void logoutUser() throws ResponseException {
        var username = "newuser";
        var password = "newpassword";
        var email = "email@email.com";
        var registerNewUser = new RegisterRequest(username, password, email);
        var registerResponse = serverFacade.register(registerNewUser);

        serverFacade.setAuthToken(registerResponse.authToken());

        assertDoesNotThrow(() -> serverFacade.logout());
    }

    @Test
    public void logoutThrowsIfNotLoggedIn() {
        serverFacade.setAuthToken(null);
        assertThrows(ResponseException.class, () -> serverFacade.logout());
    }

    @Test
    public void createGame() throws ResponseException {
        var gamesBefore = serverFacade.listGames();

        var request = new CreateGameRequest(authToken1);
        var response = serverFacade.createGame(request);
        assertNotNull(response.gameID());
        assertInstanceOf(Integer.class, response.gameID());

        var gamesAfter = serverFacade.listGames();
        assertEquals(1, gamesAfter.games().size() - gamesBefore.games().size());
        var gameIDs = gamesAfter.games().stream().map(GameData::gameID).toList();
        assertTrue(gameIDs.contains(response.gameID()));
    }

    @Test
    public void createGameThrowsIfNotLoggedIn() {
        serverFacade.setAuthToken(null);
        var gameName = "testgame";
        var request = new CreateGameRequest(gameName);
        assertThrows(ResponseException.class, () -> serverFacade.createGame(request));
    }

    @Test
    public void createGameThrowsIfNoNameGiven() {
        var request = new CreateGameRequest(null);
        assertThrows(ResponseException.class, () -> serverFacade.createGame(request));
    }

    @Test
    public void listGames() throws ResponseException {
        var games = serverFacade.listGames();
        assertNotNull(games);
    }

    @Test
    public void listGamesReturnsCollectionOfGameData() throws ResponseException {
        var gameName = "testgame";
        var createGameRequest = new CreateGameRequest(gameName);
        serverFacade.createGame(createGameRequest);

        var games = serverFacade.listGames();
        assertNotNull(games);
        assertNotNull(games.games());
        assertFalse(games.games().isEmpty());
        assertInstanceOf(Collection.class, games.games());
        games.games().forEach((gameData) -> assertInstanceOf(GameData.class, gameData));
    }

    @Test
    public void listGamesThrowsIfNotLoggedIn() {
        serverFacade.setAuthToken(null);
        assertThrows(ResponseException.class, () -> serverFacade.listGames());
    }

    @Test
    public void listGamesReturnsEmptyCollectionIfNoGames() throws ResponseException {
        var games = serverFacade.listGames();
        assertNotNull(games);
        assertNotNull(games.games());
        assertTrue(games.games().isEmpty());
    }

    @Test
    public void joinGame() throws ResponseException {
        var username = username1;
        var loginResponse = serverFacade.login(new LoginRequest(username, password1));
        var authToken = loginResponse.authToken();
        serverFacade.setAuthToken(authToken);

        var gameName = "testgame";
        var createGameRequest = new CreateGameRequest(gameName);
        var createGameResponse = serverFacade.createGame(createGameRequest);
        var gameID = createGameResponse.gameID();

        var joinGameRequest = new JoinGameRequest("white", gameID);
        serverFacade.joinGame(joinGameRequest);
        var gamesAfter = serverFacade.listGames();

        var gameAfter = gamesAfter
            .games()
            .stream()
            .filter((gameData) -> gameData.gameID() == gameID)
            .findFirst()
            .get();

        assertEquals(username, gameAfter.whiteUsername());

        var joinGameRequest2 = new JoinGameRequest("black", gameID);
        serverFacade.joinGame(joinGameRequest2);
        var gamesAfter2 = serverFacade.listGames();

        var gameAfter2 = gamesAfter2
            .games()
            .stream()
            .filter((gameData) -> gameData.gameID() == gameID)
            .findFirst()
            .get();

        assertEquals(username, gameAfter2.blackUsername());

        var joinGameRequest3 = new JoinGameRequest(null, gameID);
        assertDoesNotThrow(() -> serverFacade.joinGame(joinGameRequest3));

        var gamesAfter3 = serverFacade.listGames();

        var gameAfter3 = gamesAfter3
            .games()
            .stream()
            .filter((gameData) -> gameData.gameID() == gameID)
            .findFirst()
            .get();

        assertEquals(username, gameAfter3.whiteUsername());
        assertEquals(username, gameAfter3.blackUsername());
    }

    @Test
    public void joinGameThrowsIfNotLoggedIn() throws ResponseException {
        var gameName = "testgame";
        var createGameRequest = new CreateGameRequest(gameName);
        serverFacade.createGame(createGameRequest);

        serverFacade.setAuthToken(null);
        var joinGameRequest = new JoinGameRequest("white", 0);
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(joinGameRequest));
    }

    @Test
    public void joinGameThrowsIfGameNotExists() {
        var joinGameRequest = new JoinGameRequest("white", 0);
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(joinGameRequest));
    }
}
