package webSocketMessages.userCommands;

import chess.ChessGame.TeamColor;

public class JoinGameCommand extends UserGameCommand {
    private int gameID;
    private TeamColor teamColor;

    public JoinGameCommand(String authToken, int gameID, TeamColor teamColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.teamColor = teamColor;
    }

    public int getGameID() {
        return gameID;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }
}
