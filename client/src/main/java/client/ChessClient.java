package client;

import java.util.Arrays;

import exceptions.ResponseException;
import ui.Color;

public class ChessClient {
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
    }

    public State getState() {
        return state;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String help() {
        if (state == State.SIGNEDIN) {
            return Color.format("""
                    %light_grey - %blue help
                    %light_grey - %blue quit
                    %light_grey - %blue logout
                    %light_grey - %blue create-game %light_grey <game-name>
                    %light_grey - %blue join-game %light_grey <game-id>
                    %light_grey - %blue join-observer
                    """);
        } else if (state == State.GAMEPLAY) {
            return Color.format("""
                    %light_grey - %blue quit
                    """);
        } else {
            return Color.format("""
                    %light_grey - %blue help
                    %light_grey - %blue quit
                    %light_grey - %blue login %light_grey <username> <password>
                    %light_grey - %blue register %light_grey <username> <password>
                    """);
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
