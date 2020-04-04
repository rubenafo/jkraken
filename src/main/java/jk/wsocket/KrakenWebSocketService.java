package jk.wsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Service
public class KrakenWebSocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWebSocketService.class);
    private WebSocketClient webSocketClient;
    private WebSocketSession webSocketSession;

    @PostConstruct
    public void connect() {
        try {
            this.webSocketClient = new StandardWebSocketClient();
            this.webSocketSession = webSocketClient.doHandshake(new KrakenWebSocketHandler(), new WebSocketHttpHeaders(), URI.create("wss://ws.kraken.com")).get();

            newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                try {
//                    TextMessage message = new TextMessage("{\n" +
//                            "  \"event\": \"ping\",\n" +
//                            "  \"reqid\": 42\n" +
//                            "}");
//                    webSocketSession.sendMessage(message);
//                    LOGGER.info("sent message - " + message.getPayload());
                } catch (Exception e) {
                    LOGGER.error("Exception while sending a message", e);
                }
            }, 1, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("Exception while accessing websockets", e);
        }
    }
}