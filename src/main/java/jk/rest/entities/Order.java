package jk.rest.entities;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A builder to create params to create an order
 */

@Getter
@ToString
@Builder
@AllArgsConstructor
public class Order {

    public enum Type  {
        BUY("buy"), SELL("sell");

        private String type;
        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return super.toString();
        }
    }

    public enum OrderType {
        MARKET("market"),
        LIMIT ("limit"),
        STOP_LOSS ("stop-loss"),
        TAKE_PROFIT ("take-profit"),
        STOP_LOSS_PROFIT ("stop-loss-profit"),
        STOP_LOSS_PROFIT_LIMIT ("stop-loss-profit-limit"),
        STOP_LOSS_LIMIT ("stop-loss-limit"),
        TAKE_PROFIT_LIMIT ("take-profit-limit"),
        TRAILING_STOP ("trailing-stop"),
        TRAILING_STOP_LIMIT ("trailing-stop-limit"),
        STOP_LOSS_AND_LIMIT ("stop-loss-and-limit"),
        SETTLE_POS ("settle-position");

        private String type;

        OrderType(String type) {
            this.type = type;
        }
    };

    private AssetPairsEnum.AssetPairs pair;
    private String type; // buy | sell
    private OrderType ordertype;
    private BigDecimal price;
    private BigDecimal price2; // optional
    private double volume;
    private double leverage; // optional
    private long starttm; // long scheduled start time, timestamp, +n seconds from now, optional
    private long expiretm; // long scheduled end time, timestamp, +n seconds from now, optional
    private int userRef; // optional
    private boolean validate; // optional

    private Order() {
        this.leverage = 0;
        this.starttm = 0l;
        this.expiretm = 0l;
        this.leverage = 0;
    }

    public Map<String, Object> asMap () {
        var result = new HashMap<String, Object>();
        result.put("pair", this.pair.altName);
        result.put("type", this.type);
        result.put("ordertype", ordertype.type);
        result.put("price", String.valueOf(this.price.longValue()));
        if (price2 != null) {
            result.put("prices", String.valueOf(this.price.longValue()));
        }
        result.put("volume", String.valueOf(this.volume));
        if (this.leverage != 0) {
            result.put("leverage", String.valueOf(this.leverage));
        }
        if (this.starttm != 0) {
            result.put("starttm", String.valueOf(this.starttm));
        }
        if (this.expiretm != 0) {
            result.put("expiretm", String.valueOf(this.expiretm));
        }
        if (validate) {
            result.put("validate", "true");
        }
        return result;
    }

    public static Order createSellOrder(AssetPairsEnum.AssetPairs pair, double price, double qty) {
        var order = new OrderBuilder()
                .pair(pair)
                .type(Type.SELL.type)
                .ordertype(OrderType.LIMIT)
                .volume(qty)
                .price(new BigDecimal(price))
                .validate(true)
                .build();
        return order;
    }
}