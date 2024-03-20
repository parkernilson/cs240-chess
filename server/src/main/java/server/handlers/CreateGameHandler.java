package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import chess.ChessGame;
import model.GameData;
import server.model.CreateGameRequest;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private GameService gameService;
    private UserService userService;

    public CreateGameHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        String gameName;

        try {
            final var requestBody = new Gson().fromJson(req.body(), CreateGameRequest.class);
            gameName = requestBody.gameName();
            if (gameName == null) {
                throw new Exception();
            }
        } catch(Exception e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }

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

            final int gameId = gameService.getNextGameId();
            final var gameData = gameService.createGame(new GameData(
                    gameId,
                    null,
                    null,
                    gameName,
                    new ChessGame()));

            res.status(200);
            return new Gson().toJson(Map.of(
                "gameID", gameData.gameID()
            ));
        } catch(Exception e) {
            res.status(500);
            return "Error: Internal server error";
        }
    }
}
