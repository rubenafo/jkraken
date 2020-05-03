package jk.wsocket.validation;


import jk.rest.entities.AssetPairsEnum;
import jk.wsocket.service.KrakenHandler;
import jk.wsocket.service.KrakenWsService;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates requests parameters
 */
public class RequestValidator {

    private static Set<Integer> DEPTHS = Set.of(10, 25, 100, 500, 1000);
    private static Set<Integer> INTERVALS = Set.of(1, 5, 15, 30, 60, 240, 1440, 10080, 21600);
    private static Set<String> NAMES = Set.of("ticker","ohlc","trade","book","spread","owntrades","openorders");
    private static Set<String> REQUIRING_PAIR = Set.of("ticker", "ohlc", "trade");

    public static void validateSubscription (List<String> pairs, int interval, int depth, String name) {
        val validPairs = Arrays.stream(AssetPairsEnum.AssetPairs.values())
                .filter(pair -> pair.getWsName() != null)
                .map(pair -> pair.getWsName().toLowerCase())
                .collect(Collectors.toList());
        if (pairs != null) {
            pairs.forEach(pair -> {
                if (!validPairs.contains(pair.toLowerCase())) {
                    throw new RuntimeException(String.format("Invalid pair:%s", pair));
                }
            });
        }
        if (REQUIRING_PAIR.contains(name.toLowerCase()) && pairs == null) {
            throw new RuntimeException(String.format("Invalid pair: pair cannot be empty for channel=%s", name));
        }
        if (!INTERVALS.contains(interval)) {
            throw new RuntimeException(String.format("Invalid interval:%s . Expected:%s", interval, INTERVALS));
        }
        if (!DEPTHS.contains(depth)) {
            throw new RuntimeException(String.format("Invalid depth:%s . Expected:%s", depth, DEPTHS));
        }
        if (!NAMES.contains(name.toLowerCase())) {
            throw new RuntimeException(String.format("Invalid name:%s . Expected:%s", name, NAMES));
        }
    }

    public static void assertConnected (KrakenHandler handler) {
        if (handler.connected() == false) {
            throw new RuntimeException(String.format("%s handler not connected", handler.getId()));
        }
    }
}
