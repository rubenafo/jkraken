package jk.wsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class KrakenWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWebSocketHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        LOGGER.info("received message - " + message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("established connection - " + session);
    }
}
