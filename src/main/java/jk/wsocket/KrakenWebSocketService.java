package jk.wsocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Service
public class KrakenWebSocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWebSocketService.class);
    private KrakenSession session;

    @PostConstruct
    protected boolean connect() {
        try {
            var webSocketClient = new StandardWebSocketClient();
            var wsSession = webSocketClient.doHandshake(new KrakenWebSocketHandler(this), new WebSocketHttpHeaders(), URI.create("wss://ws.kraken.com")).get();
            this.session = new KrakenSession(wsSession);
            return this.session.getSession().isOpen();
        } catch (Exception e) {
            LOGGER.error("Exception while accessing websockets", e);
        }
        return false;
    }

    public KrakenSession getSession () {
        return this.session;
    }

    public void subscribe(List<String> pairs, int interval, int depth, String name) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "subscribe");
        json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(pairs));
        json.putObject("subscription").put("name",name);
        System.out.println(json.toString());
        try {
            this.session.getSession().sendMessage(new TextMessage(json.toString()));
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

    public boolean connectSession() {
        var sessionStarted = this.session.getSession().isOpen();
        if (!sessionStarted) {
            sessionStarted = this.connect();
        }
        return sessionStarted;
    }
}