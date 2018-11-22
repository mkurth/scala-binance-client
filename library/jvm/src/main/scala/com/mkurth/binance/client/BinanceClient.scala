package com.mkurth.binance.client
import com.softwaremill.sttp.SttpBackend
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend

import scala.concurrent.{ExecutionContext, Future}

class BinanceClient(val binanceAuth: BinanceAuth)(implicit val ex: ExecutionContext) extends BinanceClientBase {
  override val messageSigning: MessageSigning = new HMACSha256()
  override implicit val sttpBackend: SttpBackend[Future, Nothing] = AsyncHttpClientFutureBackend()
}
