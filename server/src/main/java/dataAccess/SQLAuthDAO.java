package dataAccess;

import java.sql.SQLException;

import model.AuthData;
import util.TokenGenerator;

public class SQLAuthDAO implements AuthDAO {

    SQLAuthDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    public AuthData getAuth(String authToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public AuthData getAuthByUsername(String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Create an auth for the given user
     */
    public AuthData createAuth(String username) {
        String authToken = TokenGenerator.generateToken();
        AuthData auth = new AuthData(authToken, username);
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteAuth(String authToken) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteAllAuth() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `username` varchar(256) NOT NULL,
              `auth_token` varchar(256) NOT NULL,
              PRIMARY KEY (`auth_token`),
              INDEX(username)
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
