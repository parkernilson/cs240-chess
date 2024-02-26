package dataAccess;

import java.util.HashMap;
import java.util.Map;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    private Map<String, AuthData> auths = new HashMap<String, AuthData>();

    public AuthData getAuth(String username) {
        return auths.get(username);
    }

    public AuthData createAuth(String username) {
        AuthData auth = new AuthData(username, "token");
        auths.put(username, auth);
        return auth;
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    public void deleteAllAuth() {
        auths.clear();
    }
}
