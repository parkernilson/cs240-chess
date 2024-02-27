package dataAccess;

import model.GameData;
// import model.UserData;

public interface GameDAO {
    public GameData getGame(int gameId);

    public GameData[] listGames(String username);

    public GameData createGame(GameData game);

    public void deleteGame(int gameId);
    public void deleteAllGames();

    public int getMaxGameId();

    // public UserData[] listParticipants(int gameId);
    // public UserData addParticipant(int gameId, UserData user);
}
