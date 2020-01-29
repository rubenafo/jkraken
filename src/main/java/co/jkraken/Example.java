package co.jkraken;

import co.jkraken.api.JKraken;
import co.jkraken.entities.AssetPairsEnum;

public class Example {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getAccountTrade());
		//System.out.println(JKraken.getOrderBook(AssetPairsEnum.AssetPairs.BCHUSD, 3));
		System.out.println(JKraken.getSpread(AssetPairsEnum.AssetPairs.BCHUSD, 0));
		//System.out.println(new JKraken().getTradeVolume("XXBTZUSD", "XXBTZEUR"));
		//var sell = Order.createSell(AssetPairsEnum.AssetPairs.XBTUSDC, 44d,20);
		//System.out.println(new JKraken().addOrder(sell));
	}
}
