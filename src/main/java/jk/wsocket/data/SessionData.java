package jk.wsocket.data;


import jk.wsocket.responses.*;
import lombok.Data;
import lombok.val;
import tech.tablesaw.api.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * This class contains all the session's data, organized by channelId
 */
@Data
public class SessionData {

    private static final int OPEN_ORDERS_CHANNEL = -1;
    private static final int OWN_TRADES_CHANNEL = -2;

    private static final DateTimeFormatter formatter = ISO_INSTANT;
    private Map<Integer, SubscriptionStatusMsg> subscribedChannels;
    private final Table tickerData;
    private final Table ohlcData;
    private Map<String, List<OwnTrade>> ownTrades;

    private Map<String, OpenOrder> openOrders;

    public SessionData () {
       // this.ownTrades = new HashMap<>();
        this.openOrders = new HashMap<>();
        this.subscribedChannels = new HashMap<>();
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

        this.ohlcData = Table.create("ohlcData")
                .addColumns(IntColumn.create("channelID"))
                .addColumns(DoubleColumn.create("time"))
                .addColumns(DoubleColumn.create("etime"))
                .addColumns(DoubleColumn.create("open"))
                .addColumns(DoubleColumn.create("high"))
                .addColumns(DoubleColumn.create("low"))
                .addColumns(DoubleColumn.create("close"))
                .addColumns(DoubleColumn.create("vwap"))
                .addColumns(DoubleColumn.create("volume"))
                .addColumns(DoubleColumn.create("count"))
                .addColumns(StringColumn.create("channelName"))
                .addColumns(StringColumn.create("pair"));
    }

    public void append(TickerMsg tickerRow, long timestamp) {
        val row = tickerData.appendRow();
        row.setInt(0, tickerRow.getChannelId());
        row.setString(1, tickerRow.getChannelName());
        row.setString(2, tickerRow.getPair());
        row.setLong(3, timestamp);
        IntStream.range(0, tickerRow.getValues().size()).forEach(i -> row.setDouble(i+4, tickerRow.getValues().get(i)));
    }

    public void append (OhlcMsg ohlcMsg) {
        val row = ohlcData.appendRow();
        row.setInt(0, ohlcMsg.getChannelID());
        row.setDouble(1, ohlcMsg.getTime());
        row.setDouble(2, ohlcMsg.getEtime());
        row.setDouble(3, ohlcMsg.getOpen());
        row.setDouble(4, ohlcMsg.getHigh());
        row.setDouble(5, ohlcMsg.getLow());
        row.setDouble(6, ohlcMsg.getClose());
        row.setDouble(7, ohlcMsg.getVwap());
        row.setDouble(8, ohlcMsg.getVolume());
        row.setDouble(9, ohlcMsg.getCount());
        row.setString(10, ohlcMsg.getChannelName());
        row.setString(11, ohlcMsg.getPair());
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

    public Table getOhlcData(Optional<String> channelName) {
        var filteredTable = ohlcData;
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
                filteredTable = ohlcData.where(ohlcData.stringColumn("channelName").isEqualTo(id));
            } else { // integer
                filteredTable = ohlcData.where(ohlcData.stringColumn("channelName").isEqualTo(id)
                        .or(ohlcData.intColumn("channelID").isEqualTo(channelID)));
            }
        }
        return filteredTable;
    }

    public void updateSubscription(SubscriptionStatusMsg msg) {
        var channelId = msg.getChannelID();
        if (msg.getChannelName() != null) {
            channelId = msg.getChannelName().equalsIgnoreCase("ownTrades") ? OWN_TRADES_CHANNEL :
                    msg.getChannelName().equalsIgnoreCase("openOrders") ? OPEN_ORDERS_CHANNEL : msg.getChannelID();
        }
        if (msg.getStatus().equals("unsubscribed")) {
            this.subscribedChannels.remove(channelId);
        }
        else {
            this.subscribedChannels.put(channelId, msg);
        }
    }

    public SubscriptionStatusMsg findByName (String name) {
        val channels = this.subscribedChannels.values().stream().filter(ch -> ch.getSubscriptionName().equalsIgnoreCase(name)).collect(Collectors.toList());
        return channels.get(0);
    }

    public void clearChannels() {
        this.subscribedChannels.clear();
    }

    public void addOpenOrders(OpenOrdersMsg openOrders) {
        this.openOrders = openOrders.getOwnTrades();
    }
}
