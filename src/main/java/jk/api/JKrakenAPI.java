package jk.api;

import jk.wsocket.KrakenWebSocketService;
import lombok.NonNull;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class JKrakenAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(JKrakenAPI.class);
    private final KrakenWebSocketService krakenWs;

    public JKrakenAPI (@NonNull KrakenWebSocketService krakenWs) {
        this.krakenWs = krakenWs;
    }

    @GetMapping ("/subscribe")
    public ResponseEntity<String> get (
            @RequestParam List<String> pairs,
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

    @GetMapping("/ping")
    public ResponseEntity<String> ping () {
        this.krakenWs.ping();
        return ResponseEntity.ok("");
    }

    @GetMapping ("/stop")
    public ResponseEntity<String> stop () {
        this.krakenWs.stop();
        return ResponseEntity.ok("");
    }
}