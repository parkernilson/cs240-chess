package client;

import java.util.ArrayList;
import java.util.Collection;

import model.GameData;

public class GameList {
    private ArrayList<GameData> games;

    public GameList(Collection<GameData> games) {
        this.games = new ArrayList<GameData>(games);
    }

    public GameData get(int i) {
        return games.get(i);
    }

    public int size() {
        return games.size();
    }

    public String renderGameList() {
        var sb = new StringBuilder();
        sb.append("Games:\n");
        for (int i = 0; i < games.size(); i++) {
            var game = games.get(i);
            sb.append(i + ". ");
            sb.append(game.gameName());
            sb.append("\nWhite: " + game.whiteUsername());
            sb.append("\nBlack: " + game.blackUsername());
            sb.append("\n");
        }
        return sb.toString();
    }
}
