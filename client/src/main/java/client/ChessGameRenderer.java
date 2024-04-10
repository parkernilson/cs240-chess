package client;

import static ui.EscapeSequences.BG_GREEN;
import static ui.EscapeSequences.BG_LIGHT_GREY;
import static ui.EscapeSequences.BG_WHITE;
import static ui.EscapeSequences.BG_YELLOW;
import static ui.EscapeSequences.BLUE;
import static ui.EscapeSequences.EMPTY;
import static ui.EscapeSequences.RESET_ALL;
import static ui.EscapeSequences.WHITE;

import java.util.Collection;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessGame.TeamColor;
import chess.ChessPosition;
import chess.PositionOutOfBoundsException;

public class ChessGameRenderer {
    public static String renderGame(ChessGame game) { 
        return renderGame(game, null);
    }

    public static String renderGame(ChessGame game, Collection<ChessPosition> highlightMoves) {
        final var board = game.getBoard();
        return renderBoard(board, ChessGame.TeamColor.WHITE, highlightMoves)
        + "\n" + renderBoard(board, ChessGame.TeamColor.BLACK, highlightMoves);
    }

    public static String renderBoard(ChessBoard board, ChessGame.TeamColor perspective) {
        return renderBoard(board, perspective, null);
    }

    public static String renderBoard(ChessBoard board, ChessGame.TeamColor perspective, Collection<ChessPosition> highlightMoves) {
        String[][] pieces = new String[10][10];
        pieces[0][0] = EMPTY;
        pieces[9][9] = EMPTY;
        pieces[0][9] = EMPTY;
        pieces[9][0] = EMPTY;
        for (int i = 1; i < 9; i++) {
            pieces[0][i] = " " + (char) ('a' + i - 1) + " ";
            pieces[9][i] = " " + (char) ('a' + i - 1) + " ";
            pieces[9-i][0] = " " + (char) ('8' - i + 1) + " ";
            pieces[9-i][9] = " " + (char) ('8' - i + 1) + " ";
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

        final int startI = perspective == ChessGame.TeamColor.WHITE ? 9 : 0;
        final int startJ = perspective == ChessGame.TeamColor.WHITE ? 0 : 9;
        final int deltaI = perspective == ChessGame.TeamColor.WHITE ? -1 : 1;
        final int deltaJ = perspective == ChessGame.TeamColor.WHITE ? 1 : -1;
        for (int i = startI; i >= 0 && i < 10; i += deltaI) {
            for (int j = startJ; j >= 0 && j < 10; j += deltaJ) {
                TeamColor spaceColor = (i + j + 1) % 2 == 0 ? TeamColor.WHITE : TeamColor.BLACK;
                var bg = spaceColor == TeamColor.WHITE ? BG_WHITE : BG_LIGHT_GREY;
                try {
                    var position = board.getNewPosition(i, j);
                    if (highlightMoves != null && highlightMoves.contains(position)) {
                        bg = spaceColor == TeamColor.WHITE ? BG_YELLOW : BG_GREEN;
                    }
                } catch(PositionOutOfBoundsException e) {}
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
