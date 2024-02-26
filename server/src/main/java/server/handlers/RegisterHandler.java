package server.handlers;

import java.util.Map;

import com.google.gson.Gson;

import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    private UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    private static record RequestBody(String username, String password, String email) {
    }

    public Object handle(Request req, Response res) {
        final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
        final String username = requestBody.username();
        final String password = requestBody.password();
        final String email = requestBody.email();

        // look up the user to see if they exist already
        final var existingUser = userService.getUser(username);

        if (existingUser != null) {
            res.status(403);
            return new Gson().toJson(Map.of(
                "message", "Error: already taken"
            ));
        }

        // if the user does not exist, create them
        userService.createUser(new UserData(username, password, email));

        // create a new session for the user
        final var auth = userService.createAuth(username);

        // respond with the auth token and username
        return new Gson().toJson(Map.of(
            "username", username,
            "authToken", auth.authToken()
        ));
    }
}
