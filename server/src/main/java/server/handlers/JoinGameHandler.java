package server.handlers;

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
        final var authToken = req.headers("Authorization");
        final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);

        final var user = userService.getByAuthToken(authToken);

        final var playerColor = requestBody.playerColor() == "WHITE" ? ChessGame.TeamColor.WHITE
                : ChessGame.TeamColor.BLACK;
        final var gameID = requestBody.gameID();

        try {
            gameService.addParticipant(gameID, user.username(), playerColor);
        } catch( Exception e) {
            res.status(400);
            return e.getMessage();
        }

        res.status(200);
        return "";
    }
}
