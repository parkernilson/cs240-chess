package moveCalculators;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

public class BishopMovesCalculator extends PieceMovesCalculator {
    
    public BishopMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        // forward right moves
        StraightLineCalculator.addPossibleMovesInLine(forward, right, possibleMoves, board, pieceColor, myPosition);

        // backward right moves
        StraightLineCalculator.addPossibleMovesInLine(backward, right, possibleMoves, board, pieceColor, myPosition);
 
        // backward left moves
        StraightLineCalculator.addPossibleMovesInLine(backward, left, possibleMoves, board, pieceColor, myPosition);

        // forward left moves
        StraightLineCalculator.addPossibleMovesInLine(forward, left, possibleMoves, board, pieceColor, myPosition);


        return possibleMoves;
    }
}
