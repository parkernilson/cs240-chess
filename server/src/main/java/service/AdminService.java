package service;

import dataAccess.DataAccessException;
import exceptions.ResponseException;

public class AdminService {
    private UserService userService;
    private GameService gameService;

    public AdminService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void clearApplication() throws ResponseException, DataAccessException {
        userService.deleteAllUsers();
        gameService.deleteAllGames();
    }
}
