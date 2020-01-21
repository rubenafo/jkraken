package com.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.var;

import java.util.List;
import java.util.Map;

@Data
public class TradesHistoryInfo {

    private List<String> error;
    private Map<String, TradesHistoryItem> result;

    @JsonCreator
    public TradesHistoryInfo (@JsonProperty("error") List<String> error, @JsonProperty("result") Map<String, Object> result) {
        this.error = error;
        var items = (Map<String, Object>) result.get("trades");
    }
}
