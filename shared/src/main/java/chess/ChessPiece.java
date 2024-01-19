package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    private final int FORWARD;
    private final int BACKWARD;
    private final int RIGHT;
    private final int LEFT;

    static int BLACK_PAWN_HOME_ROW = 7;
    static int WHITE_PAWN_HOME_ROW = 2;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        // if black, then forward is -1, if white, then forward is +1 (and backward is
        // opposite)
        FORWARD = pieceColor == ChessGame.TeamColor.BLACK ? -1 : 1;
        BACKWARD = -FORWARD;
        // if black, then right is +1, if white, then right is -1 (and left is opposite)
        RIGHT = pieceColor == ChessGame.TeamColor.BLACK ? -1 : 1;
        LEFT = -RIGHT;
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
                PawnMovesCalculator pawnCalc = new PawnMovesCalculator(pieceColor);
                return pawnCalc.pieceMoves(board, myPosition);
            case ROOK:
                RookMovesCalculator rookCalc = new RookMovesCalculator(pieceColor);
                return rookCalc.pieceMoves(board, myPosition);
            case KNIGHT:
                KnightMovesCalculator knightCalc = new KnightMovesCalculator(pieceColor);
                return knightCalc.pieceMoves(board, myPosition);
            case BISHOP:
                BishopMovesCalculator bishopCalc = new BishopMovesCalculator(pieceColor);
                return bishopCalc.pieceMoves(board, myPosition);
            case QUEEN:
                throw new RuntimeException("Not implemented");
            case KING:
                throw new RuntimeException("Not implemented");
            default:
                throw new RuntimeException(String.format("Unsupported piece type: %s", type));
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pieceColor == null) ? 0 : pieceColor.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ChessPiece other = (ChessPiece) obj;
        if (pieceColor != other.pieceColor)
            return false;
        if (type != other.type)
            return false;
        return true;
    }
}
