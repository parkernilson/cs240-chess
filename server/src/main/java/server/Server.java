package server;

import java.util.Map;

import spark.*;

import com.google.gson.Gson;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;
import server.handlers.ClearApplicationHandler;
import server.handlers.LoginHandler;
import server.handlers.RegisterHandler;
import service.AdminService;
import service.GameService;
import service.UserService;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

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
        Spark.delete("/session", (req, res) -> {
            res.status(200);
            return "";
        });

        // List Games
        Spark.get("/game", (req, res) -> {
            res.status(200);
            return new Gson().toJson(Map.of(
                    "games", new GameData[] {
                            new GameData(
                                    0,
                                    "whiteuser",
                                    "blackuser",
                                    "gamename",
                                    new ChessGame())
                    }));
        });

        // Create Game
        Spark.post("/game", (req, res) -> {
            res.status(200);
            return new Gson().toJson(Map.of(
                    "gameID", 0));
        });

        // Join Game
        Spark.put("/game", (req, res) -> {
            res.status(200);
            return "";
        });

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
