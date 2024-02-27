package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import model.GameData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    private GameService gameService;
    private UserService userService;

    public ListGamesHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");

            if (authToken == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            UserData user = userService.getByAuthToken(authToken);

            if (user == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            GameData[] games = gameService.listGames(user.username());

            res.status(200);
            return new Gson().toJson(Map.of(
                "games", games
            ));
        } catch(Exception e) {
            res.status(500);
            return new Gson().toJson("Error: Internal server error");
        }
    }
}
