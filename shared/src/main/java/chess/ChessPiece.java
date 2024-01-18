package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    private static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {

        throw new RuntimeException("Not implemented");
    }

    private static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {

        throw new RuntimeException("Not implemented");
    }

    private static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {

        throw new RuntimeException("Not implemented");
    }

    private static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {

        throw new RuntimeException("Not implemented");
    }

    private static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {

        throw new RuntimeException("Not implemented");
    }

    private static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {

        throw new RuntimeException("Not implemented");
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // based on which type this piece is, call the appropriate helper method
        // return the result of the helper method
        switch (type) {
            case PAWN:
                return pawnMoves(board, myPosition);
            case ROOK:
                return rookMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case KING:
                return kingMoves(board, myPosition);
            default:
                throw new RuntimeException(String.format("Unsupported piece type: %s", type));
        }
    }
}
