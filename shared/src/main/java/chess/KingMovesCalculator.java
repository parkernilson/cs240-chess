package chess;

import java.util.Collection;
import java.util.HashSet;

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
            final var forwardPiece = board.checkPiece(row + FORWARD, col);
            if (forwardPiece.isEmpty() || forwardPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col));
            }
        } catch(PositionOutOfBoundsException e) {}

        // forward right direction
        try {
            final var forwardRightPiece = board.checkPiece(row + FORWARD, col + RIGHT);
            if (forwardRightPiece.isEmpty() || forwardRightPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + RIGHT));
            }
        } catch(PositionOutOfBoundsException e) {}

        // right direction
        try {
            final var rightPiece = board.checkPiece(row, col + RIGHT);
            if (rightPiece.isEmpty() || rightPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row, col + RIGHT));
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward right direction
        try {
            final var backwardRightPiece = board.checkPiece(row + BACKWARD, col + RIGHT);
            if (backwardRightPiece.isEmpty() || backwardRightPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + BACKWARD, col + RIGHT));
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward direction
        try {
            final var backwardPiece = board.checkPiece(row + BACKWARD, col);
            if (backwardPiece.isEmpty() || backwardPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + BACKWARD, col));
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward left direction
        try {
            final var backwardLeftPiece = board.checkPiece(row + BACKWARD, col + LEFT);
            if (backwardLeftPiece.isEmpty() || backwardLeftPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + BACKWARD, col + LEFT));
            }
        } catch(PositionOutOfBoundsException e) {}

        // left direction
        try {
            final var leftPiece = board.checkPiece(row, col + LEFT);
            if (leftPiece.isEmpty() || leftPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row, col + LEFT));
            }
        } catch(PositionOutOfBoundsException e) {}

        // forward left direction
        try {
            final var forwardLeftPiece = board.checkPiece(row + FORWARD, col + LEFT);
            if (forwardLeftPiece.isEmpty() || forwardLeftPiece.get().getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + LEFT));
            }
        } catch(PositionOutOfBoundsException e) {}

        return possibleMoves;
    }
}
