package com.packt.chapter9

import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory

import scala.collection.mutable

class TemperatureInMemoryStorageRestApi(cache: mutable.Map[String, TemperatureMeasurement]) extends HttpApp
  with InMemoryStorageRestApi[String, TemperatureMeasurement]
  with GetRequestsHandler
  with PostRequestsHandler
  with PutRequestsHandler
  with DeleteRequestsHandler {

  implicit val fixedPath = "temperature"
  val route = composedRoute(cache)
}

object TemperatureInMemoryStorageRestApiApplication extends App {
  val cache = mutable.Map.empty[String, TemperatureMeasurement]
  new TemperatureInMemoryStorageRestApi(cache).startServer("0.0.0.0", 8088, ServerSettings(ConfigFactory.load))
}
