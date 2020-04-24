package jk.wsocket.responses;

import lombok.Data;

import java.util.Map;

@Data
public class OpenOrder {

    private double cost;
    private double expireTm;
    private double limitPrice;
    private String misc;
    private String oFlags;
    private double openTm;
    private double price;
    private String refId;
    private double startTm;
    private String status;
    private double stopPrice;
    private int userref;
    private double vol;
    private double fee;
    private double vol_exec;
    private Map<String, Object> descr; // descr -> close -> {leverage,order, orderType, pair, price, price2, type}
}
