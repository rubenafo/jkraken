package com.jkraken.api;

import com.jkraken.entities.*;
import com.jkraken.entities.results.AccountBalanceInfo;
import com.jkraken.utils.ApiSign;
import com.jkraken.utils.LocalPropLoader;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class JKraken {

    private LocalPropLoader properties;
    private String apiKey;
    private String apiSecret;

    public JKraken () {
        this.properties = new LocalPropLoader();
        this.apiKey = properties.getApi();
        this.apiSecret = properties.getApiSecret();
    }

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

    public AccountBalanceInfo getAccountTrade () throws URISyntaxException {
        var url = "https://api.kraken.com/0/private/Balance";
        var headers = new HttpHeaders();
        long nonce = System.currentTimeMillis();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("API-Key", this.apiKey);
        headers.add("API-Sign", ApiSign.calculateSignature(nonce +"", "nonce="+nonce, this.apiSecret, "/0/private/Balance"));
        headers.add("User-Agent", "Kraken REST API - 0");
        var requestsEntity = new RequestEntity<String>("nonce="+nonce, headers, HttpMethod.POST, new URI(url));
        ResponseEntity<AccountBalanceInfo> response = new RestTemplate().postForEntity(url, requestsEntity, AccountBalanceInfo.class);
        return response.getBody();
    }
}
