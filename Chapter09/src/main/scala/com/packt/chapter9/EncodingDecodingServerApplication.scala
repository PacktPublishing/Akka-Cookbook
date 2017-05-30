package com.packt.chapter9

import akka.http.scaladsl.coding._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory

object EncodingDecodingServer extends HttpApp {
  val route =
    post {
      decodeRequestWith(Gzip, NoCoding) {
        entity(as[String]) { stringEntity =>
          encodeResponse {
            complete {
              println(s"Received $stringEntity")
              StatusCodes.OK -> s"Thank you for your encoded request [$stringEntity]."
            }
          }
        }
      }
    }
}

object EncodingDecodingServerApplication extends App {
  EncodingDecodingServer.startServer("0.0.0.0", 8088, ServerSettings(ConfigFactory.load))
}

