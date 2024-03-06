package service;

import java.util.Collection;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.ResponseException;
import model.GameData;

public class GameService {
    private GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void deleteAllGames() throws ResponseException, DataAccessException {
        gameDAO.deleteAllGames();
    }

    public Collection<GameData> listGames(String username) throws ResponseException {
        return gameDAO.listGames(username);
    }

    public GameData createGame(GameData gameData) throws ResponseException, DataAccessException {
        if (gameData.gameID() < 0 || gameData.gameName() == null)
            throw new IllegalArgumentException("Invalid game data");
        return gameDAO.createGame(gameData);
    }

    public GameData getGame(int gameId) throws ResponseException {
        return gameDAO.getGame(gameId);
    }

    public int getNextGameId() throws ResponseException {
        return gameDAO.getMaxGameId() + 1;
    }

    public void addParticipant(int gameId, String username, ChessGame.TeamColor color) throws DataAccessException, ResponseException {
        final var game = gameDAO.getGame(gameId);
        if (game == null) throw new DataAccessException("Game not found");
        final var newGame = new GameData(
            gameId, 
            color == ChessGame.TeamColor.WHITE ? username : game.whiteUsername(),
            color == ChessGame.TeamColor.BLACK ? username : game.blackUsername(), 
            game.gameName(), 
            game.game()
        );
        gameDAO.updateGame(newGame);
    }
}
