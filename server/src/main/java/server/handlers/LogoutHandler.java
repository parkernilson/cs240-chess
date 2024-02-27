package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        try {
            final var authToken = req.headers("Authorization");
            if (authToken == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            final var auth = userService.getAuth(authToken);
            if (auth == null) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            userService.deleteAuth(authToken);
            res.status(200);
            return "";
        } catch(Exception e) {
            res.status(500);
            return "Error: Internal server error";
        }
    }
}
