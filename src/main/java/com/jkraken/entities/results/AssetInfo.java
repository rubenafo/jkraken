package com.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jkraken.entities.AssetItem;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetInfo {

    private Map<String, AssetItem> result;
}
