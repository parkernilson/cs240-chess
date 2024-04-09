package chess;

import java.util.Objects;
/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    int row;
    int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return a chess position from a string formatted: [a-h][1-8]
     */
    public static ChessPosition parse(String chessPositionString) {
        if (chessPositionString.length() != 2) {
            throw new IllegalArgumentException("Invalid position string");
        }
        int col = chessPositionString.charAt(0) - 'a' + 1;
        int row = chessPositionString.charAt(1) - '0';
        return new ChessPosition(row, col);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public String toString() {
        return String.format("%c%d", (char) ('a' + col - 1), row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChessPosition other = (ChessPosition) obj;
        return row == other.row && col == other.col;
    }
}
