package chess;

import java.util.Collection;

public abstract class PieceMovesCalculator {
    final int FORWARD;
    final int BACKWARD;
    final int RIGHT;
    final int LEFT;
    final ChessGame.TeamColor pieceColor;

    public PieceMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.FORWARD = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        this.BACKWARD = -this.FORWARD;
        this.RIGHT = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        this.LEFT = -this.RIGHT;
        this.pieceColor = pieceColor;
    }

    abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
