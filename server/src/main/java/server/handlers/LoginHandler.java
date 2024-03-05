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
        String username;
        String password;

        try {
            final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
            username = requestBody.username();
            password = requestBody.password();
            
            if (username == null || password == null) {
                throw new Exception();
            }
        } catch(Exception e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", "Error: bad request"));
        }

        try {
            final var user = userService.getUser(username);
            var encryptedPassword = userService.encryptPassword(password);

            if (user == null || !user.password().equals(encryptedPassword)) {
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }

            final var newAuth = userService.createAuth(username);

            res.status(200);
            return new Gson().toJson(Map.of(
                "username", username,
                "authToken", newAuth.authToken()
            ));
        } catch(Exception e) {
            res.status(500);
            return new Gson().toJson(Map.of("message", "Error: Internal server error"));
        }
    }
}
