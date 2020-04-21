package jk;

import jk.rest.api.JKraken;
import jk.rest.engine.ApiSign;
import jk.rest.entities.AssetPairsEnum;
import jk.rest.entities.Order;
import lombok.var;

public class Example {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getOpenOrders());
		//System.out.println(JKraken.getOrderBook(AssetPairsEnum.AssetPairs.BCHUSD, 3));
		//System.out.println(JKraken.getTicker(AssetPairsEnum.AssetPairs.XBTUSDC));
		//System.out.println(new JKraken().getTradeVolume("XXBTZUSD", "XXBTZEUR"));
//		var sell = Order.createSellOrder(AssetPairsEnum.AssetPairs.XBTUSDC, 44d,20);
//		System.out.println(new JKraken().addOrder(sell));
		//var rateManager = new RateManager();
		ApiSign.getWebSocketAuthToken();
	}
}
