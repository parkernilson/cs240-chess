package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    private UserService userService;

    private static record RequestBody(String username, String password) {
    }

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        try {
            final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
            final String username = requestBody.username();
            final String password = requestBody.password();
            
            if (username == null || password == null) {
                res.status(400);
                return new Gson().toJson("Error: username and password are required");
            }

            final var user = userService.getUser(username);

            if (user == null || !user.password().equals(password)) {
                res.status(401);
                return new Gson().toJson("Error: unauthorized");
            }

            final var newAuth = userService.refreshAuth(username);

            res.status(200);
            return new Gson().toJson(Map.of(
                "username", username,
                "authToken", newAuth.authToken()
            ));
        } catch(Exception e) {
            res.status(500);
            return new Gson().toJson("Error: Internal server error");
        }
    }
}
