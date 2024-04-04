package webSocketMessages.userCommands;

public class JoinGameCommand extends UserGameCommand {
    private int gameID;

    public JoinGameCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
