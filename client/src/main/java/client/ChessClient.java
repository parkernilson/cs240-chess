package client;

import java.util.Arrays;

import com.google.gson.Gson;

import exceptions.ResponseException;

public class ChessClient {
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        // var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
