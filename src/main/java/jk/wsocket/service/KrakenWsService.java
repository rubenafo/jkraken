package jk.wsocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jk.rest.tools.LocalPropLoader;
import jk.wsocket.data.RequestValidator;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tech.tablesaw.api.Table;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class KrakenWsService extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KrakenWsService.class);
    private final PrivateTokenService tokenService;
    private final LocalPropLoaderService propsService;
    private final KrakenWsHandler authClient;
    private final KrakenWsHandler publicClient;
    private final boolean publicMode;

    public KrakenWsService(
            @NonNull PrivateTokenService tokenService,
            @NonNull LocalPropLoaderService propsService) {
        this.tokenService = tokenService;
        this.propsService = propsService;
        this.authClient = new KrakenWsHandler("privateEndpoint", propsService.getPrivateAPI().get());
        this.publicClient = new KrakenWsHandler("publicEndpoint",propsService.getPublicAPI().get());
        this.publicMode = new LocalPropLoader().keysFound();
    }

    public void subscribe(List<String> pairs, int interval, int depth, String name) {
        boolean isPrivate = name.equalsIgnoreCase("ownTrades") || name.equalsIgnoreCase("openOrders");
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "subscribe");
        val reqId = Math.abs(new Random().nextInt());
        json.put("reqid", reqId);
        if (pairs != null)
            json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(pairs));
        val subscription = json.putObject("subscription");
        subscription.put("name",name);
        subscription.put("interval", interval);
        subscription.put("depth", depth);
        if (isPrivate) {
            subscription.put("token", tokenService.getToken());
            RequestValidator.assertConnected(this.authClient);
            this.authClient.sendAndConfirm(json.toString(), reqId);
        }
        else {
            RequestValidator.assertConnected(this.publicClient);
            this.publicClient.sendAndConfirm(json.toString(), reqId);
        }
    }

    public void unsubscribe(List<String> channelIds) {
        var json = new ObjectMapper().createObjectNode();
        json.put("event", "unsubscribe");
        json.putArray("pair").addAll((ArrayNode) new ObjectMapper().valueToTree(channelIds));
        json.putObject("subscription").put("name","ticker");
        this.publicClient.sendMessage(json.toString());
        this.authClient.sendMessage(json.toString());
    }

    public void close() {
        this.authClient.close();
        this.publicClient.close();
    }

    public String getStatusInfo() {
        var json = new ObjectMapper().createObjectNode();
        json.put("publicEndpoint", this.publicClient.connected());
        json.put("privateEndpoint", this.authClient.connected());
        val channels = json.putArray("publicChannels");
        this.publicClient.getChannels().forEach(ch -> {
            channels.addObject().put("channelID", ch.getChannelID()).put("channelName", ch.getChannelName());
        });
        val privateChannels = json.putArray("privateChannels");
        this.authClient.getChannels().forEach(ch -> {
            privateChannels.addObject().put("channelID", ch.getChannelID()).put("channelName", ch.getChannelName());
        });
        return json.toString();
    }

    public boolean isConnected() {
        return this.publicMode ? this.publicClient.connected() : this.publicClient.connected() & this.authClient.connected();
    }

    public boolean connect () {
        val publicCon = this.publicClient.connected() ? this.publicClient.connected() : this.publicClient.connect();
        val authCon = this.authClient.connected() ? this.authClient.connected () : this.authClient.connect();
        return publicCon & authCon;
    }

    public Table getTickerData(Optional<String> channelName) {
        return this.publicClient.getTickerData(channelName);
    }

    public Table getOhlcData(Optional<String> channelName) {
        return this.publicClient.getOHLCData(channelName);
    }

    public Map getOpenOrders() {
        return this.authClient.getOpenOrders();
    }

    public Map getOwnTrades() {
        return this.authClient.getOwnTrades();
    }
}