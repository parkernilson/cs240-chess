package chess;

import java.util.HashMap;
import java.util.Optional;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    static int WIDTH = 8;
    static int HEIGHT = 8;

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

    static boolean inBounds(ChessPosition position) {
        return position.getRow() >= 1 
            && position.getRow() <= HEIGHT 
            && position.getColumn() >= 1
            && position.getColumn() <= WIDTH;
    }

    public ChessPosition getNewPosition(int row, int col) throws PositionOutOfBoundsException {
        final ChessPosition position = new ChessPosition(row, col);
        if (inBounds(position)) {
            return position;
        } else {
            throw new PositionOutOfBoundsException();
        }
    }

    public Optional<ChessPiece> checkPiece(ChessPosition position) throws PositionOutOfBoundsException {
        if (inBounds(position)) {
            return Optional.ofNullable(getPiece(position));
        } else {
            throw new PositionOutOfBoundsException();
        }
    }

    public Optional<ChessPiece> checkPiece(int row, int col) throws PositionOutOfBoundsException {
        final ChessPosition position = new ChessPosition(row, col);
        return checkPiece(position);
    }

    public static int getForward(ChessGame.TeamColor pieceColor) {
        return pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
    }

    public static int getBackward(ChessGame.TeamColor pieceColor) {
        return -getForward(pieceColor);
    }

    public static int getRight(ChessGame.TeamColor pieceColor) {
        return pieceColor == ChessGame.TeamColor.WHITE ? 1 : -1;
    }

    public static int getLeft(ChessGame.TeamColor pieceColor) {
        return -getRight(pieceColor);
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

    public void removePiece(ChessPosition position) {
        if (board.get(position) != null) {
            board.remove(position);
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new HashMap<ChessPosition, ChessPiece>();
        board.put(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        board.put(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        board.put(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        board.put(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        board.put(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        board.put(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        board.put(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        board.put(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        for (int col = 1; col <= ChessBoard.WIDTH; col++) {
            board.put(new ChessPosition(2, col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        for (int col = 1; col <= ChessBoard.WIDTH; col++) {
            board.put(new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        board.put(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        board.put(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        board.put(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        board.put(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        board.put(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        board.put(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        board.put(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        board.put(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((board == null) ? 0 : board.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessBoard other = (ChessBoard) obj;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = HEIGHT; row >= 1; row--) {
            for (int col = 1; col <= WIDTH; col++) {
                try {
                    ChessPosition position = getNewPosition(row, col);
                    Optional<ChessPiece> optionalPiece = checkPiece(position);
                    if (optionalPiece.isPresent()) {
                        ChessPiece piece = optionalPiece.get();
                        sb.append(piece.getSymbol()).append(" ");
                    } else {
                        sb.append("- ");
                    }
                } catch(PositionOutOfBoundsException e) {
                    sb.append("- ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}