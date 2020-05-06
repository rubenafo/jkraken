package jk.krakenex;

import lombok.Getter;

import java.util.Set;
import java.util.stream.Stream;

public final class KrakenEnums {

    public enum Channels {

        TICKER("ticker", false),
        OHLC("ohlc", false),
        TRADE("trade", false),
        BOOK("book", false),
        SPREAD("spread", false),
        OWN_TRADES("ownTrades", true),
        OPEN_ORDERS ("openOrders", true);

        @Getter
        private final String channelName;
        private final boolean isPrivate;

        Channels(String ticker, boolean isPrivate) {
            this.channelName = ticker;
            this.isPrivate = isPrivate;
        }

        public static boolean contains (String name) {
            return Stream.of(Channels.values())
                    .filter(v -> v.channelName.equalsIgnoreCase(name))
                    .findFirst().isPresent();
        }
    };

    public enum ConfigProperties {

        API("api"),
        PAPI("papi"),
        PUBLIC_ENDPOINT("public_ws"),
        PRIVATE_ENDPOINT("private_ws"),
        SUBSCRIBE_TO ("subscribe"),
        CONNECT_ON_START ("connect_on_start");

        @Getter
        private final String configName;

        ConfigProperties(String configName) {
            this.configName = configName;
        }

        public static boolean contains (String name) {
            return Stream.of(ConfigProperties.values())
                    .filter(v -> v.configName.equalsIgnoreCase(name))
                    .findFirst().isPresent();
        }
    };

    public static Set<Integer> DEPTHS = Set.of(10, 25, 100, 500, 1000);
    public static Set<Integer> INTERVALS = Set.of(1, 5, 15, 30, 60, 240, 1440, 10080, 21600);
    public static Set<String> REQUIRING_PAIR = Set.of("ticker", "ohlc", "trade");
}
