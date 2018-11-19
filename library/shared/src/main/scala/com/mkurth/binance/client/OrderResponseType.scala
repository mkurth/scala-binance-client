package com.mkurth.binance.client

sealed trait OrderResponseType {
  val responseType: String
}

case object Ack extends OrderResponseType {
  override final val responseType: String = "ACK"
}

case object Result extends OrderResponseType {
  override final val responseType: String = "RESULT"
}

case object Full extends OrderResponseType {
  override final val responseType: String = "FULL"
}
