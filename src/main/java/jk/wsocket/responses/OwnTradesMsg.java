package jk.wsocket.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
public class OwnTradesMsg {

    private Map<String, OwnTrade> ownTrades;

    @JsonCreator
    public OwnTradesMsg (Object rawObject) {
        this.ownTrades = new HashMap<String, OwnTrade>();
        val tradesInfo = (List) ((List) rawObject).get(0);
        tradesInfo.stream().forEach(rawMap -> {
            val tradeKey = (String) ((Map) rawMap).keySet().iterator().next();
            val rawTrade = ((Map) rawMap).get(tradeKey);
            try {
                val mapper = new ObjectMapper();
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                val ownTrade = mapper.readValue(mapper.writeValueAsBytes(rawTrade), OwnTrade.class);
                ownTrades.put(tradeKey, ownTrade);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
