package co.jkraken;

import co.jkraken.api.JKraken;
import co.jkraken.entities.AddOrder;
import co.jkraken.entities.AssetPairsEnum;
import lombok.var;

public class Example {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getAccountTrade());
		//System.out.println(new JKraken().getTradeBalance());
		//var data = new JKraken().getTradeVolume("XXBTZUSD", "XXBTZEUR");
		System.out.println(AddOrder.createSell(AssetPairsEnum.AssetPairs.XBTUSDC, AddOrder.ORDERTYPE.MARKET, 20));
	}
}
