package jk.wsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
public class KrakenWebSocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWebSocketService.class);
    private WebSocketClient webSocketClient;
    private KrakenSession session;

    @PostConstruct
    private void connect() {
        try {
            this.webSocketClient = new StandardWebSocketClient();
            var wsSession = webSocketClient.doHandshake(new KrakenWebSocketHandler(), new WebSocketHttpHeaders(), URI.create("wss://ws.kraken.com")).get();
            this.session = new KrakenSession(wsSession);
            this.session.getSession().sendMessage(new TextMessage("{\"event\":\"ping\"}"));
//            newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//                try {
//                    TextMessage message = new TextMessage("{\n" +
//                            "  \"event\": \"ping\",\n" +
//                            "  \"reqid\": 42\n" +
//                            "}");
//                    webSocketSession.sendMessage(message);
//                    LOGGER.info("sent message - " + message.getPayload());
//                } catch (Exception e) {
//                    LOGGER.error("Exception while sending a message", e);
//                }
//            }, 1, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            LOGGER.error("Exception while accessing websockets", e);
        }
    }

    public KrakenSession getSession () {
        return this.session;
    }

    public void subscribe(List<String> pairs, int interval, int depth, String name) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "subscribe");
        json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(pairs));
        json.putObject("subscription").put("name",name).put("interval", interval).put("depth", depth);
        try {
            this.session.getSession().sendMessage(new TextMessage(json.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.session.getSession().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ping() {
        try {
            this.session.getSession().sendMessage(new TextMessage("{\"event\":\"ping\"}"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}