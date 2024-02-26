package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData getAuth(String authToken);
    public AuthData createAuth(String username);
    public void deleteAuth(String authToken);
    public void deleteAllAuth();
}
