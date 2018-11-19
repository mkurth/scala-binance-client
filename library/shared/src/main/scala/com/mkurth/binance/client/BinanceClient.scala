package com.mkurth.binance.client

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}

case class BinanceAuth(apiKey: String, secretKey: String)

class BinanceClient(binanceAuth: BinanceAuth, messageSigning: MessageSigning)(implicit val ex: ExecutionContext, val sttpBackend: SttpBackend[Future, Nothing]) {

  private val base = "https://api.binance.com/api/v1"
  private val baseV3 = "https://api.binance.com/api/v3"

  def ping: Future[Boolean] = {
    sttp.get(uri"$base/ping")
      .send()
      .map(_.body match {
        case Left(_) => true
        case Right(_) => false
      })
  }

  def serverTime: Future[Long] = {
    sttp.get(uri"$base/time")
      .send()
      .map(response => response.unsafeBody.toLong)
  }

  def testOrder(order: Order): Future[OrderResponse] = {
    val json = order.asJson
    messageSigning.hmacSha256(binanceAuth.secretKey, json.toString()).flatMap(data => {
      sttp.post(uri"$base/order/test")
        .body(json.asJsonObject.add("signature", Json.fromString(data)))
        .response(asJson[OrderResponse])
        .send()
        .map(response => response.body.right.get.right.get)
    })
  }

  def order(order: Order): Future[OrderResponse] = {
    val json = order.asJson
    messageSigning.hmacSha256(binanceAuth.secretKey, json.toString()).flatMap(data => {
      sttp.post(uri"$base/order")
        .body(json.asJsonObject.add("signature", Json.fromString(data)))
        .response(asJson[OrderResponse])
        .send()
        .map(response => response.body.right.get.right.get)
    })
  }

  def account: Future[AccountInfo] = {
    serverTime.flatMap(timestamp =>
      messageSigning.hmacSha256(binanceAuth.secretKey, s"timestamp=$timestamp").flatMap(signature =>
        sttp.get(uri"$baseV3/account?timestamp=$timestamp&signature=$signature")
          .response(asJson[AccountInfo])
          .send()
          .map(response => response.body.right.get.right.get)
      )
    )
  }

  def exchangeInfo: Future[ExchangeInfo] = {
    import Filter.decodeFilter
    sttp.get(uri"$base/exchangeInfo")
      .response(asJson[ExchangeInfo])
      .send()
      .map(response => response.body.right.get.right.get)
  }

}
