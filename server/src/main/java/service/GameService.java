package service;

import dataAccess.GameDAO;

public class GameService {
    public GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void deleteAllGames() {
        gameDAO.deleteAllGames();
    }
}
