package moveCalculators;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.PositionOutOfBoundsException;

public class PawnMovesCalculator extends PieceMovesCalculator {
    
    private static final int BLACK_PAWN_HOME_ROW = 7;
    private static final int WHITE_PAWN_HOME_ROW = 2;
    
    public PawnMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int row = myPosition.getRow();
        final int col = myPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        // forward moves
        try {
            // can move forward one space if that space is empty
            final var forwardPiece = board.checkPiece(row + forward, col);
            if (forwardPiece.isEmpty()) {
                // if the pawn is moving to the last row (1 for black, 8 for white), then it can promote
                if (row + forward == 1 || row + forward == ChessBoard.HEIGHT) {
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col, ChessPiece.PieceType.KNIGHT));
                } 
                // otherwise it is a normal non-promoting move
                else {
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col));
                }

                // can move forward two spaces if it is the first move and both spaces are empty
                if (
                    (pieceColor == ChessGame.TeamColor.BLACK && row == BLACK_PAWN_HOME_ROW)
                    || (pieceColor == ChessGame.TeamColor.WHITE && row == WHITE_PAWN_HOME_ROW)
                ) {
                    final var forward2Piece = board.checkPiece(row + 2 * forward, col);
                    if (forward2Piece.isEmpty()) {
                        possibleMoves.add(new ChessMove(myPosition, row + 2 * forward, col));
                    }
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // diagonal captures
        try {
            final var diagLeftPiece = board.checkPiece(row + forward, col + left);
            if (diagLeftPiece.isPresent() && diagLeftPiece.get().getTeamColor() != pieceColor) {
                // if it is a capture on last line then it can promote
                if (row + forward == 1 || row + forward == ChessBoard.HEIGHT) {
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + left, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + left, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + left, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + left, ChessPiece.PieceType.KNIGHT));
                } 
                // otherwise it is a normal move
                else {
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + left));
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        try {
            final var diagRightPiece = board.checkPiece(row + forward, col + right);
            if (diagRightPiece.isPresent() && diagRightPiece.get().getTeamColor() != pieceColor) {
                if (row + forward == 1 || row + forward == ChessBoard.HEIGHT) {
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + right, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + right, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + right, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + right, ChessPiece.PieceType.KNIGHT));
                } else {
                    possibleMoves.add(new ChessMove(myPosition, row + forward, col + right));
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        return possibleMoves;
    }
}
