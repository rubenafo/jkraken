package jk.wsocket.responses;

import lombok.Data;

@Data
public class OwnTrade {

    private String orderTxId; // order responsible for execution of trade
    private String postxId; // Position trade id
    private String pair;
    private double time;
    private String type; // type of order , buy/sell
    private String orderType; // limit
    private double price; // average price order was executed at (quote currency)
    private double cost; // total cost of order, quote currency
    private double fee; // total fee (quote currency)
    private double volume; // volume (base currency)
    private double margin; // initial margin (quote currency)
    private double vol; // volume (base currency)
}