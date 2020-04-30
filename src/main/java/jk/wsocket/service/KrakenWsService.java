package jk.wsocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

@Service
public class KrakenWsService extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWsService.class);
    private final PrivateTokenService tokenService;
    private KrakenWsClient authClient;
    private KrakenWsClient publicClient;

    public KrakenWsService(@NonNull PrivateTokenService tokenService) {
        this.tokenService = tokenService;
        this.authClient = new KrakenWsClient("auth");
        this.publicClient = new KrakenWsClient("pub");
    }

    public void subscribe(List<String> pairs, int interval, int depth, String name) {
        boolean isPrivate = name.equalsIgnoreCase("ownTrades") || name.equalsIgnoreCase("openOrders");
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "subscribe");
        if (pairs != null)
            json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(pairs));
        val subscription = json.putObject("subscription");
        subscription.put("name",name);
        if (isPrivate) {
            subscription.put("token", tokenService.getToken());
            this.authClient.sendMessage(json.toString());
        }
        else {
            this.publicClient.sendMessage(json.toString());
        }
    }

    public void unsubscribe(List<String> channelIds) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "unsubscribe");
        json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(channelIds));
        json.putObject("subscription").put("name","ticker");
        try {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.authClient.close();
        this.publicClient.close();
    }

    public String getStatusInfo() {
        var json = new ObjectMapper().createObjectNode();
        json.put("connected", this.wsAuthSession.isOpen());
      //  json.put("tickerItems", this.sessionData.getTickerData().rowCount());
        val channels = json.putArray("channels");
        this.sessionData.getSubscribedChannels().entrySet().forEach(e -> {
            channels.addObject().put("channelID", e.getKey()).put("channelName", e.getValue().getChannelName());
        });
        return json.toString();
    }

    public boolean connectSession() {
        var sessionStarted = this.wsAuthSession.isOpen();
        if (!sessionStarted) {
            sessionStarted = this.connect();
        }
        return sessionStarted;
    }

    public boolean isConnected() {
        return this.wsAuthSession.isOpen();
    }
}