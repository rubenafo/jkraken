package jk.wsocket.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenOrdersMsg {

    @Getter
    private Map<String, OpenOrder> ownTrades;

    @JsonCreator
    public OpenOrdersMsg(Object rawObject) {
        this.ownTrades = new HashMap<String, OpenOrder>();
        val tradesInfo = (List) ((List) rawObject).get(0);
        tradesInfo.stream().forEach(rawMap -> {
            val orderKey = (String) ((Map) rawMap).keySet().iterator().next();
            val rawTrade = ((Map) rawMap).get(orderKey);
            try {
                val mapper = new ObjectMapper();
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                val openOrder = mapper.readValue(mapper.writeValueAsBytes(rawTrade), OpenOrder.class);
                ownTrades.put(orderKey, openOrder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
