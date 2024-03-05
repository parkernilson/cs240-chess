package dataAccess;

import java.sql.SQLException;

import model.GameData;

public class SQLGameDAO implements GameDAO {

    SQLGameDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    public GameData getGame(int gameId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public GameData[] listGames(String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public GameData createGame(GameData game) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public GameData updateGame(GameData game) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteGame(int gameId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteAllGames() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public int getMaxGameId() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `game_id` varchar(256) NOT NULL,
              `white_username` varchar(256) DEFAULT NULL,
              `black_username` varchar(256) DEFAULT NULL,
              `game_name` varchar(256) NOT NULL,
              `game_state` varchar(256) NOT NULL,
              PRIMARY KEY (`game_id`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS participants (
              `id` int NOT NULL AUTO_INCREMENT,
              `game_id` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
              INDEX(game_id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }


}
