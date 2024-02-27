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
        String username;
        String password;
        String email;

        try {
            final var requestBody = new Gson().fromJson(req.body(), RequestBody.class);
            username = requestBody.username();
            password = requestBody.password();
            email = requestBody.email();
            if (username == null || password == null || email == null) {
                throw new Exception();
            }
        } catch(Exception e) {
            res.status(400);
            return new Gson().toJson(Map.of(
                "message", "Error: bad request"
            ));
        }

        try {
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
            final var auth = userService.refreshAuth(username);

            // respond with the auth token and username
            return new Gson().toJson(Map.of(
                "username", username,
                "authToken", auth.authToken()
            ));
        } catch(Exception e) {
            res.status(500);
            return new Gson().toJson(Map.of(
                "message", "Error: Internal server error"
            ));
        }
    }
}
