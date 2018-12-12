package com.mkurth.binance.client

import com.softwaremill.sttp.{FetchBackend, SttpBackend}

import scala.concurrent.{ExecutionContext, Future}

class BinanceClient(val binanceAuth: BinanceAuth, proxyPath: Option[String] = None)(implicit val ex: ExecutionContext) extends BinanceClientBase {
  override def basePath: String = proxyPath.getOrElse(super.basePath)
  override val messageSigning: MessageSigning = new HMACSha256()
  override implicit val sttpBackend: SttpBackend[Future, Nothing] = FetchBackend()
}
