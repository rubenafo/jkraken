package com.jkraken.api;

import com.jkraken.entities.*;
import com.jkraken.entities.results.*;
import com.jkraken.utils.ApiSign;
import com.jkraken.utils.LocalPropLoader;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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

    public static RecentTradesInfo getRecentTrades (String pair, long tradeId) {
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.RECENT_TRADES;
        url = StringUtils.join(url, "?pair=", pair);
        return new RestTemplate().getForEntity(url, RecentTradesInfo.class).getBody();
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

    public OpenOrdersInfo getOpenOrders () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.OPEN_ORDERS;
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.OPEN_ORDERS, this.properties);
        ResponseEntity<OpenOrdersInfo> response = new RestTemplate().postForEntity(url, requestsEntity, OpenOrdersInfo.class);
        return response.getBody();
    }

    public ClosedOrdersInfo getClosedOrders (double startTime, double endTime) {
        var params = new HashMap<String,Object>();
        params.put("start", startTime);
        params.put("end", endTime);
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.CLOSED_ORDERS;
        var requestsEntity = ApiSign.getRequest(url, params, KrakenEndpoints.CLOSED_ORDERS, this.properties);
        ResponseEntity<ClosedOrdersInfo> response = new RestTemplate().postForEntity(url, requestsEntity, ClosedOrdersInfo.class);
        return response.getBody();
    }

    public OpenPositionsInfo getOpenPositions () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.OPEN_POSITION;
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.OPEN_POSITION, this.properties);
        ResponseEntity<OpenPositionsInfo> response = new RestTemplate().postForEntity(url, requestsEntity, OpenPositionsInfo.class);
        return response.getBody();
    }

    public TradesHistoryInfo getTradesHistory () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.TRADES_HISTORY;
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.TRADES_HISTORY, this.properties);
        ResponseEntity<TradesHistoryInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradesHistoryInfo.class);
        return response.getBody();
    }

    public TradeVolumeInfo getTradeVolume(String ...pairs) {
        ApiSign.availableKeys(this.properties);
        var data = new HashMap<String,Object>();
        data.put("pair", StringUtils.join(pairs,","));
        var url = KrakenEndpoints.BASE_API + KrakenEndpoints.TRADE_VOLUME;
        var requestsEntity = ApiSign.getRequest(url, data, KrakenEndpoints.TRADE_VOLUME, this.properties);
        ResponseEntity<TradeVolumeInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradeVolumeInfo.class);
        return response.getBody();
    }
}
