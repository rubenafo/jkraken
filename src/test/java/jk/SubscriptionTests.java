package jk;

import jk.wsocket.KrakenWebSocketService;
import jk.wsocket.PrivateTokenService;
import lombok.val;
import lombok.var;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests the different types of subscriptions
 */
public class SubscriptionTests {

    @Test
    public void subscribed_ownTrades() {
        val input = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        var kservice = new KrakenWebSocketService(mock(PrivateTokenService.class));
        kservice.handleTextMessage(null, new TextMessage(input));
        val ownTrades = kservice.getSessionData().findByName("ownTrades");
        assertNotNull(ownTrades);
        assertEquals("subscribed", ownTrades.getStatus());
    }

    @Test
    public void subscribed_ownTrades_twice () {
        val input = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        var kservice = new KrakenWebSocketService(mock(PrivateTokenService.class));
        kservice.handleTextMessage(null, new TextMessage(input));
        val input2 = "{\"channelName\":\"ownTrades\",\"event\":\"subscriptionStatus\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ownTrades\"}}";
        kservice.handleTextMessage(null, new TextMessage(input));
    }
}
