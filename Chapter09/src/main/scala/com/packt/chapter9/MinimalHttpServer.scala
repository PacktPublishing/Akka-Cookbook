package com.packt.chapter9

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.settings.ServerSettings
import com.typesafe.config.ConfigFactory

object MinimalHttpServer extends HttpApp {
  def route =
    pathPrefix("v1") {
      path("id" / Segment) { id =>
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Hello $id from Akka Http!</h1>"))
        } ~
        post {
          entity(as[String]) { entity =>
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<b>Thanks $id for posting your message <i>$entity</i></b>"))
          }
        }
      }
    }
}

object MinimalHttpServerApplication extends App {
  MinimalHttpServer.startServer("0.0.0.0", 8088, ServerSettings(ConfigFactory.load))
}
