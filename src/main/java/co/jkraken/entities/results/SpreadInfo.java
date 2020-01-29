package co.jkraken.entities.results;

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
public class SpreadInfo {

    private final long lastTradeId;
    private List<String> errors;
    private Map<String, List<SpreadItem>> result;

    public SpreadInfo (@JsonProperty("error") List<String> errors, @JsonProperty("result") Map<String, Object> result) {
        this.errors = errors;
        this.lastTradeId = Long.valueOf(result.get("last").toString());
        this.result = new HashMap<>();
        result.entrySet().stream().filter(it -> !it.getKey().equals("last"))
                .forEach(elem -> {
                    var values = (List<List<Object>>) elem.getValue();
                    var items = values.stream().map(e -> new SpreadItem(
                            Long.valueOf(e.get(0).toString()),
                            new BigDecimal(e.get(1).toString()),
                            new BigDecimal(e.get(2).toString())))
                            .collect(Collectors.toList());
                    this.result.put(elem.getKey(), items);
                });
    }

    @Data
    @AllArgsConstructor
    class SpreadItem {

        private long time;
        private BigDecimal bid;
        private BigDecimal ask;
    }

}
