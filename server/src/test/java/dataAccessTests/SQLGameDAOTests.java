package dataAccessTests;

import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import chess.ChessGame.TeamColor;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import exceptions.ResponseException;
import model.GameData;
import service.AdminService;
import service.GameService;
import service.UserService;

public class SQLGameDAOTests {
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
    public void getGame() throws ResponseException, DataAccessException {
        final var gameId = 1;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var gameName = "gameName";
        final var game = new ChessGame();
        final var gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
        gameDAO.createGame(gameData);
        final var result = gameDAO.getGame(gameId);
        Assertions.assertEquals(result, gameData);
    }

    @Test
    public void getGameNotFound() throws ResponseException, DataAccessException {
        final var gameId = 1;
        final var result = gameDAO.getGame(gameId);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testListGames() throws ResponseException, DataAccessException {
        final var game1Id = 1;
        final var game2Id = 2;
        final var whiteUsername1 = "username1";
        final var whiteUsername2 = "username2";
        final var blackUsername1 = "username3";
        final var blackUsername2 = "username4";
        final var gameName1 = "gameName1";
        final var gameName2 = "gameName2";
        final var game1 = new ChessGame();
        final var game2 = new ChessGame();
        final var gameData1 = new GameData(game1Id, whiteUsername1, blackUsername1, gameName1, game1);
        final var gameData2 = new GameData(game2Id, whiteUsername2, blackUsername2, gameName2, game2);
        gameDAO.createGame(gameData1);
        gameDAO.createGame(gameData2);
        final var result = gameDAO.listGames(whiteUsername1);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 2);
        Assertions.assertTrue(result.contains(gameData1));
        Assertions.assertTrue(result.contains(gameData2));
    }

    @Test
    public void testListGamesNotFound() throws ResponseException, DataAccessException {
        final var username = "username";
        final var result = gameDAO.listGames(username);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof Collection);
        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    public void testCreateGame() throws ResponseException, DataAccessException {
        final var gameId = 1;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var gameName = "gameName";
        final var game = new ChessGame();
        final var gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
        gameDAO.createGame(gameData);
        final var result = gameDAO.getGame(gameId);
        Assertions.assertEquals(result, gameData);
    }

    @Test
    public void testCreateInvalidGame() throws ResponseException, DataAccessException {
        final var gameId = 1;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var gameName = "gameName";
        final var game = new ChessGame();
        final var gameData1 = new GameData(-1, whiteUsername, blackUsername, gameName, game);
        final var gameData2 = new GameData(gameId, whiteUsername, blackUsername, null, game);
        final var gameData3 = new GameData(gameId, whiteUsername, blackUsername, gameName, null);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData1));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData2));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData3));
    }

    @Test
    public void testUpdateGame() throws ResponseException, DataAccessException, InvalidMoveException {
        final var gameId = 1;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var gameName = "gameName";
        final var game = new ChessGame();
        final var gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
        gameDAO.createGame(gameData);
        final var newGame = new ChessGame();
        newGame.setTeamTurn(TeamColor.BLACK);
        final var newGameData = new GameData(gameId, whiteUsername, blackUsername, gameName, newGame);
        gameDAO.updateGame(newGameData);
        final var result = gameDAO.getGame(gameId);
        Assertions.assertEquals(result, newGameData);
    }

    @Test
    public void testInvalidUpdateGame() throws ResponseException, DataAccessException {
        final var gameId = 1;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var gameName = "gameName";
        final var game = new ChessGame();
        final var gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
        gameDAO.createGame(gameData);
        final var newGame = new ChessGame();
        final var newGameData = new GameData(-1, whiteUsername, blackUsername, gameName, newGame);
        final var newGameData2 = new GameData(gameId, whiteUsername, blackUsername, null, newGame);
        final var newGameData3 = new GameData(gameId, whiteUsername, blackUsername, gameName, null);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(newGameData));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(newGameData2));
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(newGameData3));
    }

    @Test
    public void testDeleteGame() throws ResponseException, DataAccessException {
        final var gameId = 1;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var gameName = "gameName";
        final var game = new ChessGame();
        final var gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
        gameDAO.createGame(gameData);
        gameDAO.deleteGame(gameId);
        final var result = gameDAO.getGame(gameId);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testDeleteNonExistentGame() throws ResponseException, DataAccessException {
        final var gameId = 1;
        gameDAO.deleteGame(gameId);
        gameDAO.deleteGame(gameId);
        final var result = gameDAO.getGame(gameId);
        Assertions.assertEquals(result, null);
    }

    @Test
    public void testDeleteAllGames() throws ResponseException, DataAccessException {
        final var gameId1 = 1;
        final var gameId2 = 2;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var whiteUsername2 = "username3";
        final var blackUsername2 = "username4";
        final var gameName = "gameName";
        final var gameName2 = "gameName2";
        final var game = new ChessGame();
        final var game2 = new ChessGame();
        final var gameData1 = new GameData(gameId1, whiteUsername, blackUsername, gameName, game);
        final var gameData2 = new GameData(gameId2, whiteUsername2, blackUsername2, gameName2, game2);
        gameDAO.createGame(gameData1);
        gameDAO.createGame(gameData2);
        gameDAO.deleteAllGames();
        final var result1 = gameDAO.getGame(gameId1);
        final var result2 = gameDAO.getGame(gameId2);
        Assertions.assertEquals(result1, null);
        Assertions.assertEquals(result2, null);
    }

    @Test
    public void getMaxGameId() throws ResponseException, DataAccessException {
        final var gameId1 = 10;
        final var gameId2 = 200;
        final var whiteUsername = "username";
        final var blackUsername = "username2";
        final var whiteUsername2 = "username3";
        final var blackUsername2 = "username4";
        final var gameName = "gameName";
        final var gameName2 = "gameName2";
        final var game = new ChessGame();
        final var game2 = new ChessGame();
        final var gameData1 = new GameData(gameId1, whiteUsername, blackUsername, gameName, game);
        final var gameData2 = new GameData(gameId2, whiteUsername2, blackUsername2, gameName2, game2);
        gameDAO.createGame(gameData1);
        gameDAO.createGame(gameData2);
        final var result = gameDAO.getMaxGameId();
        Assertions.assertEquals(result, gameId2);
    }
}
