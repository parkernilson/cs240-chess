package client;

import java.util.Arrays;

import exceptions.ResponseException;
import server.ServerFacade;
import server.model.LoginRequest;
import server.model.RegisterRequest;
import ui.Color;

public class ChessClient {
    private State state = State.SIGNEDOUT;
    private ServerFacade server;

    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
    }

    public State getState() {
        return state;
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "login" -> login(new LoginRequest(params[0], params[1]));
            case "register" -> register(new RegisterRequest(params[0], params[1], params[2]));
            case "logout" -> logout();
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String login(LoginRequest loginRequest) {
        try {
            this.server.login(loginRequest);
            return "Logging in...";
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String register(RegisterRequest registerRequest) {
        try {
            this.server.register(registerRequest);
            return "Registering...";
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String logout() {
        try {
            assertSignedIn();
            this.server.logout();
            return "Logging out...";
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String help() {
        if (state == State.SIGNEDIN) {
            return Color.format("""
                    {LIGHT_GREY} - {BLUE} help
                    {LIGHT_GREY} - {BLUE} quit
                    {LIGHT_GREY} - {BLUE} logout
                    {LIGHT_GREY} - {BLUE} create-game {LIGHT_GREY} <game-name>
                    {LIGHT_GREY} - {BLUE} join-game {LIGHT_GREY} <game-id>
                    {LIGHT_GREY} - {BLUE} join-observer
                    """);
        } else if (state == State.GAMEPLAY) {
            return Color.format("""
                    {LIGHT_GREY} - {BLUE} quit
                    """);
        } else {
            return Color.format("""
                    {LIGHT_GREY} - {BLUE} help
                    {LIGHT_GREY} - {BLUE} quit
                    {LIGHT_GREY} - {BLUE} login {LIGHT_GREY} <username> <password>
                    {LIGHT_GREY} - {BLUE} register {LIGHT_GREY} <username> <password> <email>
                    """);
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
