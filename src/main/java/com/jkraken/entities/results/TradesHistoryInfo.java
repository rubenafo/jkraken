package com.jkraken.entities.results;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TradesHistoryInfo {

    private List<String> error;
    private TradeHistoryContent result;

    @Data
    public class TradeHistoryContent {

        private Map<String, TradesHistoryItem> trades;
        private double count;
    }

}
