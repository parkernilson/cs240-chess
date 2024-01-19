package chess;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends PieceMovesCalculator {
    
    private static int[][] POSSIBLE_JUMPS = {
        {1, 2},
        {2, 1},
        {2, -1},
        {1, -2},
        {-1, -2},
        {-2, -1},
        {-2, 1},
        {-1, 2}
    };
    
    public KnightMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int row = myPosition.getRow();
        final int col = myPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        // coordinates of possible jumps
        for (int[] jump : POSSIBLE_JUMPS) {
            int newRow = row + jump[0];
            int newCol = col + jump[1];
            try {
                final var jumpPiece = board.checkPiece(newRow, newCol);
                if (jumpPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, newRow, newCol));
                } else if (jumpPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, newRow, newCol));
                }
            } catch(PositionOutOfBoundsException e) {}
        }

        return possibleMoves;
    }
}
