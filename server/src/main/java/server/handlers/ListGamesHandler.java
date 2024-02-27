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
        String authToken = req.headers("Authorization");

        UserData user = userService.getByAuthToken(authToken);

        GameData[] games = gameService.listGames(user.username());

        res.status(200);
        return new Gson().toJson(Map.of(
            "games", games
        ));
    }
}
