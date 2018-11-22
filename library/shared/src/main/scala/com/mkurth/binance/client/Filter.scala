package com.mkurth.binance.client

import cats.syntax.functor._
import io.circe.Decoder
import io.circe.generic.auto._

sealed trait Filter

object Filter {
  implicit val decodeFilter: Decoder[Filter] =
    List[Decoder[Filter]](
      Decoder[MinNotional].widen,
      Decoder[PriceFilter].widen,
      Decoder[LotSize].widen,
      Decoder[IcebergParts].widen,
      Decoder[MaxNumAlgoOrders].widen
    ).reduceLeft(_ or _)
}

case class PriceFilter(minPrice: BigDecimal,
                       maxPrice: BigDecimal,
                       tickSize: BigDecimal) extends Filter

case class LotSize(minQty: BigDecimal,
                   maxQty: BigDecimal,
                   stepSize: BigDecimal) extends Filter

case class MinNotional(minNotional: BigDecimal) extends Filter

case class IcebergParts(limit: Int) extends Filter

case class MaxNumAlgoOrders(maxNumAlgoOrders: Int) extends Filter