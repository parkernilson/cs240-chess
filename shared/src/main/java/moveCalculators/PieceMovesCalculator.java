package moveCalculators;

import java.util.Collection;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.PositionOutOfBoundsException;

public abstract class PieceMovesCalculator {
    final int forward;
    final int backward;
    final int right;
    final int left;
    final ChessGame.TeamColor pieceColor;

    public PieceMovesCalculator(ChessGame.TeamColor pieceColor) {
        this.forward = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        this.backward = -this.forward;
        this.right = pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
        this.left = -this.right;
        this.pieceColor = pieceColor;
    }

    abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    public static boolean checkPawns(int row, int col, int forward, int right, int left, ChessGame.TeamColor teamColor,
            ChessBoard board) {
        // check for pawns in the front left and front right positions
        try {
            final var frontLeft = board.getNewPosition(row + forward, col + left);
            final var frontLeftPiece = board.getPiece(frontLeft);
            if (frontLeftPiece != null && frontLeftPiece.getTeamColor() != teamColor
                    && frontLeftPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                return true;
            }
        } catch (PositionOutOfBoundsException e) {
        }
        try {
            final var frontRight = board.getNewPosition(row + forward, col + right);
            final var frontRightPiece = board.getPiece(frontRight);
            if (frontRightPiece != null && frontRightPiece.getTeamColor() != teamColor
                    && frontRightPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                return true;
            }
        } catch (PositionOutOfBoundsException e) {
        }
        return false;
    }
}
