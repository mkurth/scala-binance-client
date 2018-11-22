package com.mkurth.binance.client


case class ExchangeInfo(timezone: String,
                        serverTime: Long,
                        rateLimits: Seq[RateLimit],
                        symbols: Seq[BSymbol])
