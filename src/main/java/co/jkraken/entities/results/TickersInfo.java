package co.jkraken.entities.results;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TickersInfo {

    @Data
     public static class Ticker {
        private List<Double> a; // ask array(<price>, <whole lot volume>, <lot volume>)
        private List<Double> b; // bid array(<price>, <whole lot volume>, <lot volume>)
        private List<Double> c; // last trade closed array(<price>, <lot volume>)
        private List<Double> v; // volume array(<today>, <last 24 hours>)
        private List<Double> p; // volume weighted average price array(<today>, <last 24 hours>)
        private List<Double> t; // number of trades array(<today>, <last 24 hours>)
        private List<Double> l; // low array(<today>, <last 24 hours>)
        private List<Double> h; // high array(<today>, <last 24 hours>)
        private Double o; // today's opening price
    }

    private Map<String, Ticker> result;
}
