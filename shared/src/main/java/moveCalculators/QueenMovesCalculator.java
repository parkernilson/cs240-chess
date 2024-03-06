package moveCalculators;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

public class QueenMovesCalculator extends PieceMovesCalculator {
    
    public QueenMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        // forward moves
        StraightLineCalculator.addPossibleMovesInLine(forward, 0, possibleMoves, board, pieceColor, myPosition);

        // forward right moves
        StraightLineCalculator.addPossibleMovesInLine(forward, right, possibleMoves, board, pieceColor, myPosition);

        // right moves
        StraightLineCalculator.addPossibleMovesInLine(0, right, possibleMoves, board, pieceColor, myPosition);

        // backward right moves
        StraightLineCalculator.addPossibleMovesInLine(backward, right, possibleMoves, board, pieceColor, myPosition);

        // backward moves
        StraightLineCalculator.addPossibleMovesInLine(backward, 0, possibleMoves, board, pieceColor, myPosition);
 
        // backward left moves
        StraightLineCalculator.addPossibleMovesInLine(backward, left, possibleMoves, board, pieceColor, myPosition);

        // left moves
        StraightLineCalculator.addPossibleMovesInLine(0, left, possibleMoves, board, pieceColor, myPosition);

        // forward left moves
        StraightLineCalculator.addPossibleMovesInLine(forward, left, possibleMoves, board, pieceColor, myPosition);

        return possibleMoves;
    }
}
