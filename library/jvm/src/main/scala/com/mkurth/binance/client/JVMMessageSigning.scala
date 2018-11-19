package com.mkurth.binance.client

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

import scala.concurrent.{ExecutionContext, Future}


class JVMMessageSigning extends MessageSigning {
  override def hmacSha256(sec: String, data: String)(implicit ex: ExecutionContext): Future[String] = {
    Future {
      val secret = new SecretKeySpec(sec.getBytes("utf-8"), "HmacSHA256")
      val mac = Mac.getInstance("HmacSHA256")
      mac.init(secret)
      val hashString: Array[Byte] = mac.doFinal(data.getBytes("utf-8"))
      DatatypeConverter.printHexBinary(hashString).toLowerCase
    }
  }
}
