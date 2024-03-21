package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.PositionOutOfBoundsException;

import static ui.EscapeSequences.*;

public class ChessGameRenderer {
    public static String renderGame(ChessGame game) { 
        final var board = game.getBoard();
        return renderBoard(board, ChessGame.TeamColor.WHITE)
        + "\n" + renderBoard(board, ChessGame.TeamColor.BLACK);
    }

    public static String renderBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        String[][] pieces = new String[10][10];
        pieces[0][0] = EMPTY;
        pieces[9][9] = EMPTY;
        pieces[0][9] = EMPTY;
        pieces[9][0] = EMPTY;
        for (int i = 1; i < 9; i++) {
            pieces[0][i] = " " + (char) ('a' + i - 1) + " ";
            pieces[9][i] = " " + (char) ('a' + i - 1) + " ";
            pieces[i][0] = " " + (char) ('8' - i + 1) + " ";
            pieces[i][9] = " " + (char) ('8' - i + 1) + " ";
        }

        // fill the board with pieces
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                try {
                    var position = board.getNewPosition(i, j);
                    var piece = board.getPiece(position);
                    if (piece != null) {
                        final var color = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE : BLUE;
                        pieces[i][j] = color + " " + piece.getSymbol() + " ";
                    } else {
                        pieces[i][j] = EMPTY;
                    }
                } catch (PositionOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        final int startI = perspective == ChessGame.TeamColor.WHITE ? 0 : 9;
        final int startJ = perspective == ChessGame.TeamColor.WHITE ? 0 : 9;
        final int delta = perspective == ChessGame.TeamColor.WHITE ? 1 : -1;
        for (int i = startI; i >= 0 && i < 10; i += delta) {
            for (int j = startJ; j >= 0 && j < 10; j += delta) {
                var bg = (i + j) % 2 == 0 ? BG_WHITE : BG_LIGHT_GREY;
                if (i == 0 || i == 9 || j == 0 || j == 9) {
                    bg = "";
                }
                sb.append(bg + pieces[i][j] + RESET_ALL);
            }
            sb.append("\n");
        }

        sb.append(RESET_ALL);
        return sb.toString();
    }
}
