package webSocketMessages.serverMessages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private GameData gameData;

    public LoadGameMessage(GameData gameData) {
        super(ServerMessageType.LOAD_GAME);
    }

    public GameData getGameData() {
        return gameData;
    }
}