package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

public interface GameDAO {
    public GameData getGame(int gameId);

    public GameData[] listGames(String username);

    public GameData createGame(GameData game);

    public void deleteGame(int gameId);
    public void deleteAllGames();

    public int getMaxGameId();

    // public UserData[] listParticipants(int gameId);
    public void addParticipant(int gameId, String username, ChessGame.TeamColor color) throws DataAccessException;
}
