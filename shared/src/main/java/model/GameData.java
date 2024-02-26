package model;

import chess.ChessGame;

public record GameData(
        int gameId,
        String whiteUsername,
        String backUsername,
        String gameName,
        ChessGame game) {
}
