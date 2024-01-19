package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends PieceMovesCalculator {
    
    public BishopMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int row = myPosition.getRow();
        final int col = myPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        // forward right moves
        try {
            for (int i = row + FORWARD, j = col + RIGHT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += FORWARD, j += RIGHT) {
                final var forwardRightPiece = board.checkPiece(i, j);
                if (forwardRightPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (forwardRightPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // forward left moves
        try {
            for (int i = row + FORWARD, j = col + LEFT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += FORWARD, j += LEFT) {
                final var forwardLeftPiece = board.checkPiece(i, j);
                if (forwardLeftPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (forwardLeftPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward right moves
        try {
            for (int i = row + BACKWARD, j = col + RIGHT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += BACKWARD, j += RIGHT) {
                final var backwardRightPiece = board.checkPiece(i, j);
                if (backwardRightPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (backwardRightPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward left moves
        try {
            for (int i = row + BACKWARD, j = col + LEFT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += BACKWARD, j += LEFT) {
                final var backwardLeftPiece = board.checkPiece(i, j);
                if (backwardLeftPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (backwardLeftPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        return possibleMoves;
    }
}
