package jk.wsocket.validation;


import jk.rest.entities.AssetPairsEnum;
import lombok.val;

import java.util.List;
import java.util.Set;

/**
 * Validates requests parameters
 */
public class RequestValidator {

    private static Set<Integer> DEPTHS = Set.of(10, 25, 100, 500, 1000);
    private static Set<Integer> INTERVALS = Set.of(1, 5, 15, 30, 60, 240, 1440, 10080, 21600);
    private static Set<String> NAMES = Set.of("ticker","ohlc","trade","book","spread","ownTrades","openOrders");

    public static void validateSubscription (List<String> pairs, int interval, int depth, String name) {
        val validPairs = List.of(AssetPairsEnum.AssetPairs.values());
        if (pairs != null) {
            pairs.forEach(pair -> {
                if (!validPairs.contains(pair.toLowerCase())) {
                    throw new RuntimeException(String.format("Invalid pair:%s"));
                }
            });
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
}
