package moveCalculators;

import java.util.Collection;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PositionOutOfBoundsException;
import chess.ChessGame.TeamColor;

public class StraightLineCalculator {
    public static void addPossibleMovesInLine(
        int rowIncrement,
        int colIncrement,
        Collection<ChessMove> possibleMoves, 
        ChessBoard board,
        TeamColor pieceColor,
        ChessPosition myPosition
    ) {
        final int row = myPosition.getRow();
        final int col = myPosition.getColumn();
        try {
            for (int i = row + rowIncrement, j = col + colIncrement; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1
                    && j <= ChessBoard.WIDTH; i += rowIncrement, j += colIncrement) {
                final var piece = board.checkPiece(i, j);
                if (piece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (piece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch (PositionOutOfBoundsException e) {
        }
    }
}
