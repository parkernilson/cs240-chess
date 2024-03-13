package client;

public class ServerFacade {

    private String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    // TODO: wherever response data should be read, either return a
    // record type from the shared module, or create a response record type
    // Also, wherever useful take one of the record types as input

    public void registerUser(String name, String password) {

    }

    public void signIn(String name, String password) {

    }

    public void signOut() {

    }

    public void listGames() {
        
    }

    public void createGame() {

    }

    public void joinGame(int gameId) {

    }
}
