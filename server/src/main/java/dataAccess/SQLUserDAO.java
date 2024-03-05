package dataAccess;

import java.sql.SQLException;

import model.UserData;

public class SQLUserDAO implements UserDAO {

    SQLUserDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }
    
    public UserData getUser(String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public UserData createUser(UserData user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteAllUsers() {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
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
