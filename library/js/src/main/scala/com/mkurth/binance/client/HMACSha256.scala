package com.mkurth.binance.client

import org.scalajs.dom.crypto._

import scala.concurrent.{ExecutionContext, Future}
import scala.scalajs.js
import scala.scalajs.js.typedarray.{ArrayBuffer, Uint8Array, byteArray2Int8Array}

class HMACSha256 extends MessageSigning {

  def hmacSha256(secret: String, data: String)(implicit ex: ExecutionContext): Future[String] = {
    val keyData = byteArray2Int8Array(secret.getBytes)
    val hmac: KeyAlgorithmIdentifier = HmacKeyAlgorithm("HMAC", HashAlgorithm.`SHA-256`, 512)
    val key = GlobalCrypto.crypto.subtle.importKey(
      format = KeyFormat.raw,
      keyData = keyData,
      extractable = false,
      algorithm = hmac,
      keyUsages = js.Array(KeyUsage.sign)
    ).toFuture.asInstanceOf[Future[CryptoKey]]

    key.flatMap(k => GlobalCrypto.crypto.subtle.sign("HMAC", k, byteArray2Int8Array(data.getBytes)).toFuture.map(buf => {
      val bab = buf.asInstanceOf[ArrayBuffer]
      val x = new Uint8Array(bab)
      x.map(short => ("00" + short.toHexString).takeRight(2)).mkString
    }))
  }

}
