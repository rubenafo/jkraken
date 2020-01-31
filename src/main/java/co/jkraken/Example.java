package co.jkraken;

import co.jkraken.api.JKraken;
import co.jkraken.entities.AssetPairsEnum;
import co.jkraken.entities.Order;
import lombok.var;

public class Example {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getOpenOrders());
		//System.out.println(JKraken.getOrderBook(AssetPairsEnum.AssetPairs.BCHUSD, 3));
		//System.out.println(JKraken.getTicker(AssetPairsEnum.AssetPairs.XBTUSDC));
		System.out.println(new JKraken().getTradeVolume("XXBTZUSD", "XXBTZEUR"));
		//var sell = Order.createSell(AssetPairsEnum.AssetPairs.XBTUSDC, 44d,20);
		//System.out.println(new JKraken().addOrder(sell));
	}
}
