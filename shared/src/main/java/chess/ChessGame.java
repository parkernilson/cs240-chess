package chess;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
     *         startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        final var piece = board.getPiece(startPosition);

        // if there is no piece at the given position, no valid moves
        if (piece == null)
            return null;

        final var pieceColor = piece.getTeamColor();

        final var possibleMoves = piece.pieceMoves(board, startPosition);
        var validMoves = new HashSet<ChessMove>();

        // for each move, if it leaves the king in check, remove it from the list
        for (final var possibleMove : possibleMoves) {
            if (!moveCausesCheck(possibleMove, pieceColor)) {
                validMoves.add(possibleMove);
            }
        }

        return validMoves;
    }

    /**
     * Test a move to see if it causes check (without modifying the board state)
     * 
     * @param move      - The move to check
     * @param teamColor - checks if the move causes check against this team color
     * @return True if the move would cause check if executed. False otherwise
     */
    public boolean moveCausesCheck(ChessMove move, TeamColor teamColor) {
        final var startPiece = board.getPiece(move.getStartPosition());
        final var endPiece = board.getPiece(move.getEndPosition());
        // make the move
        board.removePiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), startPiece);

        if (isInCheck(teamColor)) {
            // reverse the move
            board.addPiece(move.getStartPosition(), startPiece);
            // if the end position was not occupied we actually need to remove the piece
            // (instead of placing null there)
            if (endPiece != null)
                board.addPiece(move.getEndPosition(), endPiece);
            if (endPiece == null)
                board.removePiece(move.getEndPosition());
            return true;
        }

        // reverse the move
        board.addPiece(move.getStartPosition(), startPiece);
        // if the end position was not occupied we actually need to remove the piece
        // (instead of placing null there)
        if (endPiece != null)
            board.addPiece(move.getEndPosition(), endPiece);
        if (endPiece == null)
            board.removePiece(move.getEndPosition());

        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        final var piece = board.getPiece(move.getStartPosition());
        if (piece == null || piece.getTeamColor() != turnTeam)
            throw new InvalidMoveException();

        final var validMoves = validMoves(move.getStartPosition());

        if (validMoves == null)
            throw new InvalidMoveException();

        // if the move is in the set of valid moves at the given position, make the move
        if (validMoves.contains(move)) {
            // make the move
            final var promotionPieceType = move.getPromotionPiece();
            board.removePiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(),
                    promotionPieceType != null ? new ChessPiece(piece.getTeamColor(), promotionPieceType) : piece);

            // alternate the team color
            setTeamTurn(turnTeam == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
        } else {
            throw new InvalidMoveException();
        }
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPosition positionOfKing = null;
        for (int row = 1; row <= ChessBoard.HEIGHT; row++) {
            for (int col = 1; col <= ChessBoard.WIDTH; col++) {
                final var curPosition = new ChessPosition(row, col);
                final var curPiece = board.getPiece(curPosition);
                if (curPiece != null && curPiece.getTeamColor() == teamColor
                        && curPiece.getPieceType() == ChessPiece.PieceType.KING) {
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

        // if there is no king, it is not possible to be in check
        if (kingPosition == null)
            return false;

        final var kingRow = kingPosition.getRow();
        final var kingCol = kingPosition.getColumn();

        final int FORWARD = ChessBoard.getForward(teamColor);
        final int BACKWARD = ChessBoard.getBackward(teamColor);
        final int RIGHT = ChessBoard.getRight(teamColor);
        final int LEFT = ChessBoard.getLeft(teamColor);

        // check all possible angles of attack for the other team's pieces
        // Bishop: go in diagonal directions
        // Rook: go in horizontal and vertical directions
        // Queen: (in diagonal and straight directions)
        // King: (in all directions, one space)
        // Pawn: at the front left and front right positions
        // Knight: in the L positions (don't care about blocking pieces)

        final var surroundingPositions = new ArrayList<ChessPosition>();
        final int[][] surroundingCoords = {
                { kingRow - 1, kingCol },
                { kingRow - 1, kingCol + 1 },
                { kingRow, kingCol + 1 },
                { kingRow + 1, kingCol + 1 },
                { kingRow + 1, kingCol },
                { kingRow + 1, kingCol - 1 },
                { kingRow, kingCol - 1 },
                { kingRow - 1, kingCol - 1 }
        };

        for (int[] coord : surroundingCoords) {
            try {
                final var surroundingPosition = board.getNewPosition(coord[0], coord[1]);
                surroundingPositions.add(surroundingPosition);
            } catch (PositionOutOfBoundsException e) {
            }
        }

        // check for kings in the surrounding positions
        for (ChessPosition surroundingPosition : surroundingPositions) {
            final var surroundingPiece = board.getPiece(surroundingPosition);
            if (surroundingPiece != null && surroundingPiece.getTeamColor() != teamColor
                    && surroundingPiece.getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }

        // check for pawns in the front left and front right positions
        try {
            final var frontLeft = board.getNewPosition(kingRow + FORWARD, kingCol + LEFT);
            final var frontLeftPiece = board.getPiece(frontLeft);
            if (frontLeftPiece != null && frontLeftPiece.getTeamColor() != teamColor
                    && frontLeftPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                return true;
            }
        } catch (PositionOutOfBoundsException e) {
        }
        try {
            final var frontRight = board.getNewPosition(kingRow + FORWARD, kingCol + RIGHT);
            final var frontRightPiece = board.getPiece(frontRight);
            if (frontRightPiece != null && frontRightPiece.getTeamColor() != teamColor
                    && frontRightPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                return true;
            }
        } catch (PositionOutOfBoundsException e) {
        }

        final var q = new ArrayDeque<ChessPosition>(surroundingPositions);

        while (!q.isEmpty()) {
            final var curPosition = q.remove();
            final int ydiff = curPosition.getRow() - kingRow;
            final int ydir = ydiff < 0 ? -1 : ydiff == 0 ? 0 : 1;
            final int xdiff = curPosition.getColumn() - kingCol;
            final int xdir = xdiff < 0 ? -1 : xdiff == 0 ? 0 : 1;
            final var curPiece = board.getPiece(curPosition);
            if (curPiece != null) {
                // if we encounter a piece of the team color, then this direction is blocked so
                // the king is safe
                if (curPiece.getTeamColor() != teamColor) {
                    // if the direction is diagonal, check for bishops and queens
                    if (ydir == 0 || xdir == 0) {
                        if (curPiece.getPieceType() == ChessPiece.PieceType.ROOK
                                || curPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                            return true;
                        }
                    }
                    // if the direction is horizontal or vertical, check for rooks and queens
                    else if (ydir != 0 && xdir != 0) {
                        if (curPiece.getPieceType() == ChessPiece.PieceType.BISHOP
                                || curPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                            return true;
                        }
                    }
                }
            } else {
                // if there is no piece in the path, keep traveling in the same direction
                try {
                    final var nextPosition = board.getNewPosition(curPosition.getRow() + ydir,
                            curPosition.getColumn() + xdir);
                    q.add(nextPosition);
                } catch (PositionOutOfBoundsException e) {
                }
            }
        }

        // check for knights
        final var knightPositions = new ArrayList<ChessPosition>();
        final int[][] knightCoords = {
                { kingRow - 2, kingCol - 1 },
                { kingRow - 2, kingCol + 1 },
                { kingRow - 1, kingCol - 2 },
                { kingRow - 1, kingCol + 2 },
                { kingRow + 1, kingCol - 2 },
                { kingRow + 1, kingCol + 2 },
                { kingRow + 2, kingCol - 1 },
                { kingRow + 2, kingCol + 1 }
        };

        for (int[] coord : knightCoords) {
            try {
                final var knightPosition = board.getNewPosition(coord[0], coord[1]);
                knightPositions.add(knightPosition);
            } catch (PositionOutOfBoundsException e) {
            }
        }

        for (ChessPosition knightPosition : knightPositions) {
            final var knightPiece = board.getPiece(knightPosition);
            if (knightPiece != null && knightPiece.getTeamColor() != teamColor
                    && knightPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor))
            return false;

        final var kingPosition = getKingPosition(teamColor);

        // check if any pieces can move to block the king (or capture the offending
        // piece)
        for (int row = 1; row <= ChessBoard.HEIGHT; ++row) {
            for (int col = 1; col <= ChessBoard.WIDTH; ++col) {
                // check if any of the possible moves this piece can make will stop the
                // checkmate
                final var curPosition = new ChessPosition(row, col);
                final var curPiece = board.getPiece(curPosition);

                if (curPiece != null && curPiece.getTeamColor() != teamColor)
                    continue;

                final var curValidMoves = validMoves(curPosition);

                if (curValidMoves == null)
                    continue;

                for (ChessMove move : curValidMoves) {
                    if (!moveCausesCheck(move, teamColor))
                        return false;
                }
            }
        }

        // check if the king can move to avoid check
        final var kingValidMoves = validMoves(kingPosition);

        // for each move, test the move and check if the king is still in check
        for (var move : kingValidMoves) {
            // if there is a move that removes the king from check, then it is not checkmate
            if (!moveCausesCheck(move, teamColor))
                return false;
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

        // check every position that is the team color if any piece can make a valid
        // move then return false
        for (int row = 1; row <= ChessBoard.HEIGHT; ++row) {
            for (int col = 1; col <= ChessBoard.WIDTH; ++col) {
                final var curPosition = new ChessPosition(row, col);
                final var curPiece = board.getPiece(curPosition);

                // only consider pieces of the specified color
                if (curPiece != null && curPiece.getTeamColor() != teamColor)
                    continue;

                final var validMovesAtRowCol = validMoves(curPosition);
                if (validMovesAtRowCol != null && !validMovesAtRowCol.isEmpty()) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((turnTeam == null) ? 0 : turnTeam.hashCode());
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
        ChessGame other = (ChessGame) obj;
        if (turnTeam != other.turnTeam)
            return false;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        return true;
    }

    
}
