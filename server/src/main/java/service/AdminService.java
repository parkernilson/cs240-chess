package service;

public class AdminService {
    private UserService userService;
    private GameService gameService;

    public AdminService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void clearApplication() {
        userService.deleteAllUsers();
        gameService.deleteAllGames();
    }
}
