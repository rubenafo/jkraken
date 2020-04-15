package jk.wsocket.responses;

import lombok.Data;

import java.util.List;

@Data
public class TickerData {

    private List<Double> a; // askPrice, askWholeVolume, askVolume
    private List<Double> b; // bidPrice, bidWholeVolume, bidVolume
    private List<Double> c; // closePrice, closeVolume
    private List<Double> v; // volumeToday, volume24h
    private List<Double> p; // volWeightedAvgPriceToday, volWeightedAvgPrice24h
    private List<Double> t; // tradesToday, trades24h
    private List<Double> l; // lowPice, lowPrice24h
    private List<Double> h; // highPrice, highVolume
    private List<Double> o; // openPrice, openPrice24h
}
