package chess;

import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    /** Represents the pieces of this chess board. */
    HashMap<ChessPosition, ChessPiece> board;

    public ChessBoard() {
        board = new HashMap<ChessPosition, ChessPiece>();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // TODO: set up the board with the default positions
        board = new HashMap<ChessPosition, ChessPiece>();
    }
}
