package server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.google.gson.Gson;

import chess.ChessGame.TeamColor;
import chess.ChessMove;
import exceptions.ResponseException;
import server.model.JoinGameRequest;
import webSocketMessages.ServerMessageObserver;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinGameCommand;
import webSocketMessages.userCommands.LeaveGameCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.ResignGameCommand;

public class WebSocketCommunicator extends Endpoint {
    String url;
    WebSocketContainer container;
    Session session;
    ServerMessageObserver observer;

    public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer) throws ResponseException {
        try {
            this.url = serverUrl.replace("http", "ws");
            this.observer = observer;

            connect();

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void connect() throws URISyntaxException, DeploymentException, IOException {
        URI socketURI = new URI(this.url + "/connect");

        this.container = ContainerProvider.getWebSocketContainer();
        this.session = this.container.connectToServer(this, socketURI);

        // set message handler
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                switch (notification.getServerMessageType()) {
                    case ERROR -> {
                        observer.notify(new Gson().fromJson(message, ErrorMessage.class));
                    }
                    case LOAD_GAME -> {
                        observer.notify(new Gson().fromJson(message, LoadGameMessage.class));
                    }
                    case NOTIFICATION -> {
                        observer.notify(new Gson().fromJson(message, NotificationMessage.class));
                    }
                }
            }
        });
    }

    // Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws ResponseException {
        try {
            String colorName = joinGameRequest.playerColor() != null ? joinGameRequest.playerColor().toUpperCase()
                    : null;
            TeamColor color = colorName != null ? TeamColor.valueOf(colorName) : null;
            var action = new JoinGameCommand(authToken, joinGameRequest.gameID(), color);
            connect();
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IllegalArgumentException e) {
            throw new ResponseException(400, "Invalid color");
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }

    public void leaveGame(String authToken, int gameID, boolean resign) throws ResponseException {
        try {
            var action = resign ? new ResignGameCommand(authToken, gameID) : new LeaveGameCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, ChessMove move) throws ResponseException {
        try {
            var action = new MakeMoveCommand(authToken, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new ResponseException(500, authToken);
        }
    }
}
