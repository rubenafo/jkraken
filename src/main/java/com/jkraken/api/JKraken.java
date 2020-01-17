package com.jkraken.api;

import com.jkraken.entities.*;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

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

    public static OHLCValues getOHLC (int interval, int since, String... pairs) {
        var url = "https://api.kraken.com/0/public/OHLC";
        if (pairs.length > 0) {
            url = StringUtils.join(url, "?pair=", StringUtils.join(pairs, ","));
        }
        String fullUrl = String.format("%s&interval=%s", url, interval);
        return new RestTemplate().getForEntity(fullUrl, OHLCValues.class).getBody();
    }

    public static Depth getDepth (String pair, int count) {
        var url = String.format("https://api.kraken.com/0/public/Depth?pair=%s&count=%s", pair, count);
        return new RestTemplate().getForEntity(url, Depth.class).getBody();
    }

    public void test () {
        var headers = new HttpHeaders();
        new RestTemplate().headForHeaders("", headers);
    }
}
