package jk.data;


import jk.wsocket.responses.SubscriptionStatusMsg;
import jk.wsocket.responses.TickerMsg;
import lombok.Getter;
import lombok.val;
import lombok.var;
import tech.tablesaw.api.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * This class contains all the session's data, organized by channelId
 */
@Getter
public class SessionData {

    private static final DateTimeFormatter formatter = ISO_INSTANT;
    private Map<Integer, SubscriptionStatusMsg> channels;
    private final Table tickerData;

    public SessionData () {
        this.channels = new HashMap<>();
        this.tickerData = Table.create("tickerData")
                .addColumns(IntColumn.create("channelID"))
                .addColumns(StringColumn.create("channelName"))
                .addColumns(StringColumn.create("pair"))
                .addColumns(LongColumn.create("ts"));
        String[] columns = {"askPrice", "askWholeVolume", "askVolume", "bidPrice", "bidWholeVolume", "bidVolume", "closePrice", "closeVolume",
        "volumeToday", "volume24h", "volWeightedAvgPriceToday", "volWeightedAvgPrice24h", "tradesToday", "trades24h", "highPrice", "highVolume",
                "lowPrice","lowPrice24h", "openPrice", "openPrice24h"};
        for (val col: columns) {
            this.tickerData.addColumns(DoubleColumn.create(col));
        }
    }

    public void append(TickerMsg tickerRow, long timestamp) {
        val row = tickerData.appendRow();
        row.setInt(0, tickerRow.getChannelId());
        row.setString(1, tickerRow.getChannelName());
        row.setString(2, tickerRow.getPair());
        row.setLong(3, timestamp);
        IntStream.range(0, tickerRow.getValues().size()).forEach(i -> row.setDouble(i+4, tickerRow.getValues().get(i)));
    }

    public Table getTickerData(Optional<String> channelName) {
        var filteredTable = tickerData;
        if (channelName.isPresent()) {
            val id = channelName.get();
            var isInteger = false;
            var channelID = 0;
            try {
                channelID = Integer.parseInt(id);
                isInteger = true;
            } catch (NumberFormatException ex) {
                // do nothing
            }
            if (!isInteger) {
                filteredTable = tickerData.where(tickerData.stringColumn("channelName").isEqualTo(id));
            } else { // integer
                filteredTable = tickerData.where(tickerData.stringColumn("channelName").isEqualTo(id)
                        .or(tickerData.intColumn("channelID").isEqualTo(channelID)));
            }
        }
        return filteredTable;
    }

    public Set<Integer> getChannelIDs () {
        return this.channels.keySet();
    }

    public void subscribe (SubscriptionStatusMsg msg) {
        this.channels.put(msg.getChannelID(), msg);
    }
}
