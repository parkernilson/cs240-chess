package dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;

import exceptions.ResponseException;
import model.UserData;

public class SQLUserDAO extends SQLDAO implements UserDAO {

    public static final String USERS_TABLE = "users";

    static private final String[] createStatements = {
            String.format("""
                    CREATE TABLE IF NOT EXISTS %s (
                      `id` int NOT NULL AUTO_INCREMENT,
                      `username` varchar(256) NOT NULL,
                      `email` varchar(256) NOT NULL,
                      `password` varchar(256) NOT NULL,
                      PRIMARY KEY (`id`),
                      INDEX(username)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                    """, USERS_TABLE)
    };

    public SQLUserDAO() throws ResponseException, DataAccessException {
        configureDatabase(createStatements);
    }

    public UserData getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = String.format("SELECT username, email, password FROM %s WHERE username=?", USERS_TABLE);
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public UserData createUser(UserData user) throws ResponseException, DataAccessException {
        if (user.username() == null || user.email() == null || user.password() == null) {
            throw new DataAccessException("Invalid user data");
        }
        var statement = String.format("INSERT INTO %s (username, email, password) VALUES (?, ?, ?)", USERS_TABLE);
        executeUpdate(statement, user.username(), user.email(), user.password());
        return new UserData(user.username(), user.email(), user.password());
    }

    public void deleteUser(String username) throws ResponseException, DataAccessException {
        var statement = String.format("DELETE FROM %s WHERE username=?", USERS_TABLE);
        executeUpdate(statement, username);
    }

    public void deleteAllUsers() throws ResponseException, DataAccessException {
        var statement = String.format("TRUNCATE %s", USERS_TABLE);
        executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var email = rs.getString("email");
        var password = rs.getString("password");
        return new UserData(username, password, email);
    }
}
