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
import webSocketMessages.userCommands.JoinGameCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {

    private final GameSessionManager sessionManager = new GameSessionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CommandType.JOIN_PLAYER -> joinPlayer(action.getAuthString(), ((JoinGameCommand) action).getGameID(), session);
            case CommandType.JOIN_OBSERVER -> joinObserver();
            case CommandType.MAKE_MOVE -> makeMove();
            case CommandType.LEAVE -> leave();
            case CommandType.RESIGN -> resign();
        }
    }

    private void joinPlayer(String authToken, Integer gameID, Session session) throws IOException {
        sessionManager.addUserToGame(authToken, gameID, session);
        // TODO: get the user and include their username in the message (or include the username in the join game command)
        sessionManager.broadcast(gameID, new NotificationMessage(String.format("%s joined the game", authToken)), List.of(authToken));

        session.getRemote().sendString(new Gson().toJson(new ServerMessage(ServerMessageType.LOAD_GAME)));
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