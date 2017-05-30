package com.packt.chapter11.trip.api

import play.api.libs.json.{Format, Json}

case class ReportLocation(latitude: Double, longitude: Double)

object ReportLocation {
  implicit val reportLocationRequestFormat: Format[ReportLocation] = Json.format[ReportLocation]
}