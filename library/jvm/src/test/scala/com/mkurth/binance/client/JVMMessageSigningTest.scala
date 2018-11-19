package com.mkurth.binance.client

import org.scalatest._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class JVMMessageSigningTest extends FlatSpec with Matchers {

  "JVMMessageSigning" should "create same signing output as example on binance API" in {
    val input = "symbol=LTCBTC&side=BUY&type=LIMIT&timeInForce=GTC&quantity=1&price=0.1&recvWindow=5000&timestamp=1499827319559"
    val secret = "NhqPtmdSJYdKjVHjA7PZj4Mge3R5YNiP1e3UZjInClVN65XAbvqqM6A7H5fATj0j"
    val signer = new JVMMessageSigning()

    Await.result(signer.hmacSha256(secret, input), 1 second) should be("c8db56825ae71d6d79447849e617115f4a920fa2acdcab2b053c4b2838bd6b71")
  }

}
