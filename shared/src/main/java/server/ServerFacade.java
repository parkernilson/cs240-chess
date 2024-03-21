package server;

import com.google.gson.Gson;
import exceptions.ResponseException;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import server.model.*;

public class ServerFacade {

    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
        this.authToken = null;
    }

    public void setAuthToken(String userToken) {
        this.authToken = userToken;
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, LoginResponse.class);
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, registerRequest, RegisterResponse.class);
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, createGameRequest, CreateGameResponse.class);
    }

    public ListGamesResponse listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResponse.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, joinGameRequest, null);
    }

    public void clearApplication() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass)
            throws ResponseException {
        return makeRequest(method, path, request, responseClass, new HashMap<>());
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass,
            HashMap<String, String> headers) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            for (var entry : headers.entrySet()) {
                http.addRequestProperty(entry.getKey(), entry.getValue());
            }

            if (authToken != null) {
                http.addRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
