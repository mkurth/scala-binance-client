package com.mkurth.binance.client

import io.circe.{Decoder, HCursor}

sealed trait Filter

object Filter {
  implicit val decodeFilter: Decoder[Filter] = (c: HCursor) => {
    c.downField("filterType").as[String] match {
      case Right("PRICE_FILTER") =>
        for {
          minPrice <- c.downField("minPrice").as[String]
          maxPrice <- c.downField("maxPrice").as[String]
          tickSize <- c.downField("tickSize").as[String]
        } yield PriceFilter(BigDecimal(minPrice), BigDecimal(maxPrice), BigDecimal(tickSize))
      case Right("LOT_SIZE") =>
        for {
          minQty <- c.downField("minQty").as[String]
          maxQty <- c.downField("maxQty").as[String]
          stepSize <- c.downField("stepSize").as[String]
        } yield LotSize(BigDecimal(minQty), BigDecimal(maxQty), BigDecimal(stepSize))
      case Right("MIN_NOTIONAL") =>
        for {
          minNotional <- c.downField("minNotional").as[String]
        } yield MinNotional(BigDecimal(minNotional))
    }
  }
}

case class PriceFilter(minPrice: BigDecimal,
                       maxPrice: BigDecimal,
                       tickSize: BigDecimal) extends Filter

case class LotSize(minQty: BigDecimal,
                   maxQty: BigDecimal,
                   stepSize: BigDecimal) extends Filter

case class MinNotional(minNotional: BigDecimal) extends Filter

case class RateLimit(rateLimitType: String,
                     interval: String,
                     intervalNum: Int,
                     limit: Long)

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

case class ExchangeInfo(timezone: String,
                        serverTime: Long,
                        rateLimits: Seq[RateLimit],
                        exchangeFilters: Seq[_],
                        symbols: Seq[BSymbol])
