[![License](https://img.shields.io/badge/license-Apache-blue.svg?style=flat)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.com/rubenafo/jkraken.svg?branch=master)](https://travis-ci.com/rubenafo/jkraken)
[![Coverage Status](https://coveralls.io/repos/github/rubenafo/jkraken/badge.svg?branch=master)](https://coveralls.io/github/rubenafo/jkraken?branch=master)
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
Setup and more: [here](https://github.com/rubenafo/jkraken/wiki)  
Kraken API implemented so far is documented here: [Kraken API support](https://github.com/rubenafo/jkraken/wiki/API-Support)


## Disclaimer
This package provides functionality to create real trade orders using real assets directly on Kraken Exchange.  
You can lose money by using such functionality so it's under your own risk.  
Use wisely.
