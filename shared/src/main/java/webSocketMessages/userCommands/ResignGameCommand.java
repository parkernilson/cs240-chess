package webSocketMessages.userCommands;

public class ResignGameCommand extends UserGameCommand {
    public int gameID;

    public ResignGameCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
    
    public int getGameID() {
        return gameID;
    }
}
