package service;

import dataAccess.GameDAO;
import model.GameData;

public class GameService {
    private GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void deleteAllGames() {
        gameDAO.deleteAllGames();
    }

    public GameData[] listGames(String username) {
        return gameDAO.listGames(username);
    }
}
