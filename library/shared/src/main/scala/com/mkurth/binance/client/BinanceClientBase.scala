package com.mkurth.binance.client

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

case class BinanceAuth(apiKey: String, secretKey: String)

case class ServerTime(serverTime: Long)

trait BinanceClientBase {

  def basePath = "https://api.binance.com"
  val binanceAuth: BinanceAuth
  val messageSigning: MessageSigning
  implicit val ex: ExecutionContext
  implicit val sttpBackend: SttpBackend[Future, Nothing]

  private val base = s"$basePath/api/v1"
  private val baseV3 = s"$basePath/api/v3"

  lazy val timeOffset: Future[Long] = {
    serverTime.map(server => System.currentTimeMillis() - server.right.get.serverTime)
  }

  def ping: Future[Boolean] = {
    sttp.get(uri"$base/ping")
      .send()
      .map(_.body.isRight)
  }

  def serverTime: Future[Either[String, ServerTime]] = {
    sttp.get(uri"$base/time")
      .response(asJson[ServerTime])
      .send()
      .map(_.body)
  }

  def testOrder(order: Order): Future[Either[String, OrderResponse]] = {
    val json = order.asJson
    messageSigning.hmacSha256(binanceAuth.secretKey, json.toString()).flatMap(data => {
      sttp.post(uri"$base/order/test")
        .body(json.withObject(obj => obj.add("signature", Json.fromString(data)).asJson))
        .header("X-MBX-APIKEY", binanceAuth.apiKey)
        .response(asJson[OrderResponse])
        .send()
        .map(_.body)
    })
  }

  def order(order: Order): Future[Either[String, OrderResponse]] = {
    val json = order.asJson
    messageSigning.hmacSha256(binanceAuth.secretKey, json.toString()).flatMap(data => {
      sttp.post(uri"$base/order")
        .body(json.withObject(obj => obj.add("signature", Json.fromString(data)).asJson))
        .header("X-MBX-APIKEY", binanceAuth.apiKey)
        .response(asJson[OrderResponse])
        .send()
        .map(_.body)
    })
  }

  def account: Future[Either[String, AccountInfo]] = {
    timeOffset.flatMap(timeOffset => {
      val timestamp = System.currentTimeMillis() - timeOffset
      messageSigning.hmacSha256(binanceAuth.secretKey, s"timestamp=$timestamp").flatMap(signature =>
        sttp.get(uri"$baseV3/account?timestamp=$timestamp&signature=$signature")
          .header("X-MBX-APIKEY", binanceAuth.apiKey)
          .response(asJson[AccountInfo])
          .send()
          .map(_.body)
      )
    })
  }

  def exchangeInfo: Future[Either[String, ExchangeInfo]] = {
    sttp.get(uri"$base/exchangeInfo")
      .response(asJson[ExchangeInfo])
      .send()
      .map(_.body)
  }

  implicit def responseMapper[A](either: Either[String, Either[DeserializationError[io.circe.Error], A]]): Either[String, A] = {
    either match {
      case Right(Right(body)) => Right(body)
      case Right(Left(error)) => Left(error.message)
      case Left(error) => Left(error)
    }
  }

}
