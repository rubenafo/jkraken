package jk.wsocket.responses;

import lombok.Value;

@Value
public class OhlcMsg {

    private int channelID;
    private String channelName;
    private String pair;
    private double time;
    private double etime;
    private double open;
    private double high;
    private double low;
    private double close;
    private double vwap;
    private double volume;
    private double count;
}