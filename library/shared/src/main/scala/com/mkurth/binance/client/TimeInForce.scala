package com.mkurth.binance.client

sealed trait TimeInForce {
  val timeInForce: String
}

case object GTC extends TimeInForce {
  override final val timeInForce: String = "GTC"
}

case object IOC extends TimeInForce {
  override final val timeInForce: String = "IOC"
}

case object FOK extends TimeInForce {
  override final val timeInForce: String = "FOK"
}