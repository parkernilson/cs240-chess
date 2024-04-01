package server;

import com.google.gson.Gson;
import exceptions.ResponseException;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import server.model.*;
import webSocketMessages.ServerMessageObserver;

public class ServerFacade {

    private final String serverUrl;
    private String authToken;
    private ServerMessageObserver observer;
    private HttpCommunicator http;

    public ServerFacade(String url, ServerMessageObserver observer) {
        serverUrl = url;
        this.authToken = null;
        this.observer = observer;
        this.http = new HttpCommunicator(serverUrl);
    }

    public void setAuthToken(String userToken) {
        this.authToken = userToken;
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.http.makeRequest("POST", authToken, path, loginRequest, LoginResponse.class);
    }

    public RegisterResponse register(String authToken, RegisterRequest registerRequest) throws ResponseException {
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
        this.http.makeRequest("PUT", authToken, path, joinGameRequest, null);
    }

    public void clearApplication() throws ResponseException {
        var path = "/db";
        this.http.makeRequest("DELETE", authToken, path, null, null);
    }
 
}
