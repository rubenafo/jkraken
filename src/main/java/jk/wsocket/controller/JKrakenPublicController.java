package jk.wsocket.controller;

import jk.rest.api.KrakenRestService;
import jk.wsocket.data.JsonUtils;
import jk.wsocket.service.KrakenWsService;
import jk.wsocket.data.RequestValidator;
import lombok.NonNull;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class JKrakenPublicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JKrakenPublicController.class);
    private final KrakenWsService krakenWs;
    private final KrakenRestService krakenRest;

    public JKrakenPublicController(@NonNull KrakenWsService krakenWs, @NonNull KrakenRestService krakenRest) {
        this.krakenWs = krakenWs;
        this.krakenRest = krakenRest;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<String> postSubscribe (
            @RequestParam (required = false) List<String> pairs,
            @RequestParam(defaultValue = "5") int interval,
            @RequestParam(defaultValue = "10") int depth,
            @RequestParam String name) {
        LOGGER.debug("subscribe");
        try {
            RequestValidator.validateSubscription(pairs, interval, depth, name);
            this.krakenWs.subscribe(pairs, interval, depth, name);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("{error:\"" + e.getMessage() + "\"}");
        }
        return ResponseEntity.ok("{\"msg\": \"subscribed\"}");
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe (@RequestParam List<String> pairs) {
        LOGGER.debug("unsubscribe");
        this.krakenWs.unsubscribe(pairs);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> status () {
        return ResponseEntity.ok(this.krakenWs.getStatusInfo());
    }

    @PostMapping("/connect")
    public ResponseEntity<String> connect () {
        if (this.krakenWs.isConnected()) {
            return ResponseEntity.ok("{\"msg\": \"server already connected\"}");
        }
        else {
            val success = this.krakenWs.connect();
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

    @GetMapping (path = {"/tickers", "/tickers/{channelName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> tickers (
            @PathVariable (value = "channelName", required = false) String channelName) {
        val tickerData = this.krakenWs.getTickerData(Optional.ofNullable(channelName));
        return ResponseEntity.ok(JsonUtils.toJson(tickerData));
    }

    @GetMapping (path = {"/ohlc", "/ohlc/{channelName}"})
    public ResponseEntity<String> ohlc (
            @PathVariable (value = "channelName", required = false) String channelName) {
        val ohlcData = this.krakenWs.getOhlcData(Optional.ofNullable(channelName));
        return ResponseEntity.ok(JsonUtils.toJson(ohlcData));
    }

    @GetMapping(value = "/pairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pairs (
    ) {
        return ResponseEntity.ok(KrakenRestService.getAssetPairs().toString());
    }
}
