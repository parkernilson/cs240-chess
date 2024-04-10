package server.websocket;

import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;

import chess.ChessGame;
import chess.ChessGame.TeamColor;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import exceptions.ResponseException;
import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import util.StringUtils;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.JoinGameCommand;
import webSocketMessages.userCommands.LeaveGameCommand;
import webSocketMessages.userCommands.MakeMoveCommand;
import webSocketMessages.userCommands.ResignGameCommand;
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
            case CommandType.JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinGameCommand.class), session);
            case CommandType.JOIN_OBSERVER -> joinPlayer(new Gson().fromJson(message, JoinGameCommand.class), session);
            case CommandType.MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMoveCommand.class), session);
            case CommandType.LEAVE -> leave(new Gson().fromJson(message, LeaveGameCommand.class), session);
            case CommandType.RESIGN -> resign(new Gson().fromJson(message, ResignGameCommand.class), session);
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    private void joinPlayer(JoinGameCommand action, Session session) throws IOException {
        final var authToken = action.getAuthString();
        final var gameID = action.getGameID();
        final var teamColor = action.getPlayerColor();

        UserData user;
        try {
            user = userService.getByAuthToken(authToken);
            if (user == null) {
                throw new ResponseException(401, "Invalid auth token");
            }
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid auth token")));
            return;
        }

        GameData game;
        try {
            game = gameService.getGame(gameID);
            if (game == null) {
                throw new ResponseException(404, "Game not found");
            }
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid game ID")));
            return;
        }

        if (teamColor != null) {
            final var requestedColorUsername = teamColor == TeamColor.WHITE ? game.whiteUsername()
                    : game.blackUsername();
            if (!StringUtils.equals(requestedColorUsername, user.username())) {
                session.getRemote().sendString(new Gson().toJson(
                        new ErrorMessage("The color was either taken or the user has not joined the game yet.")));
                return;
            }
            sessionManager.broadcast(gameID,
                    new NotificationMessage(String.format("%s joined the game as %s", user.username(), teamColor)),
                    List.of(authToken));
        } else {
            sessionManager.broadcast(gameID,
                    new NotificationMessage(String.format("%s joined the game as an observer", user.username())));
        }

        sessionManager.addUserToGame(authToken, gameID, session);

        final var loadGameMessage = new LoadGameMessage(game);

        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
    }

    private String checkWinConditions(ChessGame game) {
        if (game.getResigned() != null) {
            return game.getResigned() == TeamColor.WHITE ? "White resigned" : "Black resigned";
        } else if (game.isInCheckmate(TeamColor.WHITE)) {
            return "Black wins!";
        } else if (game.isInCheckmate(TeamColor.BLACK)) {
            return "White wins!";
        } else if (game.isInStalemate(TeamColor.WHITE) || game.isInStalemate(TeamColor.BLACK)) {
            return "Stalemate!";
        }
        return null;
    }

    private void makeMove(MakeMoveCommand action, Session session) throws IOException {
        final var authToken = action.getAuthString();
        final var move = action.getMove();

        UserData user;
        try {
            user = userService.getByAuthToken(authToken);
            if (user == null) {
                throw new ResponseException(401, "Invalid auth token");
            }
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid auth token")));
            return;
        }

        final Integer gameID = sessionManager.getUserGameID(authToken);
        if (gameID == null) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("User is not in a game")));
            return;
        }

        GameData gameData;
        try {
            gameData = gameService.getGame(gameID);
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid game ID")));
            return;
        }

        TeamColor currentTeam = gameData.game().getTeamTurn();
        TeamColor otherTeam = currentTeam == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;

        // if the game is already over, send the message and don't allow any more moves
        String endGameMessage = checkWinConditions(gameData.game());
        if (endGameMessage != null) {
            session.getRemote()
                    .sendString(new Gson().toJson(new ErrorMessage(String.format("Game is over: %s", endGameMessage))));
            return;
        }

        // Make sure the user is on the correct team
        final var currentColorUsername = currentTeam == TeamColor.WHITE ? gameData.whiteUsername()
                : gameData.blackUsername();
        if (!StringUtils.equals(currentColorUsername, user.username())) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("It is not your turn")));
            return;
        }

        // try to make the move
        try {
            gameData.game().makeMove(move);
            gameService.updateGame(gameData);

            // send the updated game state to all players
            final var loadGameMessage = new LoadGameMessage(gameData);
            sessionManager.broadcast(gameID, loadGameMessage);
            // send the move notification to all other players
            sessionManager.broadcast(gameID,
                    new NotificationMessage(String.format("%s made move %s", user.username(), move)),
                    List.of(authToken));

            // check for win conditions after move
            final var endGameMessageAfterMove = checkWinConditions(gameData.game());
            if (endGameMessageAfterMove != null) {
                sessionManager.broadcast(gameID, new NotificationMessage(endGameMessageAfterMove));
            } else {
                if (gameData.game().isInCheck(otherTeam)) {
                    sessionManager.broadcast(gameID,
                            new NotificationMessage(String.format("%s is in check!", otherTeam)));
                }
            }
        } catch (InvalidMoveException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid move")));
            return;
        } catch (DataAccessException e) {
            session.getRemote()
                    .sendString(new Gson().toJson(new ErrorMessage("Something went wrong when updating the game")));
            e.printStackTrace();
            return;
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(e.getMessage())));
            e.printStackTrace();
            return;
        }

    }

    private void leave(LeaveGameCommand action, Session session) throws IOException {
        final var authToken = action.getAuthString();

        try {
            UserData user = userService.getByAuthToken(authToken);
            if (user == null) {
                throw new ResponseException(401, "Invalid auth token");
            }

            GameData game = gameService.getGame(action.getGameID());
            GameData newGame = new GameData(
                    game.gameID(),
                    StringUtils.equals(game.whiteUsername(), user.username()) ? null : game.whiteUsername(),
                    StringUtils.equals(game.blackUsername(), user.username()) ? null : game.blackUsername(),
                    game.gameName(),
                    game.game());
            gameService.updateGame(newGame);

            sessionManager.broadcast(action.getGameID(),
                    new NotificationMessage(
                            String.format("%s left the game", user.username())));
            sessionManager.removeUserFromGame(authToken);
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("Invalid auth token")));
            e.printStackTrace();
            return;
        } catch (DataAccessException e) {
            session.getRemote()
                    .sendString(new Gson().toJson(new ErrorMessage("Something went wrong when updating the game")));
            e.printStackTrace();
            return;
        }
    }

    private void resign(ResignGameCommand action, Session session) throws IOException {
        final var authToken = action.getAuthString();

        try {
            UserData user = userService.getByAuthToken(authToken);
            if (user == null) {
                throw new ResponseException(401, "Invalid auth token");
            }

            GameData gameData = gameService.getGame(action.getGameID());
            TeamColor color = StringUtils.equals(gameData.whiteUsername(), user.username()) ? TeamColor.WHITE
                    : null;
            color = StringUtils.equals(gameData.blackUsername(), user.username()) ? TeamColor.BLACK : color;

            if (color == null) {
                throw new ResponseException(400, "User is not in the game");
            }

            final var endGameMessage = checkWinConditions(gameData.game());
            if (endGameMessage != null) {
                session.getRemote()
                        .sendString(new Gson().toJson(new ErrorMessage(String.format("Game is over: %s", endGameMessage))));
                return;
            }

            gameData.game().setResigned(color);
            GameData newGame = new GameData(
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game());
            gameService.updateGame(newGame);

            sessionManager.broadcast(action.getGameID(),
                    new NotificationMessage(
                            String.format("%s resigned the game", user.username())));
        } catch (ResponseException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage(e.getMessage())));
            e.printStackTrace();
            return;
        } catch (DataAccessException e) {
            session.getRemote()
                    .sendString(new Gson().toJson(new ErrorMessage("Something went wrong when updating the game")));
            e.printStackTrace();
            return;
        }
    }
}