package com.packt.chapter11.consumer.api

import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait ConsumerService extends Service {
  def consume: ServiceCall[ConsumeRequest, Boolean]

  override final def descriptor = {
    import Service._
    named("consume").withCalls(
      pathCall("/api/consume", consume)
    ).withAutoAcl(true)
  }
}
