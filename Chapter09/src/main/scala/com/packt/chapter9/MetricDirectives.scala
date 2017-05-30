package com.packt.chapter9

import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives._
import com.codahale.metrics.MetricRegistry

trait MetricDirectives {
  def meter(metricRegistry: MetricRegistry) : Directive0 = {
    extractMethod.flatMap[Unit] { httpMethod =>
      extractUri.flatMap { uri ⇒
        metricRegistry.meter(s"meter-Method[${httpMethod.value}]-Uri[${uri.path.toString}]").mark
        pass
      }
    }
  }

  def timer(metricRegistry: MetricRegistry) : Directive0 = {
    extractMethod.flatMap[Unit] { httpMethod =>
      extractUri.flatMap { uri ⇒
        val timer = metricRegistry.timer(s"timer-Method[${httpMethod.value}]-Uri[${uri.path.toString}]")
        val timerContext = timer.time()
        mapRouteResult { x ⇒
          timerContext.stop()
          x
        }
      }
    }
  }
}
