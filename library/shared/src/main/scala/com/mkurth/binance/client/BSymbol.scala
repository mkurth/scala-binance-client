package com.mkurth.binance.client

case class BSymbol(symbol: String,
                   status: String,
                   baseAsset: String,
                   baseAssetPrecision: Int,
                   quoteAsset: String,
                   quotePrecision: Int,
                   orderTypes: Seq[String],
                   icebergAllowed: Boolean,
                   filters: Seq[Filter]
                  )