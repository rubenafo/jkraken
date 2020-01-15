package com.jkraken.api;

import com.jkraken.entities.*;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

public class JKraken {

    public static ServerDate getTime () {
        var serverDate = new RestTemplate().getForEntity("https://api.kraken.com/0/public/Time", ServerDate.class).getBody();
        return serverDate;
    }

    public static Asset getAssets () {
        var assets = new RestTemplate().getForEntity("https://api.kraken.com/0/public/Assets", Asset.class).getBody();
        return assets;
    }

    public static AssetPairs getAssetPairs () {
        var assetPairs = new RestTemplate().getForEntity("https://api.kraken.com/0/public/AssetPairs", AssetPairs.class).getBody();
        return assetPairs;
    }

    public static Tickers getTicker (String... pairs) {
        var url = "https://api.kraken.com/0/public/Ticker";
        if (pairs.length > 0) {
            url = StringUtils.join(url, "?pair=", StringUtils.join(pairs, ","));
        }
        var tickers = new RestTemplate().getForEntity(url, Tickers.class).getBody();
        return tickers;
    }

    private static OHLCData getOHLC (String pair, int interval, int since) {

    }
}
