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
        throw new RuntimeException("Not implemented");
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
        throw new RuntimeException("Not implemented");
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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
