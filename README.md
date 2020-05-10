[![Build Status](https://travis-ci.com/rubenafo/jkraken.svg?branch=master)](https://travis-ci.com/rubenafo/jkraken)
------------
# JKraken
A REST server to interact with [Kraken API](https://www.kraken.com/features/api) aiming to unify Kraken WebSocket and Rst APIs in one single place.  
JKraken provides:
* subscription channels and position/trades tracking using [Kraken websockets](https://docs.kraken.com/websockets/#overview)
* balance and trades using [Kraken REST API](https://www.kraken.com/features/api)
* add/remove orders
* private keys handling and auth token refresh.

#### Subscription channels

JKraken exposes endpoints to subscribe to Kraken channels using [websockets](https://docs.kraken.com/websockets/#overview):
* public channels: _ticker_, _ohlc_, _spread_ data
* private channels: _ownTrades_, _openOrders_


### Documentation

Kraken API implemented so far is documented here: [Kraken API support](https://github.com/rubenafo/jkraken/wiki/API-Support)


## Disclaimer
This package provides functionality to create real trade orders using real assets directly on Kraken Exchange.  
You can lose money by using such functionality so it's under your own risk.  
Use wisely.
