package webSocketMessages.userCommands;

import chess.ChessGame.TeamColor;

public class JoinGameCommand extends UserGameCommand {
    private int gameID;
    private TeamColor playerColor;

    public JoinGameCommand(String authToken, int gameID, TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public TeamColor getPlayerColor() {
        return playerColor;
    }
}
