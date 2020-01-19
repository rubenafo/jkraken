package com.jkraken;

import com.jkraken.api.JKraken;
import com.jkraken.utils.ApiSign;
import com.jkraken.utils.LocalPropLoader;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Example {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
		System.out.println(new JKraken().getAccountTrade());
	}
}
