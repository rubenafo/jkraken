package co.jkraken.entities;

import lombok.Data;

import java.util.Map;

@Data
public class Tickers {

    private Map<String, Ticker> result;
}
