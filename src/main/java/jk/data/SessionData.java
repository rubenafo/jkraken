package jk.data;


import jk.wsocket.responses.TickerData;
import lombok.val;
import tech.tablesaw.api.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * This class contains all the session's data, organized by channelId
 */
public class SessionData {

    private final Table subscriptionData;
    private final Table tickerData;

    public SessionData () {
        this.subscriptionData = Table.create("subscriptionData").addColumns(
                IntColumn.create("channelId"),
                StringColumn.create("channelName"),
                StringColumn.create("event"),
                StringColumn.create("pair"),
                StringColumn.create("status"));

        this.tickerData = Table.create("tickerData").addColumns(IntColumn.create("channelId"))
                .addColumns(StringColumn.create("channelName"))
                .addColumns(StringColumn.create("pair"));
        String[] columns = {"askPrice", "askWholeVolume", "askVolume", "bidPrice", "bidWholeVolume", "bidVolume", "closePrice", "closeVolume",
        "volumeToday", "volume24h", "volWeightedAvgPriceToday", "volWeightedAvgPrice24h", "tradesToday", "trades24h", "highPrice", "highVolume",
                "lowPrice","lowPrice24h", "openPrice", "openPrice24h"};
        for (val col: columns) {
            this.tickerData.addColumns(DoubleColumn.create(col));
        }
    }

    public void append(TickerData tickerRow) {
        val row = tickerData.appendRow();
        row.setInt(0, tickerRow.getChannelId());
        row.setString(1, tickerRow.getChannelName());
        row.setString(2, tickerRow.getPair());
        IntStream.range(0, tickerRow.getValues().size()).forEach(i -> row.setDouble(i+3, tickerRow.getValues().get(i)));
    }
}
