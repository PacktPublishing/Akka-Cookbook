package com.packt.chapter11.trip.impl

import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import scala.collection.immutable.Seq

object ClientSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[Location],
    JsonSerializer[ClientState],
    JsonSerializer[TripStarted],
    JsonSerializer[TripEnded],
    JsonSerializer[LocationAdded],
    JsonSerializer[AddLocation]
  )
}
