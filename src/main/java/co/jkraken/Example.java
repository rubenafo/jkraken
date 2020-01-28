package co.jkraken;

import co.jkraken.api.JKraken;
import co.jkraken.entities.Order;
import co.jkraken.entities.AssetPairsEnum;
import lombok.var;

public class Example {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getAccountTrade());
		//System.out.println(new JKraken().getTradeBalance());
		//System.out.println(new JKraken().getTradeVolume("XXBTZUSD", "XXBTZEUR"));
		var sell = Order.createSell(AssetPairsEnum.AssetPairs.XBTUSDC, 44d,20);
		System.out.println(new JKraken().addOrder(sell));
	}
}
