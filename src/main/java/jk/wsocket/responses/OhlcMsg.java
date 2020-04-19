package jk.wsocket.responses;

import lombok.Value;
import lombok.val;

import java.util.List;

@Value
public class OhlcMsg {

    private int channelID;
    private double time;
    private double etime;
    private double open;
    private double high;
    private double low;
    private double close;
    private double vwap;
    private double volume;
    private double count;
    private String channelName;
    private String pair;

    public static OhlcMsg fromMap(List jsonList) {
        val values = ((List<Object>) jsonList.get(1)).stream().map(v -> Double.parseDouble(v.toString())).toArray(Double[]::new);
        val ohlcMsg = new OhlcMsg((int) jsonList.get(0), values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8],
                (String) jsonList.get(2), (String) jsonList.get(3));
        return ohlcMsg;
    }
}