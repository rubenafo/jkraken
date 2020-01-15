package com.jkraken;

import com.jkraken.api.JKraken;
import lombok.var;

public class Example {

	public static void main(String[] args) {
		var ticker = JKraken.getTicker("BATUSD");
		System.out.println(ticker);
	}
}
