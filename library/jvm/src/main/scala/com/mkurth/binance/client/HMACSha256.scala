package com.mkurth.binance.client

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import scala.concurrent.{ExecutionContext, Future}


class HMACSha256 extends MessageSigning {
  override def hmacSha256(sec: String, data: String)(implicit ex: ExecutionContext): Future[String] = {
    Future {
      val secret = new SecretKeySpec(sec.getBytes("utf-8"), "HmacSHA256")
      val mac = Mac.getInstance("HmacSHA256")
      mac.init(secret)
      val hashString: Array[Byte] = mac.doFinal(data.getBytes("utf-8"))

      printHexBinary(hashString).toLowerCase
    }
  }

  def printHexBinary(data: Array[Byte]): String = {
    val builder = new StringBuilder(data.length * 2)
    val hexCode = "0123456789ABCDEF".toCharArray
    data.foreach(b => {
      builder.append(hexCode(b >> 4 & 15))
      builder.append(hexCode(b & 15))
    })

    builder.toString
  }
}
