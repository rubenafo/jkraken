package com.jkraken;

import com.jkraken.utils.LocalPropLoader;

import java.io.IOException;

public class Example {

	public static void main(String[] args) {
		System.out.println(new LocalPropLoader().getPrivAPI());
	}
}
