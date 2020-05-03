# JKraken
A REST server to interact with [Kraken API](https://www.kraken.com/features/api) using websockets and a REST client.  
JKraken provides:
* subscription channels and position/trades tracking using [Kraken websockets](https://docs.kraken.com/websockets/#overview)
* balance, positions and order handling using [Kraken API](https://www.kraken.com/features/api)
* private keys handling and auth token refresh.

#### Subscription channels

JKraken exposes endpoints to subscribe to Kraken channels using [websockets](https://docs.kraken.com/websockets/#overview)
* /subscribe : subscribes to _ticker_, _ohlc_, _spread_ data
* /unsubscribe


#### Kraken API current support (WIP)

Kraken API implemented so far is documented here: [Kraken API support](https://github.com/rubenafo/jkraken/wiki/API-Support)

Kraken API code can be reused in a standalone way directly from _KrakenRestService_ class:
```
import jk.rest.api.KrakenRestService;

System.out.println (new KrakenRestService().getBalance());.
```
which returns
```json
{"error": [], "result": {"ZEUR": 300.089, "XXBT": 90.0498929700}}
```


## Disclaimer
This package provides functionality to create real trade orders using real assets directly on Kraken Exchange.  
You can lose money by using such functionality so it's under your own risk.  
Use wisely.
