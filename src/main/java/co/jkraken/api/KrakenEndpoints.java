package co.jkraken.api;

public final class KrakenEndpoints {

    public static String BASE_API = "https://api.kraken.com";

    // public Endpoints
    public static String SERVER_TIME = "/0/public/Time";
    public static String ASSET_INFO = "/0/public/Assets";
    public static String RECENT_TRADES = "/0/public/Trades";
    public static String ASSET_PAIRS = "/0/public/AssetPairs";
    public static String TICKER_INFO = "/0/public/Ticker";

    // Private Endpoints
    public static String PRIVATE_BALANCE = "/0/private/Balance";
    public static String PRIVATE_TRADE_BALANCE = "/0/private/TradeBalance";
    public static String OPEN_ORDERS = "/0/private/OpenOrders";
    public static String CLOSED_ORDERS = "/0/private/ClosedOrders";
    public static String OPEN_POSITION = "/0/private/OpenPositions";
    public static String TRADES_HISTORY = "/0/private/TradesHistory";
    public static String TRADE_VOLUME = "/0/private/TradeVolume";
    public static String ADD_ORDER = "/0/private/AddOrder";

    public static String url (String path) {
        return BASE_API + path;
    }
}