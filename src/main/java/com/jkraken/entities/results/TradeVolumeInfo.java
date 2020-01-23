package com.jkraken.entities.results;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TradeVolumeInfo {

    private List<String> error;
    private Map<String,Object> result;
}
