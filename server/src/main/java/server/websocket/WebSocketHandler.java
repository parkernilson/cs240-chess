package server.websocket;

import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;

import chess.ChessGame.TeamColor;
import exceptions.ResponseException;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerMessage.ServerMessageType;
import webSocketMessages.userCommands.JoinGameCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;

@WebSocket
public class WebSocketHandler {

    private final GameSessionManager sessionManager = new GameSessionManager();
    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CommandType.JOIN_PLAYER -> joinPlayer((JoinGameCommand) action, session);
            case CommandType.JOIN_OBSERVER -> joinPlayer((JoinGameCommand) action, session);
            case CommandType.MAKE_MOVE -> makeMove();
            case CommandType.LEAVE -> leave();
            case CommandType.RESIGN -> resign();
        }
    }

    private void joinPlayer(JoinGameCommand action, Session session) throws IOException {
        final var authToken = action.getAuthString();
        final var gameID = action.getGameID();
        final var teamColor = action.getTeamColor();

        UserData user;
        try {
            user = userService.getUser(authToken);
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid auth token")));
            return;
        }

        GameData game;
        try {
            game = gameService.getGame(gameID);
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid game ID")));
            return;
        }

        if (teamColor != null) {
            final var requestedColorUsername = teamColor == TeamColor.WHITE ? game.whiteUsername()
                    : game.blackUsername();
            if (requestedColorUsername != user.username()) {
                session.getRemote().sendString(new Gson().toJson(
                        new ErrorMessage("The color was either taken or the user has not joined the game yet.")));
                return;
            }
        }

        sessionManager.addUserToGame(authToken, gameID, session);
        sessionManager.broadcast(gameID, new NotificationMessage(String.format("%s joined the game", user.username())),
                List.of(authToken));

        final var loadGameMessage = new LoadGameMessage(game);

        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
    }

    private void makeMove(MakeMoveCommand action, Session session) throws IOException {
        final var authToken = action.getAuthString();
        final var move = action.getMove();

        UserData user;
        try {
            user = userService.getUser(authToken);
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid auth token")));
            return;
        }

        // GameData game;
        // try {
        //     game = gameService.getGame(gameID);
        // } catch (ResponseException e) {
        //     session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid game ID")));
        //     return;
        // }

    }

    private void leave() {

    }

    private void resign() {

    }
}