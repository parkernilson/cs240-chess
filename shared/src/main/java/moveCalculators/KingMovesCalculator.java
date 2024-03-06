package moveCalculators;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PositionOutOfBoundsException;

public class KingMovesCalculator extends PieceMovesCalculator {
    
    public KingMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int row = myPosition.getRow();
        final int col = myPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        // king can move one space in any direction
        // forward direction
        try {
            final var forwardPiece = board.checkPiece(row + forward, col);
            if (forwardPiece.isEmpty() || forwardPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + forward, col));
            }
        } catch(PositionOutOfBoundsException e) {}

        // forward right direction
        try {
            final var forwardRightPiece = board.checkPiece(row + forward, col + right);
            if (forwardRightPiece.isEmpty() || forwardRightPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + forward, col + right));
            }
        } catch(PositionOutOfBoundsException e) {}

        // right direction
        try {
            final var rightPiece = board.checkPiece(row, col + right);
            if (rightPiece.isEmpty() || rightPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row, col + right));
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward right direction
        try {
            final var backwardRightPiece = board.checkPiece(row + backward, col + right);
            if (backwardRightPiece.isEmpty() || backwardRightPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + backward, col + right));
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward direction
        try {
            final var backwardPiece = board.checkPiece(row + backward, col);
            if (backwardPiece.isEmpty() || backwardPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + backward, col));
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward left direction
        try {
            final var backwardLeftPiece = board.checkPiece(row + backward, col + left);
            if (backwardLeftPiece.isEmpty() || backwardLeftPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + backward, col + left));
            }
        } catch(PositionOutOfBoundsException e) {}

        // left direction
        try {
            final var leftPiece = board.checkPiece(row, col + left);
            if (leftPiece.isEmpty() || leftPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row, col + left));
            }
        } catch(PositionOutOfBoundsException e) {}

        // forward left direction
        try {
            final var forwardLeftPiece = board.checkPiece(row + forward, col + left);
            if (forwardLeftPiece.isEmpty() || forwardLeftPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + forward, col + left));
            }
        } catch(PositionOutOfBoundsException e) {}

        return possibleMoves;
    }
}
