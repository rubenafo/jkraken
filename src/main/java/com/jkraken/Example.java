package com.jkraken;

import com.jkraken.api.JKraken;
import lombok.var;

public class Example {

	public static void main(String[] args) {

		var ohlc = JKraken.getDepth("BATUSD", 5);
		System.out.println(ohlc);
	}
}
