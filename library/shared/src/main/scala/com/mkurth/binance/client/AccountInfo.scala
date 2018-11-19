package com.mkurth.binance.client

case class Balance(asset: String, free: String, locked: String)

case class AccountInfo(canTrade: Boolean,
                       canWithdraw: Boolean,
                       canDeposit: Boolean,
                       balances: Seq[Balance])
