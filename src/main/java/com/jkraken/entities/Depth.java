package com.jkraken.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.var;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Depth {

    List<double[]> asks; // <price>, <volume>, <timestamp>
    List<double[]> bids; // <price>, <volume>, <timestamp>

    public Depth (@JsonProperty("result") Map<String, Object> result) {
        var askList = (List<List<Object>>) result.get("asks");
        asks = new ArrayList<>();
        askList.forEach( i -> {
            double[] a = {(double) i.get(0), (double) i.get(1), (double) i.get(2)};
        });

        var bidMap = result.get("bid");

    }
}
