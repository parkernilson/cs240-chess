package server;

import webSocketMessages.ServerMessageObserver;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinGameCommand;
import webSocketMessages.userCommands.LeaveGameCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.*;

import com.google.gson.Gson;

import chess.ChessGame.TeamColor;
import exceptions.ResponseException;
import server.model.JoinGameRequest;

public class WebSocketCommunicator {
    String url;
    Session session;
    ServerMessageObserver observer;

    public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer) throws ResponseException {
        try {
            this.url = serverUrl.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.observer = observer;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    observer.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ResponseException {
        // TODO: how to re open the connection after it has been closed?
        try {
            var action = new JoinGameCommand(authToken, joinGameRequest.gameID(),
                    TeamColor.valueOf(joinGameRequest.playerColor()));
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException {
        try {
            var action = new LeaveGameCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close(); // TODO: should I close this?
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
