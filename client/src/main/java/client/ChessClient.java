package client;

import java.util.Arrays;
import java.util.stream.Collectors;

import chess.ChessMove;
import chess.ChessPiece.PieceType;
import chess.ChessPosition;
import exceptions.ResponseException;
import model.GameData;
import server.ServerFacade;
import server.model.CreateGameRequest;
import server.model.JoinGameRequest;
import server.model.LoginRequest;
import server.model.RegisterRequest;
import ui.Color;
import webSocketMessages.ServerMessageObserver;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

public class ChessClient implements ServerMessageObserver {
    private State state = State.SIGNEDOUT;
    private ServerFacade server;
    private GameList gameList;
    private GameData gameData;
    private Repl repl;

    public ChessClient(String serverUrl, Repl repl) throws ResponseException {
        this.server = new ServerFacade(serverUrl, this);
        this.gameList = null;
        this.repl = repl;
    }

    public State getState() {
        return state;
    }

    public String evalSignedIn(String cmd, String[] params) {
        return switch (cmd) {
            case "create" -> createGame(new CreateGameRequest(params[0]));
            case "list" -> listGames();
            case "join" -> joinGame(Integer.parseInt(params[0]), params.length > 1 ? params[1] : null);
            case "observe" -> joinGame(Integer.parseInt(params[0]), null);
            case "logout" -> logout();
            case "quit" -> "quit";
            case "help" -> help();
            default -> help();
        };
    }

    public String evalSignedOut(String cmd, String[] params) {
        return switch (cmd) {
            case "register" -> register(new RegisterRequest(params[0], params[1], params[2]));
            case "login" -> login(new LoginRequest(params[0], params[1]));
            case "quit" -> "quit";
            case "help" -> help();
            default -> help();
        };
    }

    public String evalGameplay(String cmd, String[] params) {
        return switch (cmd) {
            case "redraw" -> redraw();
            case "leave" -> leaveGame(this.gameData.gameID());
            case "move" -> {
                String promotionPieceParam = params.length > 2 ? params[2] : null;
                yield makeMove(params[0], params[1], promotionPieceParam);
            }
            case "resign" -> leaveGame(this.gameData.gameID());
            case "show-moves" -> showMoves(params[0]);
            case "help" -> help();
            default -> help();
        };
    }

    public String eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (state) {
            case State.SIGNEDIN -> evalSignedIn(cmd, params);
            case State.SIGNEDOUT -> evalSignedOut(cmd, params);
            case State.GAMEPLAY -> evalGameplay(cmd, params);
            default -> help();
        };
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
            return String.format("Join game %s...", game.gameName());
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }

    public String redraw() {
        return ChessGameRenderer.renderGame(this.gameData.game());
    }

    public String leaveGame(int gameID) {
        try {
            this.server.leaveGame(gameID);
            state = State.SIGNEDIN;
        } catch (ResponseException e) {
            return e.getMessage();
        }
        return "Leaving game...";
    }

    public String makeMove(String fromPosition, String toPosition, String promotionPiece) {
        ChessPosition from = ChessPosition.parse(fromPosition);
        ChessPosition to = ChessPosition.parse(toPosition);
        PieceType promotionPieceType = promotionPiece == null ? null : PieceType.valueOf(promotionPiece);

        ChessMove move = new ChessMove(from, to, promotionPieceType);

        try {
            // TODO: get response?
            this.server.makeMove(move);
        } catch (ResponseException e) {
            return e.getMessage();
        }
        // TODO: add better message here
        return "Moving...";
    }

    public String showMoves(String fromPosition) {
        ChessPosition from = ChessPosition.parse(fromPosition);

        var validPositions = this.gameData.game()
                .validMoves(from)
                .stream()
                .map(ChessMove::getEndPosition)
                .collect(Collectors.toSet());

        return ChessGameRenderer.renderGame(this.gameData.game(), validPositions);
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
            System.out.println("\n" + notificationMessage.getMessage());
        } else if (message instanceof LoadGameMessage) {
            var loadGameMessage = (LoadGameMessage) message;
            this.gameData = loadGameMessage.getGameData();

            state = State.GAMEPLAY;

            System.out.println("\n" + ChessGameRenderer.renderGame(this.gameData.game()));
        } else if (message instanceof ErrorMessage) {
            var errorMessage = (ErrorMessage) message;
            System.out.println("\nError: " + errorMessage.getMessage());
        }

        repl.printPrompt();
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
