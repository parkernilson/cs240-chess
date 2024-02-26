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
        final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
        final String username = requestBody.username();
        final String password = requestBody.password();
        
        final var user = userService.getUser(username);

        if (user == null || !user.password().equals(password)) {
            res.status(403);
            return new Gson().toJson("Error: invalid username or password");
        }

        final var existingAuth = userService.getAuthByUsername(username);

        if (existingAuth != null) {
            userService.deleteAuth(existingAuth.authToken());
        }

        final var newAuth = userService.createAuth(username);

        res.status(200);

        return new Gson().toJson(Map.of(
            "username", username,
            "authToken", newAuth.authToken()
        ));
    }
}
