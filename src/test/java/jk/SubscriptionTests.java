package jk;

import jk.wsocket.service.KrakenHandler;
import jk.wsocket.service.KrakenWsService;
import jk.wsocket.service.PrivateTokenService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests the different types of subscriptions
 */
public class SubscriptionTests {

    private KrakenHandler khandler;
    private WebSocketSession session;

    @BeforeEach
    public void setup() {
        this.khandler = new KrakenHandler("id", "url");
        this.session = mock(WebSocketSession.class);
    }

    @Test
    public void subscribed_ownTrades() {
        val input = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(input));
        val ownTrades = khandler.getChannels().stream().filter(ch -> ch.getSubscriptionName().equalsIgnoreCase("ownTrades")).findFirst().get();
        assertNotNull(ownTrades);
        assertEquals("subscribed", ownTrades.getStatus());
    }

    @Test
    public void subscribed_ownTrades_twice () {
        val input = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        khandler.handleTextMessage(this.session, new TextMessage(input));
        val input2 = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        khandler.handleTextMessage(null, new TextMessage(input2));
        val ownTrades = khandler.getChannels().stream().filter(ch -> ch.getSubscriptionName().equalsIgnoreCase("ownTrades")).findFirst().get();
        assertNotNull(ownTrades);
        assertEquals("subscribed", ownTrades.getStatus());
    }

    @Test
    public void subscribed_open_orders () throws IOException {
        val input = Files.readAllBytes(Paths.get("src", "test", "java", "resources", "openOrders.json"));
        khandler.handleTextMessage(this.session, new TextMessage(input));
        val orders = khandler.getOpenOrders();
        assertEquals(1, orders.size());
    }
}
