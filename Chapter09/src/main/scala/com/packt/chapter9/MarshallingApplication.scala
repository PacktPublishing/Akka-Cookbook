package com.packt.chapter9

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory

object MarshallingServer extends HttpApp with SpeedMeasurementMarshallingHelper {
  var measurement: Option[SpeedMeasurement] = None

  val route =
    get {
      complete {
        measurement match {
          case None => StatusCodes.NotFound -> "Speed Measurement is empty"
          case Some(value) => StatusCodes.OK -> value
        }
      }
    } ~
    post {
      entity(as[SpeedMeasurement]) { speedMeasurement =>
        complete {
          measurement = Some(speedMeasurement)
          StatusCodes.OK -> s"Speed Measurement now is $speedMeasurement"
        }
      }
    }
}

object MarshallingApplication extends App {
  MarshallingServer.startServer("0.0.0.0", 8088, ServerSettings(ConfigFactory.load))
}

