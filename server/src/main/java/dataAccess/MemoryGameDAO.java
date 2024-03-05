package dataAccess;

import java.util.Collection;
import java.util.HashMap;

import model.GameData;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games = new HashMap<Integer, GameData>();

    public GameData getGame(int gameId) {
        return games.get(gameId);
    }

    public Collection<GameData> listGames(String username) {
        return games.values();
    }

    public GameData createGame(GameData game) {
        games.put(game.gameID(), game);
        return game;
    }

    public GameData updateGame(GameData game) throws DataAccessException {
        final var existingGame = games.get(game.gameID());
        if (existingGame == null) {
            throw new DataAccessException("Game " + game.gameID() + " does not exist");
        }
        games.put(game.gameID(), game);
        return game;
    }

    public void deleteGame(int gameId) {
        games.remove(gameId);
    }

    public void deleteAllGames() {
        games.clear();
    }

    @SuppressWarnings("null")
    public int getMaxGameId() {
        return games.keySet().stream().max(Integer::compare).orElse(0);
    }

}
