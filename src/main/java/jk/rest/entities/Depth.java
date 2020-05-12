package jk.rest.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Depth {

    private String pair;
    List<double[]> asks; // <price>, <volume>, <timestamp>
    List<double[]> bids; // <price>, <volume>, <timestamp>

    @JsonCreator
    public Depth (@JsonProperty("result") Map<String, Map<String,Object>> result) {
        var pair = result.entrySet().stream().findFirst().get();
        this.pair = pair.getKey();
        var lists = pair.getValue();
        var asks = (List) lists.get("asks");
        var a = asks.stream().map(l -> {
          var list = (List<Object>) l;
          var values = list.stream().mapToDouble(i -> Double.parseDouble(i.toString())).boxed().collect(Collectors.toList());
          return values;
        }).collect(Collectors.toList());
        this.asks = (List<double[]>) a;
        var bids = (List) lists.get("bids");
        var b = bids.stream().map(l -> {
            var list = (List<Object>) l;
            var values = list.stream().mapToDouble(i -> Double.parseDouble(i.toString())).boxed().collect(Collectors.toList());
            return values;
        }).collect(Collectors.toList());
        this.bids = (List<double[]>) b;
    }
}
