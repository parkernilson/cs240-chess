package server;

import chess.ChessMove;
import exceptions.ResponseException;
import server.model.CreateGameRequest;
import server.model.CreateGameResponse;
import server.model.JoinGameRequest;
import server.model.ListGamesResponse;
import server.model.LoginRequest;
import server.model.LoginResponse;
import server.model.RegisterRequest;
import server.model.RegisterResponse;
import webSocketMessages.ServerMessageObserver;

public class ServerFacade {

    private final String serverUrl;
    private String authToken;
    private WebSocketCommunicator ws;
    private HttpCommunicator http;

    public ServerFacade(String url, ServerMessageObserver observer) throws ResponseException {
        serverUrl = url;
        this.authToken = null;
        this.http = new HttpCommunicator(serverUrl);
        this.ws = new WebSocketCommunicator(serverUrl, observer);
    }

    public void setAuthToken(String userToken) {
        this.authToken = userToken;
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.http.makeRequest("POST", authToken, path, loginRequest, LoginResponse.class);
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return this.http.makeRequest("POST", authToken, path, registerRequest, RegisterResponse.class);
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.http.makeRequest("DELETE", authToken, path, null, null);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        return this.http.makeRequest("POST", authToken, path, createGameRequest, CreateGameResponse.class);
    }

    public ListGamesResponse listGames() throws ResponseException {
        var path = "/game";
        return this.http.makeRequest("GET", authToken, path, null, ListGamesResponse.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        // Join the game on the server
        this.http.makeRequest("PUT", authToken, path, joinGameRequest, null);

        // join the game on the websocket
        this.ws.joinGame(authToken, joinGameRequest);
    }

    public void leaveGame(int gameID) throws ResponseException {
        // Leave the game on the websocket
        this.ws.leaveGame(authToken, gameID);
    }

    public void clearApplication() throws ResponseException {
        var path = "/db";
        this.http.makeRequest("DELETE", authToken, path, null, null);
    }

    public void makeMove(ChessMove move) throws ResponseException {
        this.ws.makeMove(authToken, move);
    }
 
}
