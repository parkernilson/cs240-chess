package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turnTeam = TeamColor.WHITE;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        final var piece = board.getPiece(startPosition);

        // if there is no piece at the given position, no valid moves
        if (piece == null) return null;

        // if it is not the piece's turn, it has no valid moves
        if (piece.getTeamColor() != turnTeam) return null;

        final var possibleMoves = piece.pieceMoves(board, startPosition);

        // for each move, if it leaves the king in check, remove it from the list
        for (final var possibleMove : possibleMoves) {
            if (!moveCausesCheck(possibleMove)) {
                possibleMoves.remove(possibleMove);
            }
        }

        return possibleMoves;
    }

    /**
     * Test a move to see if it causes check (without modifying the board state)
     * @param move - The move to check
     * @return True if the move would cause check if executed. False otherwise
     */
    public boolean moveCausesCheck(ChessMove move) {
        final var startPiece = board.getPiece(move.getStartPosition());
        final var endPiece = board.getPiece(move.getEndPosition());
        // make the move
        board.addPiece(move.getStartPosition(), null);
        board.addPiece(move.getEndPosition(), startPiece);

        if (isInCheck(turnTeam)) {
            return true;
        }

        // reverse the move
        board.addPiece(move.getStartPosition(), startPiece);
        board.addPiece(move.getEndPosition(), endPiece);

        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // if the move is in the set of valid moves at the given position, make the move

        // once the move is made, set the turn to the other team (?)

        throw new RuntimeException("Not implemented");
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition positionOfKing = null;
        for (int row = 0; row <= ChessBoard.HEIGHT; ++row) {
            for (int col = 0; col <= ChessBoard.WIDTH; ++col) {
                final var curPosition = new ChessPosition(row, col);
                final var curPiece = board.getPiece(curPosition);
                if (curPiece.getTeamColor() == teamColor && curPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    positionOfKing = curPosition;
                    break;
                } 
            }
        }

        return positionOfKing;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // find the king of the given team (maybe keep a reference?)
        final var kingPosition = getKingPosition(teamColor);

        // check all possible angles of attack for the other team's pieces
        // Bishop: go in diagonal directions
        // Rook: go in horizontal and vertical directions
        // Queen: (in diagonal and straight directions)
        // King: (in all directions, one space)
        // Pawn: at the front left and front right positions
        // Knight: in the L positions (don't care about blocking pieces)

        // if a piece is found that can attack the king, return true

        // otherwise, return false
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) return false;

        final var kingPosition = getKingPosition(teamColor);

        // check if any pieces can move to block the king (or capture the offending piece)
        for (int row = 1; row <= ChessBoard.HEIGHT; ++row) {
            for (int col = 1; col <= ChessBoard.WIDTH; ++col) {
                // check if any of the possible moves this piece can make will stop the checkmate
                final var curPosition = new ChessPosition(row, col);
                final var curPiece = board.getPiece(curPosition);

                if (curPiece.getTeamColor() != teamColor) continue;

                final var curValidMoves = validMoves(curPosition);

                for (ChessMove move : curValidMoves) {
                    if (!moveCausesCheck(move)) return false;
                }
            }
        }

        // check if the king can move to avoid check
        final var kingValidMoves = validMoves(kingPosition);

        // for each move, test the move and check if the king is still in check
        for (var move : kingValidMoves) {
            // if there is a move that removes the king from check, then it is not checkmate
            if (!moveCausesCheck(move)) return false;
        }

        // if there is no case where the king can avoid check, then it is checkmate
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // check every position that is the team color if any piece can make a valid move then return false
        for (int row = 1; row <= ChessBoard.HEIGHT; ++row) {
            for (int col = 1; col <= ChessBoard.WIDTH; ++col) {
                final var curPosition = new ChessPosition(row, col);
                final var curPiece = board.getPiece(curPosition);

                // only consider pieces of the specified color
                if (curPiece.getTeamColor() != teamColor) continue;

                final var validMovesAtRowCol = validMoves(curPosition);
                if (validMovesAtRowCol != null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
