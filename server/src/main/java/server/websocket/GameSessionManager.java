package server.websocket;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

public class GameSessionManager {
    private final HashMap<Integer, Collection<Connection>> gameConnections = new HashMap<Integer, Collection<Connection>>();
    private final HashMap<String, Integer> userGames = new HashMap<String, Integer>();

    public void addUserToGame(String authToken, int gameID, Session session) {
        userGames.put(authToken, gameID);
        final Connection connection = new Connection(authToken, session);
        if (gameConnections.containsKey(gameID)) {
            gameConnections.get(gameID).add(connection);
        } else {
            gameConnections.put(gameID, List.of(connection));
        }
    }

    public void removeUserFromGame(String authToken) {
        if (userGames.containsKey(authToken)) {
            final int gameID = userGames.get(authToken);
            userGames.remove(authToken);
            if (gameConnections.containsKey(gameID)) {
                final Collection<Connection> connections = gameConnections.get(gameID);
                connections.removeIf(connection -> connection.authToken().equals(authToken));
            }
        }
    }

    public void broadcast(Integer gameID, Object message) {
        broadcast(gameID, message, List.of());
    }

    public void broadcast(Integer gameID, Object message, Collection<String> excludedUsers) {
        for (Connection connection : gameConnections.get(gameID)) {
            // Clean up any connections that have been closed
            if (!connection.session().isOpen()) {
                gameConnections.get(gameID).remove(connection);

                // remove the game from the list if there are no more connections
                if (gameConnections.get(gameID).isEmpty()) {
                    gameConnections.remove(gameID);
                }

                continue;
            }

            // Send message to all non-excluded connections to the game
            if (!excludedUsers.contains(connection.authToken())) {
                connection.session().getRemote().sendStringByFuture(new Gson().toJson(message));
            }
        }
    }

    public Integer getUserGameID(String authToken) {
        return userGames.get(authToken);
    }

}
