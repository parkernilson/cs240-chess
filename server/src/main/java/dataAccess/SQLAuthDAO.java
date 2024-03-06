package dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.AuthData;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {

    private static final String AUTH_TABLE = "auth";

    static private final String[] createStatements = {
            String.format("""
                    CREATE TABLE IF NOT EXISTS %s (
                        `username` varchar(256) NOT NULL,
                        `auth_token` varchar(256) NOT NULL,
                        PRIMARY KEY (`auth_token`),
                        INDEX(username)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """, AUTH_TABLE)
    };

    public SQLAuthDAO() throws ResponseException, DataAccessException {
        configureDatabase(createStatements);
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT auth_token, username FROM %s WHERE auth_token=?", AUTH_TABLE);
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public AuthData getAuthByUsername(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT auth_token, username FROM %s WHERE username=?", AUTH_TABLE);
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    /**
     * Create an auth for the given user
     * 
     * @throws DataAccessException
     * @throws ResponseException
     */
    public AuthData createAuth(String username, String authToken) throws ResponseException, DataAccessException {
        if (!(username instanceof String && authToken instanceof String)) {
            throw new DataAccessException("Invalid input");
        }
        var statement = String.format("INSERT INTO %s (auth_token, username) VALUES (?, ?)", AUTH_TABLE);
        executeUpdate(statement, authToken, username);
        return new AuthData(authToken, username);
    }

    public void deleteAuth(String authToken) throws ResponseException, DataAccessException {
        var statement = String.format("DELETE FROM %s WHERE auth_token=?", AUTH_TABLE);
        executeUpdate(statement, authToken);
    }

    public void deleteAllAuth() throws ResponseException, DataAccessException {
        var statement = String.format("TRUNCATE %s", AUTH_TABLE);
        executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var authToken = rs.getString("auth_token");
        return new AuthData(authToken, username);
    }

}
