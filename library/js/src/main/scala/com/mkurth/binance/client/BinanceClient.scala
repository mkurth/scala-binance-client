package com.mkurth.binance.client

import com.softwaremill.sttp.{FetchBackend, SttpBackend}

import scala.concurrent.{ExecutionContext, Future}

class BinanceClient(val binanceAuth: BinanceAuth)(implicit val ex: ExecutionContext) extends BinanceClientBase {
  override val messageSigning: MessageSigning = new HMACSha256()
  override implicit val sttpBackend: SttpBackend[Future, Nothing] = FetchBackend()
}
