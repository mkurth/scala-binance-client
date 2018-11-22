package com.mkurth.binance.client

import io.circe.generic.auto._
import io.circe.parser.decode
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source
import scala.language.postfixOps

class JsonDecodeTest extends FlatSpec with Matchers {

  "BSymbol" should "decode from JSON" in {
    val json =
      """
        |{
        |    "symbol": "ETHBTC",
        |    "status": "TRADING",
        |    "baseAsset": "ETH",
        |    "baseAssetPrecision": 8,
        |    "quoteAsset": "BTC",
        |    "quotePrecision": 8,
        |    "orderTypes": ["LIMIT", "MARKET"],
        |    "icebergAllowed": false,
        |    "filters": [{
        |      "filterType": "PRICE_FILTER",
        |      "minPrice": "0.00000100",
        |      "maxPrice": "100000.00000000",
        |      "tickSize": "0.00000100"
        |    }, {
        |      "filterType": "LOT_SIZE",
        |      "minQty": "0.00100000",
        |      "maxQty": "100000.00000000",
        |      "stepSize": "0.00100000"
        |    }, {
        |      "filterType": "MIN_NOTIONAL",
        |      "minNotional": "0.00100000"
        |    }]
        |  }
      """.stripMargin
    val decoded = decode[BSymbol](json).right.get
    decoded shouldBe a[BSymbol]
    decoded.filters.find(_.isInstanceOf[PriceFilter]).get shouldBe a[PriceFilter]
  }

  "RateLimit" should "decode from JSON" in {
    val json =
      """
        |{
        |  "rateLimitType": "REQUESTS_WEIGHT",
        |  "interval": "MINUTE",
        |  "intervalNum": 1,
        |  "limit": 1200
        |}
      """.stripMargin

    decode[RateLimit](json).right.get shouldBe a[RateLimit]
  }

  "ExchangeInfo" should "be parsable from JSON" in {
    val json =
      """
        |{
        |  "timezone": "UTC",
        |  "serverTime": 1508631584636,
        |  "rateLimits": [{
        |      "rateLimitType": "REQUESTS_WEIGHT",
        |      "interval": "MINUTE",
        |      "intervalNum": 1,
        |      "limit": 1200
        |    },
        |    {
        |      "rateLimitType": "ORDERS",
        |      "interval": "SECOND",
        |      "intervalNum": 1,
        |      "limit": 10
        |    },
        |    {
        |      "rateLimitType": "ORDERS",
        |      "interval": "DAY",
        |      "intervalNum": 1,
        |      "limit": 100000
        |    },
        |    {
        |      "rateLimitType": "RAW_REQUESTS",
        |      "interval": "MINUTE",
        |      "intervalNum": 5,
        |      "limit": 5000
        |    }
        |  ],
        |  "exchangeFilters": [],
        |  "symbols": [{
        |    "symbol": "ETHBTC",
        |    "status": "TRADING",
        |    "baseAsset": "ETH",
        |    "baseAssetPrecision": 8,
        |    "quoteAsset": "BTC",
        |    "quotePrecision": 8,
        |    "orderTypes": ["LIMIT", "MARKET"],
        |    "icebergAllowed": false,
        |    "filters": [{
        |      "filterType": "PRICE_FILTER",
        |      "minPrice": "0.00000100",
        |      "maxPrice": "100000.00000000",
        |      "tickSize": "0.00000100"
        |    }, {
        |      "filterType": "LOT_SIZE",
        |      "minQty": "0.00100000",
        |      "maxQty": "100000.00000000",
        |      "stepSize": "0.00100000"
        |    }, {
        |      "filterType": "MIN_NOTIONAL",
        |      "minNotional": "0.00100000"
        |    }]
        |  }]
        |}
      """.stripMargin

    decode[ExchangeInfo](json).right.get shouldBe an[ExchangeInfo]
  }

  "PriceFilter Filter" should "decode from json" in {
    val json =
      """|{
         |   "filterType": "PRICE_FILTER",
         |   "minPrice": "0.00000100",
         |   "maxPrice": "100000.00000000",
         |   "tickSize": "0.00000100"
         |}""".stripMargin
    val decoded = decode[Filter](json).right.get

    decoded shouldBe a[PriceFilter]
    decoded.asInstanceOf[PriceFilter].maxPrice should be(BigDecimal(100000))
    decoded.asInstanceOf[PriceFilter].minPrice should be(BigDecimal(0.000001))
    decoded.asInstanceOf[PriceFilter].tickSize should be(BigDecimal(0.000001))
  }

  "MinNotional Filter" should "decode" in {
    val json =
      """
        |{
        |  "filterType": "MIN_NOTIONAL",
        |  "minNotional": "0.00100000",
        |  "applyToMarket": true,
        |  "avgPriceMins": 5
        |}
      """.stripMargin

    val decoded = decode[Filter](json).right.get

    decoded shouldBe a[MinNotional]
  }

  "Whole ExchangeInfo" should "decode" in {
    val json = Source.fromResource("exchangeInfo.json").getLines().mkString

    val decoded = decode[ExchangeInfo](json)
    decoded shouldBe a[Right[_, ExchangeInfo]]
  }

}
