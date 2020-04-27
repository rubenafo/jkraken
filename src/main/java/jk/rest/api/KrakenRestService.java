package jk.rest.api;

import jk.rest.entities.*;
import jk.rest.entities.results.*;
import jk.rest.tools.ApiSign;
import jk.rest.tools.LocalPropLoader;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static jk.rest.api.KrakenEndpoints.*;

@Service
public class KrakenRestService {

    private final LocalPropLoader properties;
    private final RestTemplate restTemplate;

    public KrakenRestService() {
        this.properties = new LocalPropLoader();
        this.restTemplate = new RestTemplate();
    }

    public static ServerDateInfo getTime () {
        return new RestTemplate().getForEntity(KrakenEndpoints.url(SERVER_TIME), ServerDateInfo.class).getBody();
    }

    public static AssetInfo getAssets () {
        return new RestTemplate().getForEntity(KrakenEndpoints.url(ASSET_INFO), AssetInfo.class).getBody();
    }

    public static AssetPairs getAssetPairs () {
        return new RestTemplate().getForEntity(KrakenEndpoints.url(ASSET_PAIRS), AssetPairs.class).getBody();
    }

    public static TickersInfo getTicker (AssetPairsEnum.AssetPairs... pairs) {
        var url = KrakenEndpoints.url(TICKER_INFO);
        if (pairs.length > 0) {
            url = StringUtils.join(url, "?pair=", StringUtils.join(pairs, ","));
        }
        var tickers = new RestTemplate().getForEntity(url, TickersInfo.class).getBody();
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

    /**
     * @param pair       asset pair
     * @param maxAskBids maximum number of ask/bids
     * @return
     */
    public static Depth getOrderBook(AssetPairsEnum.AssetPairs pair, int maxAskBids) {
        var url = String.format("%s?pair=%s&count=%s", KrakenEndpoints.url(ORDER_BOOK), pair, maxAskBids);
        return new RestTemplate().getForEntity(url, Depth.class).getBody();
    }

    public static RecentTradesInfo getRecentTrades (AssetPairsEnum.AssetPairs pair, long sinceTradeId) {
        var url = KrakenEndpoints.url(KrakenEndpoints.RECENT_TRADES);
        url = StringUtils.join(url, "?pair=", pair);
        if (sinceTradeId != 0) {
            url = StringUtils.join(url, "&since=", sinceTradeId);
        }
        return new RestTemplate().getForEntity(url, RecentTradesInfo.class).getBody();
    }

    public static SpreadInfo getSpread (AssetPairsEnum.AssetPairs pair, long sinceTradeId) {
        var url = KrakenEndpoints.url(SPREAD_DATA);
        url = StringUtils.join(url, "?pair=",pair);
        if (sinceTradeId != 0) {
            url = StringUtils.join(url, "&since=", sinceTradeId);
        }
        return new RestTemplate().getForEntity(url, SpreadInfo.class).getBody();
    }

    // private trading methods

    public AccountBalanceInfo getBalance() {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.url(KrakenEndpoints.PRIVATE_BALANCE);
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.PRIVATE_BALANCE, this.properties);
        ResponseEntity<AccountBalanceInfo> response = new RestTemplate().postForEntity(url, requestsEntity, AccountBalanceInfo.class);
        return response.getBody();
    }

    public TradeBalanceInfo getTradeBalance () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.url(KrakenEndpoints.PRIVATE_TRADE_BALANCE);
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.PRIVATE_TRADE_BALANCE, this.properties);
        ResponseEntity<TradeBalanceInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradeBalanceInfo.class);
        return response.getBody();
    }

    public OpenOrdersInfo getOpenOrders () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.url(KrakenEndpoints.OPEN_ORDERS);
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.OPEN_ORDERS, this.properties);
        ResponseEntity<OpenOrdersInfo> response = new RestTemplate().postForEntity(url, requestsEntity, OpenOrdersInfo.class);
        return response.getBody();
    }

    public ClosedOrdersInfo getClosedOrders (double startTime, double endTime) {
        var params = new HashMap<String,Object>();
        params.put("start", startTime);
        params.put("end", endTime);
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.url(KrakenEndpoints.CLOSED_ORDERS);
        var requestsEntity = ApiSign.getRequest(url, params, KrakenEndpoints.CLOSED_ORDERS, this.properties);
        ResponseEntity<ClosedOrdersInfo> response = new RestTemplate().postForEntity(url, requestsEntity, ClosedOrdersInfo.class);
        return response.getBody();
    }

    public OpenPositionsInfo getOpenPositions () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.url(KrakenEndpoints.OPEN_POSITION);
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.OPEN_POSITION, this.properties);
        ResponseEntity<OpenPositionsInfo> response = new RestTemplate().postForEntity(url, requestsEntity, OpenPositionsInfo.class);
        return response.getBody();
    }

    public TradesHistoryInfo getTradesHistory () {
        ApiSign.availableKeys(this.properties);
        var url = KrakenEndpoints.url(KrakenEndpoints.TRADES_HISTORY);
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.TRADES_HISTORY, this.properties);
        ResponseEntity<TradesHistoryInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradesHistoryInfo.class);
        return response.getBody();
    }

    public TradeVolumeInfo getTradeVolume(String ...pairs) {
        ApiSign.availableKeys(this.properties);
        var data = new HashMap<String,Object>();
        data.put("pair", StringUtils.join(pairs,","));
        var url = KrakenEndpoints.url(KrakenEndpoints.TRADE_VOLUME);
        var requestsEntity = ApiSign.getRequest(url, data, KrakenEndpoints.TRADE_VOLUME, this.properties);
        ResponseEntity<TradeVolumeInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradeVolumeInfo.class);
        return response.getBody();
    }

    public AddOrderInfo addOrder (Order order) {
        ApiSign.availableKeys(this.properties);
        var data = order.asMap();
        var url = KrakenEndpoints.url(ADD_ORDER);
        var requestEntity = ApiSign.getRequest(url, data, ADD_ORDER, this.properties);
        ResponseEntity<AddOrderInfo> response = restTemplate.postForEntity(url, requestEntity, AddOrderInfo.class);
        return response.getBody();
    }
}
