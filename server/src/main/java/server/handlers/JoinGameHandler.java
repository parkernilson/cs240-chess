package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import chess.ChessGame;
import server.model.JoinGameRequest;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    private GameService gameService;
    private UserService userService;

    public JoinGameHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        String playerColorString;
        int gameID;

        try {
            final var requestBody = new Gson().fromJson(req.body(), JoinGameRequest.class);
            playerColorString = requestBody.playerColor();
            if (playerColorString != null) {
                playerColorString = playerColorString.toUpperCase();
            }
            gameID = requestBody.gameID();
            if (gameID < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }

        try {
            final var authToken = req.headers("Authorization");
            if (authToken == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: Unauthorized"));
            }
            final var user = userService.getByAuthToken(authToken);
            if (user == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: Unauthorized"));
            }

            final var playerColor = playerColorString == null ? null : ChessGame.TeamColor.valueOf(playerColorString);

            final var game = gameService.getGame(gameID);
            if (game == null) {
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }

            if (playerColor != null) {
                final var requestedColorUsername = playerColor == ChessGame.TeamColor.WHITE ? game.whiteUsername()
                        : game.blackUsername();
                if (requestedColorUsername != null) {
                    res.status(403);
                    return new Gson().toJson(Map.of("message", "Error: already taken"));
                }
                gameService.addParticipant(gameID, user.username(), playerColor);
            }

            res.status(200);
            return "";
        } catch (Exception e) {
            res.status(500);
            return "Error: Internal server error";
        }
    }
}
