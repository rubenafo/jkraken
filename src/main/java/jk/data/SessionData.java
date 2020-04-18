package jk.data;


import jk.wsocket.responses.ChannelMsg;
import jk.wsocket.responses.TickerMsg;
import lombok.Getter;
import lombok.val;
import tech.tablesaw.api.*;

import java.text.DateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * This class contains all the session's data, organized by channelId
 */
@Getter
public class SessionData {

    private static final DateTimeFormatter formatter = ISO_INSTANT;

    private final Table subscriptionData;
    private final Table tickerData;

    public SessionData () {
        this.subscriptionData = Table.create("subscriptionData").
                addColumns(
                IntColumn.create("channelID"),
                StringColumn.create("channelName"),
                StringColumn.create("ticker"),
                StringColumn.create("event"),
                StringColumn.create("pair"),
                StringColumn.create("status"), LongColumn.create("ts"));

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

    public void append(ChannelMsg channelAck, long timestamp) {
        val row = subscriptionData.appendRow();
        row.setInt(0, channelAck.getChannelId());
        row.setString(1, channelAck.getChannelName());
        row.setString(2, channelAck.getTicker());
        row.setString(3, channelAck.getEvent());
        row.setString(4, channelAck.getPair());
        row.setString(5, channelAck.getStatus());
        row.setLong(6, timestamp);
    }
}
