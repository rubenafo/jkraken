package jk.rest.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class OHLCValues {

    private List<String> error;
    private HashMap<String, List<Double>> ohlcs; // array of array entries(<time>, <open>, <high>, <low>, <close>, <vwap>, <volume>, <count>)
    private int lastValue; // id to be used as since when polling for new, committed OHLC data

    @JsonCreator
    public OHLCValues (@JsonProperty("error")  List<String> error, @JsonProperty("result") Map<String, Object> result) {
        this.error = error;
        ohlcs = new HashMap<>();
        result.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("last"))
                .forEach(entry -> {
                    var rawlist = (List<Double>) entry.getValue();
                    ohlcs.put(entry.getKey(), rawlist);
                });
        this.lastValue = (int) result.get("last");
    }
}
