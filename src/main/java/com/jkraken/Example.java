package com.jkraken;

import com.jkraken.api.JKraken;

public class Example {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getAccountTrade());
		//System.out.println(new JKraken().getTradeBalance());
		System.out.println(JKraken.getAssetPairs());
	}
}
