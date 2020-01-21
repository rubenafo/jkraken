package com.jkraken;

import com.jkraken.api.JKraken;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.Date;

public class Example {

	public static void main(String[] args)  {
		//System.out.println(new JKraken().getAccountTrade());
		//System.out.println(new JKraken().getTradeBalance());
		System.out.println(new JKraken().getTradesHistory());
	}
}
