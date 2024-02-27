package dataAccess;

import java.util.HashMap;

import chess.ChessGame;
import model.GameData;

public class MemoryGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games = new HashMap<Integer, GameData>();

    public GameData getGame(int gameId) {
        return games.get(gameId);
    }

    public GameData[] listGames(String username) {
        return games.values().toArray(new GameData[0]);
    }

    public GameData createGame(GameData game) {
        games.put(game.gameId(), game);
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

    public void addParticipant(int gameId, String username, ChessGame.TeamColor color) throws DataAccessException {
        final var game = games.get(gameId);
        if (game == null) {
            throw new DataAccessException(username + " tried to join game " + gameId + " but it does not exist");
        }
        games.put(gameId, new GameData(gameId, color == ChessGame.TeamColor.WHITE ? username : game.whiteUsername(),
                color == ChessGame.TeamColor.BLACK ? username : game.blackUsername(), game.gameName(), game.game()));
    }
}
