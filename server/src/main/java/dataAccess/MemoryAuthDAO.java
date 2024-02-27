package dataAccess;

import java.util.HashMap;
import java.util.Map;

import model.AuthData;
import util.TokenGenerator;

public class MemoryAuthDAO implements AuthDAO {
    /**
     * A map of auth tokens to auth data.
     */
    private Map<String, AuthData> auths = new HashMap<String, AuthData>();

    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    public AuthData getAuthByUsername(String username) {
        for (AuthData auth : auths.values()) {
            if (auth.username().equals(username)) {
                return auth;
            }
        }
        return null;
    }

    /**
     * Create an auth for the given user
     */
    public AuthData createAuth(String username) {
        String authToken = TokenGenerator.generateToken();
        AuthData auth = new AuthData(authToken, username);
        auths.put(authToken, auth);
        return auth;
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    public void deleteAllAuth() {
        auths.clear();
    }
}
