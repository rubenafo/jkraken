package com.jkraken.api;

import com.jkraken.entities.*;
import com.jkraken.entities.results.AccountBalanceInfo;
import com.jkraken.entities.results.TradeBalanceInfo;
import com.jkraken.utils.ApiSign;
import com.jkraken.utils.LocalPropLoader;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    public AccountBalanceInfo getAccountTrade () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.PRIVATE_BALANCE;
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.PRIVATE_BALANCE, this.properties);
        ResponseEntity<AccountBalanceInfo> response = new RestTemplate().postForEntity(url, requestsEntity, AccountBalanceInfo.class);
        return response.getBody();
    }

    public TradeBalanceInfo getTradeBalance () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.PRIVATE_TRADE_BALANCE;
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.PRIVATE_TRADE_BALANCE, this.properties);
        ResponseEntity<TradeBalanceInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradeBalanceInfo.class);
        return response.getBody();
    }
}
