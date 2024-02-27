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
        String playerColorString;
        int gameId;

        try {
            final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
            playerColorString = requestBody.playerColor();
            gameId = requestBody.gameID();
            if (gameId < 0) {
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

            final var game = gameService.getGame(gameId);
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
                gameService.addParticipant(gameId, user.username(), playerColor);
            }

            res.status(200);
            return "";
        } catch (Exception e) {
            res.status(500);
            return "Error: Internal server error";
        }
    }
}
