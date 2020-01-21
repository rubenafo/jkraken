package com.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TradesHistoryItem {

    @JsonProperty("ordertxid")
    private String orderTxId;

    @JsonProperty("postxid")
    private String posTxId;

    private String pair;
    private long time;
    private String type;
    private String orderType;
    private BigDecimal price;
    private BigDecimal cost;
    private BigDecimal fee;
    private double volume;
    private double margin;
    private List<String> misc;
}
