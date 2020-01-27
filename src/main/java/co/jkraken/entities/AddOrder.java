package co.jkraken.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.var;

import java.math.BigDecimal;

/**
 * A builder to create params to create an order
 */

@Getter
@ToString
@Builder
public class AddOrder {

    public enum ORDERTYPE  {
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

        ORDERTYPE(String type) {
            this.type = type;
        }
    };

    private AssetPairsEnum.AssetPairs pair;
    private String type; // buy | sell
    private ORDERTYPE ordertype;
    private BigDecimal price;
    private BigDecimal price2; // optional
    private double volume;
    private double leverage; // optional
    private String starttm; // long scheduled start time, timestamp, +n seconds from now, optional
    private String expiretm; // long scheduled end time, timestamp, +n seconds from now, optional
    private int userRef; // optional
    private boolean validate; // optional

    private AddOrder () {
        this.leverage = 0;
        this.starttm = "0";
        this.expiretm = "0";
    }

    public static AddOrder createSell(AssetPairsEnum.AssetPairs pair, ORDERTYPE orderType, double qty) {
        var order = new AddOrderBuilder()
                .type("sell")
                .pair(pair)
                .ordertype(orderType)
                .volume(qty)
                .build();
        return order;
    }
}
