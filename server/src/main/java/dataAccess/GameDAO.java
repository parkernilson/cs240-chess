package dataAccess;

import java.util.Collection;

import exceptions.ResponseException;
import model.GameData;

public interface GameDAO {
    public GameData getGame(int gameId) throws ResponseException;

    public Collection<GameData> listGames(String username) throws ResponseException;

    public GameData createGame(GameData game) throws ResponseException, DataAccessException;

    public GameData updateGame(GameData game) throws DataAccessException, ResponseException;

    public void deleteGame(int gameId) throws ResponseException, DataAccessException;

    public void deleteAllGames() throws ResponseException, DataAccessException;

    public int getMaxGameId() throws ResponseException;
}
