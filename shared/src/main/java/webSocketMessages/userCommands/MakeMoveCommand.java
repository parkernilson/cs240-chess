package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private ChessMove move;

    public MakeMoveCommand(String authToken, ChessMove move) {
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
