package jk.wsocket.data;


import jk.krakenex.KrakenEnums;
import jk.rest.entities.AssetPairsEnum;
import jk.wsocket.service.KrakenWsHandler;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.text.html.parser.DTDConstants.NAMES;
import static jk.krakenex.KrakenEnums.DEPTHS;

/**
 * Validates requests parameters
 */
public class RequestValidator {

    public static void validateSubscription (List<String> pairs, int interval, int depth, String name) {
        RequestValidator.validatePairs(pairs);
        if (!KrakenEnums.Channels.contains(name) && pairs == null) {
            throw new RuntimeException(String.format("Invalid pair: pair cannot be empty for channel=%s", name));
        }
        if (!KrakenEnums.INTERVALS.contains(interval)) {
            throw new RuntimeException(String.format("Invalid interval:%s . Expected:%s", interval, KrakenEnums.INTERVALS));
        }
        if (!DEPTHS.contains(depth)) {
            throw new RuntimeException(String.format("Invalid depth:%s . Expected:%s", depth, DEPTHS));
        }
        if (!KrakenEnums.Channels.contains(name)) {
            throw new RuntimeException(String.format("Invalid name:%s . Expected:%s", name, NAMES));
        }
    }

    public static void validateUnsubscribe (List<String> pairs, String name) {
        if (pairs == null && name == null) {
            throw new RuntimeException("pairs or name parameters must be provided");
        }
        RequestValidator.validatePairs(pairs);
    }

    public static void validatePairs (List<String> pairs) {
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
    }

    public static void assertConnected (KrakenWsHandler handler) {
        if (handler.connected() == false) {
            throw new RuntimeException(String.format("%s handler not connected", handler.getId()));
        }
    }
}
