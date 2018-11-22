package com.mkurth.binance.client

case class RateLimit(rateLimitType: String,
                     interval: String,
                     intervalNum: Int,
                     limit: Long)