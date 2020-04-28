package jk;

import jk.rest.api.KrakenRestService;

public class SampleRequests {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getOpenOrders());
		//System.out.println(JKraken.getOrderBook(AssetPairsEnum.AssetPairs.BCHUSD, 3));
		//System.out.println(JKraken.getTicker(AssetPairsEnum.AssetPairs.XBTUSDC));
		//System.out.println(new JKraken().getTradeVolume("XXBTZUSD", "XXBTZEUR"));
//		var sell = Order.createSellOrder(AssetPairsEnum.AssetPairs.XBTUSDC, 44d,20);
		System.out.println(new KrakenRestService().getBalance());
		//System.out.println (new KrakenRestService().getBalance());
		//var rateManager = new RateManager();
		//ApiSign.getWebSocketAuthToken();
	}
}
