package com.jkraken.entities.results;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerDateInfo {

    private Map<String, Object> result;
}
