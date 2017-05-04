package org.zella.net.impl;


import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dru on 11.01.16.
 */
public class WebSocketGameClient extends WebSocketClient {

    private final Executor executor = Executors.newSingleThreadExecutor();

    private static final Logger log = LoggerFactory.getLogger(WebSocketGameClient.class);

    private final ActorRef botActor;

    public WebSocketGameClient(URI serverURI, ActorRef botActor) throws URISyntaxException {
        super(serverURI);

//      super(new URI("ws://192.168.3.103:9000/game?email=email1&pass=pass1"));
        this.botActor = botActor;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        log.trace("Connection opened");
    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        log.trace("Text send to server: {}", text);
        executor.execute(() -> super.send(text));

    }

    @Override
    public void onMessage(String message) {
        log.trace("From: {}", message);
        botActor.tell(message
          , ActorRef.noSender());

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.trace("Connection closed: {}", reason);
        botActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }

    @Override
    public void onError(Exception ex) {
        log.error("Error: ", ex);
        botActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
