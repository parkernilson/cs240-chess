package server.handlers;

import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object handle(Request req, Response res) {
        final var authToken = req.headers("Authorization");
        userService.deleteAuth(authToken);
        res.status(200);
        return "";
    }
}
