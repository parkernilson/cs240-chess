package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator extends PieceMovesCalculator {
    
    private final int BLACK_PAWN_HOME_ROW = 7;
    private final int WHITE_PAWN_HOME_ROW = 2;
    
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
            final var forwardPiece = board.checkPiece(row + FORWARD, col);
            if (forwardPiece.isEmpty()) {
                // if the pawn is moving to the last row (1 for black, 8 for white), then it can promote
                if (row + FORWARD == 1 || row + FORWARD == ChessBoard.HEIGHT) {
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col, ChessPiece.PieceType.KNIGHT));
                } 
                // otherwise it is a normal non-promoting move
                else {
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col));
                }

                // can move forward two spaces if it is the first move and both spaces are empty
                if (
                    (pieceColor == ChessGame.TeamColor.BLACK && row == BLACK_PAWN_HOME_ROW)
                    || (pieceColor == ChessGame.TeamColor.WHITE && row == WHITE_PAWN_HOME_ROW)
                ) {
                    final var forward2Piece = board.checkPiece(row + 2 * FORWARD, col);
                    if (forward2Piece.isEmpty()) {
                        possibleMoves.add(new ChessMove(myPosition, row + 2 * FORWARD, col));
                    }
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // diagonal captures
        try {
            final var diagLeftPiece = board.checkPiece(row + FORWARD, col + LEFT);
            if (diagLeftPiece.isPresent() && diagLeftPiece.get().getTeamColor() != pieceColor) {
                // if it is a capture on last line then it can promote
                if (row + FORWARD == 1 || row + FORWARD == ChessBoard.HEIGHT) {
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + LEFT, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + LEFT, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + LEFT, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + LEFT, ChessPiece.PieceType.KNIGHT));
                } 
                // otherwise it is a normal move
                else {
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + LEFT));
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        try {
            final var diagRightPiece = board.checkPiece(row + FORWARD, col + RIGHT);
            if (diagRightPiece.isPresent() && diagRightPiece.get().getTeamColor() != pieceColor) {
                if (row + FORWARD == 1 || row + FORWARD == ChessBoard.HEIGHT) {
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + RIGHT, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + RIGHT, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + RIGHT, ChessPiece.PieceType.ROOK));
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + RIGHT, ChessPiece.PieceType.KNIGHT));
                } else {
                    possibleMoves.add(new ChessMove(myPosition, row + FORWARD, col + RIGHT));
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        return possibleMoves;
    }
}
