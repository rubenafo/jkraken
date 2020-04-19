package jk;

import jk.app.JsonUtils;
import jk.wsocket.KrakenWebSocketService;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;

public class KrakenSessionTest {

    @Test
    public void simple_test () {
        var input = "{\"channelID\":274,\"channelName\":\"ticker\",\"event\":\"subscriptionStatus\",\"pair\":\"XBT/USD\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ticker\"}}";
        var session = new KrakenWebSocketService();
        session.handleTextMessage(null, new TextMessage(input));
    }

    @Test
    public void session_receives_ticker_data() {
        var input = "[274,{\"a\":[\"6804.10000\",3,\"3.98560000\"],\"b\":[\"6801.00000\",1,\"1.34500000\"],\"c\":[\"6804.10000\",\"0.01440000\"],\"v\":[\"3081.29016909\",\"4852.51773585\"],\"p\":[\"6884.69236\",\"6890.16503\"],\"t\":[9843,16235],\"l\":[\"6789.60000\",\"6789.60000\"],\"h\":[\"6960.00000\",\"6960.00000\"],\"o\":[\"6873.70000\",\"6869.10000\"]},\"ticker\",\"XBT/USD\"]";
        var session = new KrakenWebSocketService();
        session.handleTextMessage(null, new TextMessage(input));
        System.out.println(JsonUtils.toJson(session.getSessionData().getTickerData()));
    }

    @Test
    public void json_export () {
        var input = "{\"channelID\":274,\"channelName\":\"ticker\",\"event\":\"subscriptionStatus\",\"pair\":\"XBT/USD\",\"status\":\"subscribed\",\"subscription\":{\"name\":\"ticker\"}}";
        var session = new KrakenWebSocketService();
        session.handleTextMessage(null, new TextMessage(input));
        System.out.println(JsonUtils.toJson(session.getSessionData().getSubscriptionData()));
    }
}
