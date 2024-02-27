package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

public class GameService {
    private GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void deleteAllGames() {
        gameDAO.deleteAllGames();
    }

    public GameData[] listGames(String username) {
        return gameDAO.listGames(username);
    }

    public GameData createGame(GameData gameData) {
        return gameDAO.createGame(gameData);
    }

    public GameData getGame(int gameId) {
        return gameDAO.getGame(gameId);
    }

    public int getNextGameId() {
        return gameDAO.getMaxGameId() + 1;
    }

    public void addParticipant(int gameId, String username, ChessGame.TeamColor color) throws DataAccessException {
        gameDAO.addParticipant(gameId, username, color);
    }
}
