package server;

import webSocketMessages.ServerMessageObserver;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.*;

import com.google.gson.Gson;

import exceptions.ResponseException;

public class WebSocketCommunicator {
    String url;
    Session session;
    ServerMessageObserver observer;

    public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer) throws ResponseException {
        try {
            this.url = serverUrl.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.observer = observer;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    observer.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    // @Override
    // public void onOpen(Session session, EndpointConfig endpointConfig) {
    // }

    // public void enterPetShop(String visitorName) throws ResponseException {
    //     try {
    //         var action = new Action(Action.Type.ENTER, visitorName);
    //         this.session.getBasicRemote().sendText(new Gson().toJson(action));
    //     } catch (IOException ex) {
    //         throw new ResponseException(500, ex.getMessage());
    //     }
    // }

    // public void leavePetShop(String visitorName) throws ResponseException {
    //     try {
    //         var action = new Action(Action.Type.EXIT, visitorName);
    //         this.session.getBasicRemote().sendText(new Gson().toJson(action));
    //         this.session.close();
    //     } catch (IOException ex) {
    //         throw new ResponseException(500, ex.getMessage());
    //     }
    // }
}