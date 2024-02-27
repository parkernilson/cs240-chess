package dataAccess;

import model.GameData;

public interface GameDAO {
    public GameData getGame(int gameId);

    public GameData[] listGames(String username);

    public GameData createGame(GameData game);

    public GameData updateGame(GameData game) throws DataAccessException;

    public void deleteGame(int gameId);

    public void deleteAllGames();

    public int getMaxGameId();
}
