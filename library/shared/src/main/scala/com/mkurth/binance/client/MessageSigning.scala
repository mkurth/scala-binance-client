package com.mkurth.binance.client

import scala.concurrent.{ExecutionContext, Future}

trait MessageSigning {

  def hmacSha256(secret: String, data: String)(implicit ex: ExecutionContext): Future[String]

}
