package com.jkraken;

import com.jkraken.entities.Asset;
import com.jkraken.entities.AssetPairManager;
import com.jkraken.entities.ServerDate;
import lombok.var;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//SpringApplication.run(DemoApplication.class, args);
		var serverDate = new RestTemplate().getForEntity("https://api.kraken.com/0/public/Time", ServerDate.class).getBody();
		System.out.println(serverDate);

		var assets = new RestTemplate().getForEntity("https://api.kraken.com/0/public/Assets", Asset.class).getBody();
		System.out.println(assets);

		var assetPairs = new RestTemplate().getForEntity("https://api.kraken.com/0/public/AssetPairs", AssetPairManager.class).getBody();
		System.out.println(assetPairs);
	}

}
