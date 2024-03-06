package dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import chess.ChessGame;
import model.GameData;

public class SQLGameDAO extends SQLDAO implements GameDAO {

    public static final String GAMES_TABLE = "games";
    public static final String PARTICIPANTS_TABLE = "participants";

    static private final String[] createStatements = {
            String.format("""
                    CREATE TABLE IF NOT EXISTS %s (
                        `game_id` varchar(256) NOT NULL,
                        `white_username` varchar(256) DEFAULT NULL,
                        `black_username` varchar(256) DEFAULT NULL,
                        `game_name` varchar(256) NOT NULL,
                        `game_state` varchar(256) NOT NULL,
                        PRIMARY KEY (`game_id`),
                        INDEX(game_name)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """, GAMES_TABLE),
            String.format("""
                    CREATE TABLE IF NOT EXISTS %s (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `game_id` varchar(256) NOT NULL,
                        `username` varchar(256) NOT NULL,
                        PRIMARY KEY (`id`),
                        INDEX(username),
                        INDEX(game_id)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """, PARTICIPANTS_TABLE)
    };

    public SQLGameDAO() throws ResponseException, DataAccessException {
        configureDatabase(createStatements);
    }

    public GameData getGame(int gameId) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT id, json FROM %s WHERE game_id=?", GAMES_TABLE);
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public ArrayList<GameData> listGames(String username) throws ResponseException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT * FROM %s", GAMES_TABLE);
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public GameData createGame(GameData game) throws ResponseException, DataAccessException {
        var statement = String.format(
                "INSERT INTO %s (game_id, game_name, white_username, black_username, game_state) VALUES (?, ?, ?, ?, ?)",
                GAMES_TABLE);
        var gameState = new Gson().toJson(game.game());
        executeUpdate(statement, game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername(), gameState);
        return new GameData(
                game.gameID(),
                game.whiteUsername(),
                game.blackUsername(),
                game.gameName(),
                game.game()
        );
    }

    public GameData updateGame(GameData game) throws DataAccessException, ResponseException {
        var statement = String.format(
                "UPDATE %s SET game_name=?, white_username=?, black_username=?, game_state=? WHERE game_id=?",
                GAMES_TABLE);
        var gameState = new Gson().toJson(game.game());
        executeUpdate(statement, game.gameName(), game.whiteUsername(), game.blackUsername(), gameState, game.gameID());
        return new GameData(
                game.gameID(),
                game.gameName(),
                game.whiteUsername(),
                game.blackUsername(),
                game.game()
        );
    }

    public void deleteGame(int gameId) throws ResponseException, DataAccessException {
        var statement = String.format("DELETE FROM %s WHERE game_id=?", GAMES_TABLE);
        executeUpdate(statement, gameId);
    }

    public void deleteAllGames() throws ResponseException, DataAccessException {
        var statement = String.format("TRUNCATE %s", GAMES_TABLE);
        executeUpdate(statement);
    }

    public int getMaxGameId() throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT MAX(game_id) FROM %s", GAMES_TABLE);
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }

        throw new UnsupportedOperationException("Not implemented yet");
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameId = rs.getInt("game_id");
        var gameName = rs.getString("game_name");
        var whiteUsername = rs.getString("white_username");
        var blackUsername = rs.getString("black_username");
        var gameState = rs.getString("game_state");
        var chessGameState = new Gson().fromJson(gameState, ChessGame.class);
        return new GameData(
                gameId,
                gameName,
                whiteUsername,
                blackUsername,
                chessGameState);
    }

}
