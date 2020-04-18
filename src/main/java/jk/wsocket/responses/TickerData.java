package jk.wsocket.responses;

import com.google.common.collect.Lists;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class TickerData {

    private int channelId;
    private String channelName;
    private String pair;

    /**
     * askPrice, askWholeVolume, askVolume,
     * bidPrice, bidWholeVolume, bidVolume,
     * closePrice, closeVolume,
     * volumeToday, volume24h,
     * volWeightedAvgPriceToday, volWeightedAvgPrice24h,
     * tradesToday, trades24h,
     * lowPice, lowPrice24h,
     * highPrice, highVolume,
     * openPrice, openPrice24h
     */
    private List<Double> values;

    public static TickerData fromMap(int channelId, String name, String pair, Map data) {
        List<Double> values = Lists.newArrayList();
        ((List<Object>) data.get("a")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("b")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("c")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("v")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("p")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("t")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("l")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("h")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        ((List<Object>) data.get("o")).stream().forEach(v -> values.add(Double.parseDouble(v.toString())));
        return new TickerData(channelId, name, pair, values);
    }
}
