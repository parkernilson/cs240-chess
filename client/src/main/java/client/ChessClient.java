package client;

import java.util.Arrays;

import chess.ChessGame;
import exceptions.ResponseException;
import server.ServerFacade;
import server.model.CreateGameRequest;
import server.model.JoinGameRequest;
import server.model.LoginRequest;
import server.model.RegisterRequest;
import webSocketMessages.ServerMessageObserver;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import ui.Color;

public class ChessClient implements ServerMessageObserver {
    private State state = State.SIGNEDOUT;
    private ServerFacade server;
    private GameList gameList;

    public ChessClient(String serverUrl) throws ResponseException {
        this.server = new ServerFacade(serverUrl, this);
        this.gameList = null;
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
            case "create" -> createGame(new CreateGameRequest(params[0]));
            case "list" -> listGames();
            case "join" -> joinGame(Integer.parseInt(params[0]), params.length > 1 ? params[1] : null);
            case "observe" -> joinGame(Integer.parseInt(params[0]), null);
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String showDefaultGame() {
        ChessGame game = new ChessGame();
        game.getBoard().resetBoard();
        return ChessGameRenderer.renderGame(game);
    }

    public String login(LoginRequest loginRequest) {
        try {
            final var response = this.server.login(loginRequest);
            this.server.setAuthToken(response.authToken());
            this.state = State.SIGNEDIN;
            return "Logging in...";
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String register(RegisterRequest registerRequest) {
        try {
            final var response = this.server.register(registerRequest);
            this.server.setAuthToken(response.authToken());
            this.state = State.SIGNEDIN;
            return "Registering...";
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String logout() {
        try {
            assertSignedIn();
            this.server.logout();
            this.server.setAuthToken(null);
            this.state = State.SIGNEDOUT;
            return "Logging out...";
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String createGame(CreateGameRequest request) {
        try {
            assertSignedIn();
            var response = this.server.createGame(request);
            return Color.format("Created game with id: {GREEN} %d", response.gameID());
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String listGames() {
        try {
            assertSignedIn();
            var response = this.server.listGames();
            this.gameList = new GameList(response.games());
            return this.gameList.renderGameList();
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String joinGame(int gameNumber, String color) {
        try {
            assertSignedIn();
            var game = this.gameList.get(gameNumber);
            this.server.joinGame(new JoinGameRequest(color, game.gameID()));
            return showDefaultGame();
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String help() {
        if (state == State.SIGNEDIN) {
            return Color.format("""
                    {LIGHT_GREY} - {BLUE} create <NAME> {LIGHT_GREY} - a new game
                    {LIGHT_GREY} - {BLUE} list {LIGHT_GREY} - available games
                    {LIGHT_GREY} - {BLUE} join <ID> [WHITE|BLACK|<empty>] {LIGHT_GREY} - a game
                    {LIGHT_GREY} - {BLUE} observe <ID> {LIGHT_GREY} - a game
                    {LIGHT_GREY} - {BLUE} logout
                    {LIGHT_GREY} - {BLUE} quit
                    {LIGHT_GREY} - {BLUE} help
                    """);
        } else if (state == State.GAMEPLAY) {
            return Color.format("""
                    {LIGHT_GREY} - {BLUE} redraw {LIGHT_GREY} - the board
                    {LIGHT_GREY} - {BLUE} leave {LIGHT_GREY} - the game
                    {LIGHT_GREY} - {BLUE} move <FROM> <TO> {LIGHT_GREY} - a piece
                    {LIGHT_GREY} - {BLUE} resign
                    {LIGHT_GREY} - {BLUE} show-moves <FROM> {LIGHT_GREY} - a piece
                    {LIGHT_GRAY} - {BLUE} help
                    """);
        } else {
            return Color.format("""
                    {LIGHT_GREY} - {BLUE} register {LIGHT_GREY} <username> <password> <email>
                    {LIGHT_GREY} - {BLUE} login {LIGHT_GREY} <username> <password>
                    {LIGHT_GREY} - {BLUE} quit
                    {LIGHT_GREY} - {BLUE} help
                    """);
        }
    }

    public void notify(ServerMessage message) {
        if (message instanceof NotificationMessage) {
            var notificationMessage = (NotificationMessage) message;
            System.out.println(notificationMessage.getMessage());
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
