package com.packt.chapter9

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.HttpApp
import akka.http.scaladsl.settings.ServerSettings
import com.codahale.metrics.{ConsoleReporter, MetricRegistry}
import com.typesafe.config.ConfigFactory

object CustomDirectivesServer extends HttpApp with MetricDirectives {
  private val metricRegistry = new MetricRegistry()
  ConsoleReporter.forRegistry(metricRegistry).build().start(10, TimeUnit.SECONDS)

  val route =
    timer(metricRegistry) {
      get {
        complete { Thread.sleep(200); "Hello from GET!" }
      } ~
        post {
          complete { Thread.sleep(500); "Hello from POST!" }
        } ~
      put {
        meter(metricRegistry) {
          complete { "Hello from PUT!" }
        }
      }
    }
}

object CustomDirectivesApplication extends App {
  CustomDirectivesServer.startServer("0.0.0.0", 8088, ServerSettings(ConfigFactory.load))
}

