package jk.wsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jk.data.SessionData;
import jk.wsocket.responses.SubscriptionStatusMsg;
import jk.wsocket.responses.TickerMsg;
import lombok.Getter;
import lombok.val;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class KrakenWebSocketService extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWebSocketService.class);

    @Getter
    private SessionData sessionData;
    private WebSocketSession wsSession;
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public KrakenWebSocketService () {
        this.sessionData = new SessionData();
    }

    @PostConstruct
    protected boolean connect() {
        try {
            var webSocketClient = new StandardWebSocketClient();
            this.wsSession = webSocketClient.doHandshake(this, new WebSocketHttpHeaders(), URI.create("wss://ws.kraken.com")).get();
            return true;
        } catch (Exception e) {
            LOGGER.error("Exception while accessing websockets", e);
        }
        return false;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        LOGGER.info("received message - " + message.getPayload());
        String rawMsg = message.getPayload();
        try {
            Object obj = this.jsonMapper.readValue(rawMsg, Object.class);
            if (obj instanceof Map) {
                val eventType = (String) ((Map) obj).get("event");
                switch (eventType) {
                    case "ping": case "pong": case "heartbeat": break;
                    case "subscriptionStatus":
                        val subscribeMsg = this.jsonMapper.readValue(rawMsg, SubscriptionStatusMsg.class);
                        this.sessionData.append(subscribeMsg, System.currentTimeMillis());
                        break;
                    default:
                        LOGGER.debug("Error: ignoring unknown event type received={}", eventType);
                }
            } else if (obj instanceof List) {
                val jsonList = (List) obj;
                int channelId = (int) jsonList.get(0);
                val channelName = (String) jsonList.get(2);
                val pair = (String) jsonList.get(3);
                switch (channelName) {
                    case "ticker":
                        val tickerMsg = TickerMsg.fromMap(channelId, channelName, pair, (Map) jsonList.get(1));
                        this.sessionData.append(tickerMsg, System.currentTimeMillis());
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        LOGGER.info("established connection - " + session);
    }

    public void subscribe(List<String> pairs, int interval, int depth, String name) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "subscribe");
        json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(pairs));
        json.putObject("subscription").put("name",name);
        System.out.println(json.toString());
        try {
            this.wsSession.sendMessage(new TextMessage(json.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(List<String> channelIds) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "unsubscribe");
        json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(channelIds));
        json.putObject("subscription").put("name","ticker");
        try {
            this.wsSession.sendMessage(new TextMessage(json.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            val IDs = this.sessionData.getChannelIDs();
            IDs.stream().forEach(id -> {
                val json = new ObjectMapper().createObjectNode();
                json.put("event", "unsubscribe");
                json.put("channelID", id);
                try {
                    this.wsSession.sendMessage(new TextMessage(json.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.wsSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ping() {
        try {
            this.wsSession.sendMessage(new TextMessage("{\"event\":\"ping\"}"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connectSession() {
        var sessionStarted = this.wsSession.isOpen();
        if (!sessionStarted) {
            sessionStarted = this.connect();
        }
        return sessionStarted;
    }
}