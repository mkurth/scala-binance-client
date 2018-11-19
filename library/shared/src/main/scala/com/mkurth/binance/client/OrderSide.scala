package com.mkurth.binance.client

sealed trait OrderSide {
  val side: String
}
case object BuyOrder extends OrderSide {
  override final val side: String = "BUY"
}
case object SellOrder extends OrderSide {
  override final val side: String = "SELL"
}
