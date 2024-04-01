package server.websocket;

import com.google.gson.Gson;
import exceptions.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerMessage.ServerMessageType;
import webSocketMessages.userCommands.UserGameCommand.CommandType;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CommandType.JOIN_PLAYER -> joinPlayer(action.getAuthString(), session);
            case CommandType.JOIN_OBSERVER -> joinObserver();
            case CommandType.MAKE_MOVE -> makeMove();
            case CommandType.LEAVE -> leave();
            case CommandType.RESIGN -> resign();
        }
    }

    private void joinPlayer(String authToken, Session session) throws IOException {
        connections.add(authToken, session);
        // TODO: get the user
        String username = "username";
        var notification = new NotificationMessage(String.format("%s has joined the game", username));
        connections.broadcast(authToken, notification);
    }

    private void joinObserver() {

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}