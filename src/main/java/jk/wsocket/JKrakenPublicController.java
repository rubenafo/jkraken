package jk.wsocket;

import jk.app.JsonUtils;
import jk.rest.api.KrakenRestService;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class JKrakenPublicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JKrakenPublicController.class);
    private final KrakenWebSocketService krakenWs;
    private final KrakenRestService krakenRest;

    public JKrakenPublicController(@NonNull KrakenWebSocketService krakenWs, @NonNull KrakenRestService krakenRest) {
        this.krakenWs = krakenWs;
        this.krakenRest = krakenRest;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> postSubscribe (
            @RequestParam (required = false) List<String> pairs,
            @RequestParam(defaultValue = "5") int interval,
            @RequestParam(defaultValue = "10") int depth,
            @RequestParam(required = true) String name) {
        var depths = Set.of(10, 25, 100, 500, 1000);
        var intervals = Set.of(1|5|15|30|60|240|1440|10080|21600);
        var names = Set.of("ticker","ohlc","trade","book","spread","ownTrades","openOrders");
        LOGGER.debug("subscribe");
        this.krakenWs.subscribe(pairs, interval, depth, name);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/subscribe")
    public ResponseEntity<String> getSubscribe (
            @RequestParam(required = false) Integer subscriptionId) {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe (@RequestParam List<String> pairs) {
        LOGGER.debug("unsubscribe");
        this.krakenWs.unsubscribe(pairs);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<String> ping () {
        return ResponseEntity.ok(this.krakenWs.getStatusInfo());
    }

    @PostMapping("/connect")
    public ResponseEntity<String> connect () {
        if (this.krakenWs.connected()) {
            return ResponseEntity.ok("{\"msg\": \"server already connected\"}");
        }
        else {
            val success = this.krakenWs.connectSession();
            if (success) {
                return ResponseEntity.ok("{\"msg\": \"connected to Kraken\"}");
            } else {
                return ResponseEntity.ok("{\"msg\": \"error connecting to Kraken\"}");
            }
        }
    }

    @PostMapping ("/close")
    public ResponseEntity<String> close () {
        this.krakenWs.close();
        return ResponseEntity.ok("{}");
    }

    @GetMapping (path = {"/tickers", "/tickers/{channelName}"})
    public ResponseEntity<String> tickers (
            @PathVariable (value = "channelName", required = false) String channelName) {
        val tickerData = this.krakenWs.getSessionData().getTickerData(Optional.ofNullable(channelName));
        return ResponseEntity.ok(JsonUtils.toJson(tickerData));
    }

    @GetMapping (path = {"/ohlc", "/ohlc/{channelName}"})
    public ResponseEntity<String> ohlc (
            @PathVariable (value = "channelName", required = false) String channelName) {
        val ohlcData = this.krakenWs.getSessionData().getOhlcData(Optional.ofNullable(channelName));
        return ResponseEntity.ok(JsonUtils.toJson(ohlcData));
    }

}
