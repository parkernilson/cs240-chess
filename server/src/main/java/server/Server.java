package server;

import spark.*;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.handlers.ClearApplicationHandler;
import server.handlers.CreateGameHandler;
import server.handlers.JoinGameHandler;
import server.handlers.ListGamesHandler;
import server.handlers.LoginHandler;
import server.handlers.LogoutHandler;
import server.handlers.RegisterHandler;
import service.AdminService;
import service.GameService;
import service.UserService;

public class Server {

    public int run(int desiredPort) throws DataAccessException {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        DatabaseManager.createDatabase();

        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryGameDAO gameDAO = new MemoryGameDAO();
        UserService userService = new UserService(userDAO, authDAO);
        GameService gameService = new GameService(gameDAO);
        AdminService adminService = new AdminService(userService, gameService);

        // Clear Application
        Spark.delete("/db", new ClearApplicationHandler(adminService)::handle);

        // Register
        Spark.post("/user", new RegisterHandler(userService)::handle);

        // Login
        Spark.post("/session", new LoginHandler(userService)::handle);

        // Logout
        Spark.delete("/session", new LogoutHandler(userService)::handle);

        // List Games
        Spark.get("/game", new ListGamesHandler(gameService, userService)::handle);

        // Create Game
        Spark.post("/game", new CreateGameHandler(gameService, userService)::handle);

        // Join Game
        Spark.put("/game", new JoinGameHandler(gameService, userService)::handle);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
