package jk.data;


import lombok.val;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

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
        this.tickerData = Table.create("market").addColumns(IntColumn.create("channelId"), StringColumn.create("channelName","pair"));
        String[] columns = {"askPrice", "askWholeVolume", "askVolume", "bidPrice", "bidWholeVolume", "bidVolume", "closePrice", "closeVolume",
        "volumeToday", "volume24h", "volWeightedAvgPriceToday", "volWeightedAvgPrice24h", "tradesToday", "trades24h", "highPrice", "highVolume",
                "lowPrice","lowPrice24h", "openPrice", "openPrice24h"};
        for (val col: columns) {
            this.tickerData.addColumns(FloatColumn.create(col));
        }
    }
}
