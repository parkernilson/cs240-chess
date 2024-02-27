package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import chess.ChessGame;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    private GameService gameService;
    private UserService userService;

    public static record RequestBody(String playerColor, int gameID) {
    }

    public JoinGameHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
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

            final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
            final var playerColor = requestBody.playerColor() == "WHITE" ? ChessGame.TeamColor.WHITE
                    : ChessGame.TeamColor.BLACK;
            final var gameId = requestBody.gameID();

            if (playerColor == null || gameId < 0) {
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }

            if (playerColor != null) {
                final var game = gameService.getGame(gameId);
                final var requestedColorUsername = playerColor == ChessGame.TeamColor.WHITE ? game.whiteUsername()
                        : game.blackUsername();
                if (requestedColorUsername != null) {
                    res.status(403);
                    return "Error: already taken";
                }
                gameService.addParticipant(gameId, user.username(), playerColor);
            }

            res.status(200);
            return "";
        } catch(Exception e) {
            res.status(500);
            return "Error: Internal server error";
        }
    }
}
