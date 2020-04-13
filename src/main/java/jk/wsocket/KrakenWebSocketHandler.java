package jk.wsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class KrakenWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWebSocketHandler.class);
    private final KrakenWebSocketService krakenWs;

    @Autowired
    public KrakenWebSocketHandler(@NonNull KrakenWebSocketService krakenWs) {
        this.krakenWs = krakenWs;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        LOGGER.info("received message - " + message.getPayload());
        this.krakenWs.getSession().handleMessage (message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("established connection - " + session);
    }
}
