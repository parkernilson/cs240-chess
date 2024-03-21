package client;

import java.util.ArrayList;
import java.util.Collection;

import model.GameData;
import ui.Color;
import static ui.EscapeSequences.*;

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
        sb.append(Color.format("Games{LIGHT_GREY} :\n"));
        for (int i = 0; i < games.size(); i++) {
            var game = games.get(i);
            sb.append(BLUE + i + LIGHT_GREY + ". ");
            sb.append(WHITE + game.gameName());
            sb.append(LIGHT_GREY + "\nWhite: " + WHITE + game.whiteUsername());
            sb.append(LIGHT_GREY + "\nBlack: " + WHITE + game.blackUsername());
            sb.append("\n");
        }
        return sb.toString();
    }
}
