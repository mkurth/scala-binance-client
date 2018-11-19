package com.mkurth.binance.client

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val input = "symbol=LTCBTC&side=BUY&type=LIMIT&timeInForce=GTC&quantity=1&price=0.1&recvWindow=5000&timestamp=1499827319559"
  val secret = "NhqPtmdSJYdKjVHjA7PZj4Mge3R5YNiP1e3UZjInClVN65XAbvqqM6A7H5fATj0j"
  val signer = new JsMessageSigning()

  signer.hmacSha256(secret, input).map(println)
}
