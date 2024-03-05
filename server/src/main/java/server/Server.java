package server;

import spark.*;
import dataAccess.AuthDAO;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import dataAccess.UserDAO;
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

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        AuthDAO authDAO;
        UserDAO userDAO;
        GameDAO gameDAO;

        try {
            DatabaseManager.createDatabase();
            authDAO = new SQLAuthDAO();
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
        } catch(Exception e) {
            System.out.println("Failed to connect to SQL database. Using memory database instead.");
            e.printStackTrace();
            authDAO = new MemoryAuthDAO();
            userDAO = new MemoryUserDAO();
            gameDAO = new MemoryGameDAO();
        }

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
