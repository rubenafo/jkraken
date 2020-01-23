package com.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.var;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class RecentTradesInfo {

    @Data
    @AllArgsConstructor
    public class RecentTradesInfoElem {
        private BigDecimal price;
        private double volume;
        private double time;
        private String buySell;  // B | S
        private String marketLimit;  // M | L
    }

    private List<String> error;
    private Map<String, List<RecentTradesInfoElem>> result;
    private String lastTradeId;

    public RecentTradesInfo (@JsonProperty("error") List<String> error, @JsonProperty("result") Map<String, Object> result) {
        this.error = error;
        this.lastTradeId = (String) result.get("last");
        this.result = new HashMap<>();
        result.entrySet().stream().filter(it -> !it.getKey().equals("last"))
                .forEach(it -> {
                    var values = (List<List<Object>>) it.getValue();
                    var items = values.stream().map(e -> new RecentTradesInfoElem(
                            new BigDecimal((String) e.get(0)),
                            Double.valueOf((String) e.get(1)),
                            (double) e.get(2),
                            (String) e.get(3),
                            (String) e.get(4)))
                            .collect(Collectors.toList());
                    this.result.put(it.getKey(), items);
                });
    }
}
