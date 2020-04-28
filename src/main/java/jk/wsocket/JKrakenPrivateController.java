package jk.wsocket;

import jk.rest.api.KrakenRestService;
import jk.rest.entities.AssetPairsEnum;
import jk.rest.entities.Order;
import jk.rest.entities.results.AccountBalanceInfo;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JKrakenPrivateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JKrakenPublicController.class);
    private final KrakenWebSocketService krakenWs;
    private final KrakenRestService krakenRest;

    public JKrakenPrivateController(@NonNull KrakenWebSocketService krakenWs, @NonNull KrakenRestService krakenRest) {
        this.krakenWs = krakenWs;
        this.krakenRest = krakenRest;
    }

    @PostMapping(path = "/addOrder")
    public ResponseEntity<String> addOrder () {
        return null;
    }

    @PostMapping(path = "/cancelOrder")
    public ResponseEntity<String> cancelOrder() {
        return ResponseEntity.ok("");
    }

    @GetMapping(path = "/orders")
    public ResponseEntity<Object> ownOrders () {
        val orders = this.krakenWs.getSessionData().getOpenOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping(path = "/trades")
    public ResponseEntity<Object> ownTrades () {
        val trades = this.krakenWs.getSessionData().getOwnTrades();
        return ResponseEntity.ok(trades);
    }

    @PostMapping(path = {"/orders/add"})
    public ResponseEntity<Object> addTrade () {
        var sell = Order.createSellOrder(AssetPairsEnum.AssetPairs.XBTUSDC, 44d,20);
        val orderInfo = new KrakenRestService().addOrder(sell);
        return ResponseEntity.ok(orderInfo);
    }

    @GetMapping(path= "/balance")
    public ResponseEntity<AccountBalanceInfo> getBalance () {
        return ResponseEntity.ok(this.krakenRest.getBalance());
    }
}
