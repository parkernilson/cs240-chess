package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import chess.ChessGame;
import model.GameData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private GameService gameService;
    private UserService userService;

    public static record RequestBody(String gameName) {}

    public CreateGameHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        try {
            final var authToken = req.headers("Authorization");
            if (authToken == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            final var user = userService.getByAuthToken(authToken);
            if (user == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
            final String gameName = requestBody.gameName();

            if (gameName == null) {
                res.status(400);
                return new Gson().toJson("Error: bad request");
            }

            final int gameId = gameService.getNextGameId();
            final var gameData = gameService.createGame(new GameData(
                    gameId,
                    null,
                    null,
                    gameName,
                    new ChessGame()));

            gameService.createGame(gameData);

            res.status(200);
            return new Gson().toJson(Map.of(
                "gameID", gameId
            ));
        } catch(Exception e) {
            res.status(500);
            return "Error: Internal server error";
        }
    }
}
