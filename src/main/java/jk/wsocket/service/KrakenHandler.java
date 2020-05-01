package jk.wsocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jk.data.SessionData;
import jk.wsocket.responses.*;
import lombok.val;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KrakenHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenHandler.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private WebSocketSession clientSession;
    private final SessionData sessionData;
    private final String id;
    private final String url;

    public KrakenHandler(String clientId, String url) {
        this.sessionData = new SessionData();
        this.id = clientId;
        this.url = url;
    }

    public void sendMessage(String msg) {
        try {
            this.clientSession.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean connect() {
        try {
            var webSocketClient = new StandardWebSocketClient();
            this.clientSession = webSocketClient.doHandshake(this, new WebSocketHttpHeaders(), URI.create(url)).get();
            return true;
        } catch (Exception e) {
            LOGGER.error("Exception while creating websockets", e);
        }
        return false;
    }

    public void close () {
        try {
            this.clientSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("Kraken ws: connection closed " + session.getUri());
        this.sessionData.clearChannels();
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
                        val status = ((Map) obj).get("status");
                        if (status.equals("error")) {
                            LOGGER.debug("Error: " + ((Map) (obj)).get("errorMessage"));
                        }
                        else {
                            val subscribeMsg = this.jsonMapper.readValue(rawMsg, SubscriptionStatusMsg.class);
                            this.sessionData.updateSubscription(subscribeMsg);
                        }
                        break;
                    default:
                        LOGGER.debug("Error: ignoring unknown event type received={}", eventType);
                }
            } else if (obj instanceof List) {
                val jsonList = (List) obj;
                if (jsonList.get(0) instanceof List) {
                    val type = (String) jsonList.get(1);
                    switch (type) {
                        case "ownTrades":
                            val ownTradesMsg = this.jsonMapper.readValue(rawMsg, OwnTradesMsg.class);
                            this.sessionData.setOwnTrades(ownTradesMsg.getOwnTrades());
                            break;
                        case "openOrders":
                            val openOrders = this.jsonMapper.readValue(rawMsg, OpenOrdersMsg.class);
                            this.sessionData.addOpenOrders(openOrders);
                            break;
                    }
                }
                else {
                    int channelId = (int) jsonList.get(0);
                    val channelName = (String) jsonList.get(2);
                    val pair = (String) jsonList.get(3);
                    val channelType = this.sessionData.getSubscribedChannels().get(channelId).getSubscriptionName();
                    switch (channelType) {
                        case "ticker":
                            val tickerMsg = TickerMsg.fromMap(channelId, channelName, pair, (Map) jsonList.get(1));
                            this.sessionData.append(tickerMsg, System.currentTimeMillis());
                            break;
                        case "ohlc":
                            val ohlcMsg = OhlcMsg.fromMap(jsonList);
                            this.sessionData.append(ohlcMsg);
                            break;
                    }
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

    public Optional<SubscriptionStatusMsg> subscribed (int channelId) {
        return Optional.ofNullable(this.sessionData.getSubscribedChannels().get(channelId));
    }

    public boolean connected () {
        return this.clientSession == null ? false : this.clientSession.isOpen();
    }

    public List<SubscriptionStatusMsg> getChannels () {
        return new ArrayList<>(this.sessionData.getSubscribedChannels().values());
    }

    public Table getTickerData(Optional<String> channelName) {
        return this.sessionData.getTickerData(channelName);
    }

    public Table getTickerData () {
        return this.getTickerData(Optional.empty());
    }

    public Table getOHLCData(Optional<String> channelName) {
        return this.sessionData.getOhlcData(channelName);
    }

    public Map<String, OpenOrder>  getOpenOrders() {
        return this.sessionData.getOpenOrders();
    }

    public Map getOwnTrades () {
        return this.sessionData.getOwnTrades();
    }
}
