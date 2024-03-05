package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData getAuth(String authToken) throws ResponseException;
    public AuthData getAuthByUsername(String username) throws ResponseException;
    public AuthData createAuth(String username) throws ResponseException, DataAccessException;
    public void deleteAuth(String authToken) throws ResponseException, DataAccessException;
    public void deleteAllAuth() throws ResponseException, DataAccessException;
}
