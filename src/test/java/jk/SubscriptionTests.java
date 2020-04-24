package jk;

import jk.wsocket.KrakenWebSocketService;
import jk.wsocket.PrivateTokenService;
import lombok.val;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Tests the different types of subscriptions
 */
public class SubscriptionTests {

    private KrakenWebSocketService kservice;
    private WebSocketSession session;

    @BeforeEach
    public void setup() {
        this.kservice = new KrakenWebSocketService(mock(PrivateTokenService.class));
        this.session = mock(WebSocketSession.class);
    }

    @Test
    public void subscribed_ownTrades() {
        val input = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        kservice.handleTextMessage(this.session, new TextMessage(input));
        val ownTrades = kservice.getSessionData().findByName("ownTrades");
        assertNotNull(ownTrades);
        assertEquals("subscribed", ownTrades.getStatus());
    }

    @Test
    public void subscribed_ownTrades_twice () {
        val input = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        kservice.handleTextMessage(this.session, new TextMessage(input));
        val input2 = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        kservice.handleTextMessage(null, new TextMessage(input2));
        val ownTrades = kservice.getSessionData().findByName("ownTrades");
        assertNotNull(ownTrades);
        assertEquals("subscribed", ownTrades.getStatus());
    }

    @Test
    public void subscribed_open_orders () throws IOException {
        val input = Files.readAllBytes(Paths.get("src", "test", "java", "resources", "openOrders.json"));
        kservice.handleTextMessage(this.session, new TextMessage(input));
    }
}
