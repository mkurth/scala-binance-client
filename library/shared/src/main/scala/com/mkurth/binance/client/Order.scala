package com.mkurth.binance.client

sealed trait Order {
  val `type`: String
}

final case class LimitOrder(symbol: String,
                            side: OrderSide,
                            quantity: BigDecimal,
                            timestamp: Long,
                            timeInForce: TimeInForce,
                            price: BigDecimal,
                            newClientOrderId: Option[String] = None,
                            stopPrice: Option[BigDecimal] = None,
                            icebergQty: Option[BigDecimal] = None,
                            newOrderRespType: Option[OrderResponseType] = None,
                            recvWindow: Option[Long] = None) extends Order {
  val `type`: String = "LIMIT"
}

final case class MarketOrder(symbol: String,
                             side: OrderSide,
                             quantity: BigDecimal,
                             timestamp: Long,
                             price: BigDecimal,
                             timeInForce: Option[TimeInForce] = None,
                             newClientOrderId: Option[String] = None,
                             stopPrice: Option[BigDecimal] = None,
                             icebergQty: Option[BigDecimal] = None,
                             newOrderRespType: Option[OrderResponseType] = None,
                             recvWindow: Option[Long] = None) extends Order {
  val `type`: String = "MARKET"
}