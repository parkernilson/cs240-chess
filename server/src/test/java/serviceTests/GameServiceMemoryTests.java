package serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;
import exceptions.ResponseException;
import model.GameData;
import service.GameService;

public class GameServiceMemoryTests {
    private static MemoryGameDAO gameDAO;
    private static GameService gameService;

    @BeforeEach
    public void beforeEach() {
        gameDAO = new MemoryGameDAO();
        gameService = new GameService(gameDAO);
    }

    @Test
    void shouldListGames() throws ResponseException, DataAccessException {
        final int gameID = 1;
        final String gameName = "gameName";
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        final int gameID2 = 2;
        final String gameName2 = "gameName2";
        final GameData game2 = new GameData(gameID2, null, null, gameName2, new ChessGame());

        gameService.createGame(game1);
        gameService.createGame(game2);

        final String username = "username";

        final var games = gameService.listGames(username);

        assertEquals(2, games.size());
    }

    @Test
    void shouldListEmptyGames() throws ResponseException {
        final String username = "username";

        final var games = gameService.listGames(username);

        assertEquals(0, games.size());
    }

    @Test 
    void shouldDeleteAllGames() throws ResponseException, DataAccessException {
        final int gameID = 1;
        final String gameName = "gameName";
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        gameService.createGame(game1);

        gameService.deleteAllGames();

        final var games = gameService.listGames(null);

        assertEquals(0, games.size());
    }

    @Test
    void shouldDeleteAllGamesEvenWhenEmpty() throws ResponseException, DataAccessException {
        gameService.deleteAllGames();

        final var games = gameService.listGames(null);

        assertEquals(0, games.size());
    }

    @Test
    void shouldCreateGame() throws ResponseException, DataAccessException {
        final int gameID = 1;
        final String gameName = "gameName";
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        gameService.createGame(game1);
        final var gameResult = gameService.getGame(gameID);

        assertEquals(gameID, gameResult.gameID());
        assertEquals(gameName, gameResult.gameName());
    }

    @Test
    void createGameShouldThrowIllegalArguments() {
        final int gameID = -1;
        final String gameName = null;
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        assertThrows(IllegalArgumentException.class, () -> {
            gameService.createGame(game1);
        });
    }

    @Test
    void shouldGetGame() throws ResponseException, DataAccessException {
        final int gameID = 1;
        final String gameName = "gameName";
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        gameService.createGame(game1);
        final var gameResult = gameService.getGame(gameID);

        assertEquals(gameID, gameResult.gameID());
        assertEquals(gameName, gameResult.gameName());
    }

    @Test
    void shouldReturnNullWhenGameNotFound() throws ResponseException {
        final int gameID = 1;

        final var gameResult = gameService.getGame(gameID);

        assertEquals(null, gameResult);
    }

    @Test
    void shouldGetNextGameId() throws ResponseException, DataAccessException {
        final int gameID = 1;
        final String gameName = "gameName";
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        gameService.createGame(game1);
        final var nextGameId = gameService.getNextGameId();

        assertEquals(gameID + 1, nextGameId);
    }

    @Test
    void shouldGet1AsFirstGameId() throws ResponseException {
        final var nextGameId = gameService.getNextGameId();

        assertEquals(1, nextGameId);
    }

    @Test
    void shouldAddParticipant() throws DataAccessException, ResponseException {
        final int gameID = 1;
        final String gameName = "gameName";
        final GameData game1 = new GameData(gameID, null, null, gameName, new ChessGame());

        gameService.createGame(game1);
        gameService.addParticipant(gameID, "username", ChessGame.TeamColor.WHITE);

        final var gameResult = gameService.getGame(gameID);

        assertEquals("username", gameResult.whiteUsername());
    }

    @Test
    void shouldThrowDataAccessExceptionWhenAddParticipantToNonexistentGame() {
        final int gameID = 1;

        assertThrows(DataAccessException.class, () -> {
            gameService.addParticipant(gameID, "username", ChessGame.TeamColor.WHITE);
        });
    }

}
