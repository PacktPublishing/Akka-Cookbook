package com.packt.chapter11.akka.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait CalculatorService extends Service {
  def add(one: Int, other: Int): ServiceCall[NotUsed, Int]
  def multiply(one: Int, other: Int): ServiceCall[NotUsed, Int]

  override final def descriptor = {
    import Service._
    named("calculator").withCalls(
      pathCall("/add/:one/:other", add _),
      pathCall("/multiply/:one/:other", multiply _)
    ).withAutoAcl(true)
  }
}
