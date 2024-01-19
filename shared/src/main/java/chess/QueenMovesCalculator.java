package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator extends PieceMovesCalculator {
    
    public QueenMovesCalculator(ChessGame.TeamColor pieceColor) {
        super(pieceColor);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        final int row = myPosition.getRow();
        final int col = myPosition.getColumn();

        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        // forward moves
        try {
            for (int i = row + FORWARD; i >= 1 && i <= ChessBoard.HEIGHT; i += FORWARD) {
                final var forwardPiece = board.checkPiece(i, col);
                if (forwardPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, col));
                } else if (forwardPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, col));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // forward right moves
        try {
            for (int i = row + FORWARD, j = col + RIGHT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += FORWARD, j += RIGHT) {
                final var forwardRightPiece = board.checkPiece(i, j);
                if (forwardRightPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (forwardRightPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // right moves
        try {
            for (int j = col + RIGHT; j >= 1 && j <= ChessBoard.WIDTH; j += RIGHT) {
                final var rightPiece = board.checkPiece(row, j);
                if (rightPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, row, j));
                } else if (rightPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, row, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward right moves
        try {
            for (int i = row + BACKWARD, j = col + RIGHT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += BACKWARD, j += RIGHT) {
                final var backwardRightPiece = board.checkPiece(i, j);
                if (backwardRightPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (backwardRightPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // backward moves
        try {
            for (int i = row + BACKWARD; i >= 1 && i <= ChessBoard.HEIGHT; i += BACKWARD) {
                final var backwardPiece = board.checkPiece(i, col);
                if (backwardPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, col));
                } else if (backwardPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, col));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}
        
        // backward left moves
        try {
            for (int i = row + BACKWARD, j = col + LEFT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += BACKWARD, j += LEFT) {
                final var backwardLeftPiece = board.checkPiece(i, j);
                if (backwardLeftPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (backwardLeftPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // left moves
        try {
            for (int j = col + LEFT; j >= 1 && j <= ChessBoard.WIDTH; j += LEFT) {
                final var leftPiece = board.checkPiece(row, j);
                if (leftPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, row, j));
                } else if (leftPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, row, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        // forward left moves
        try {
            for (int i = row + FORWARD, j = col + LEFT; i >= 1 && i <= ChessBoard.HEIGHT && j >= 1 && j <= ChessBoard.WIDTH; i += FORWARD, j += LEFT) {
                final var forwardLeftPiece = board.checkPiece(i, j);
                if (forwardLeftPiece.isEmpty()) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                } else if (forwardLeftPiece.get().getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, i, j));
                    break;
                } else {
                    break;
                }
            }
        } catch(PositionOutOfBoundsException e) {}

        return possibleMoves;
    }
}
