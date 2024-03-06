package util;

import java.util.ArrayList;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.PositionOutOfBoundsException;

public class PiecePositions {
    public static ArrayList<ChessPosition> getSurroundingPositions(int row, int col, ChessBoard board) {
        final var surroundingPositions = new ArrayList<ChessPosition>();
        final int[][] surroundingCoords = {
                { row - 1, col },
                { row - 1, col + 1 },
                { row, col + 1 },
                { row + 1, col + 1 },
                { row + 1, col },
                { row + 1, col - 1 },
                { row, col - 1 },
                { row - 1, col - 1 }
        };

        for (int[] coord : surroundingCoords) {
            try {
                final var surroundingPosition = board.getNewPosition(coord[0], coord[1]);
                surroundingPositions.add(surroundingPosition);
            } catch (PositionOutOfBoundsException e) {
            }
        }

        return surroundingPositions;
    }

    public static ArrayList<ChessPosition> getSurroundingKnightPositions(int row, int col, ChessBoard board) {
        // check for knights
        final var knightPositions = new ArrayList<ChessPosition>();
        final int[][] knightCoords = {
                { row - 2, col - 1 },
                { row - 2, col + 1 },
                { row - 1, col - 2 },
                { row - 1, col + 2 },
                { row + 1, col - 2 },
                { row + 1, col + 2 },
                { row + 2, col - 1 },
                { row + 2, col + 1 }
        };

        for (int[] coord : knightCoords) {
            try {
                final var knightPosition = board.getNewPosition(coord[0], coord[1]);
                knightPositions.add(knightPosition);
            } catch (PositionOutOfBoundsException e) {
            }
        }
        return knightPositions;
    }
}
