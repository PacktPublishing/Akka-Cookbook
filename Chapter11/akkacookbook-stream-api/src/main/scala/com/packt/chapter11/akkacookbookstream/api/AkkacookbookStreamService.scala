package com.packt.chapter11.akkacookbookstream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * The AkkaCookbook stream interface.
  *
  * This describes everything that Lagom needs to know about how to serve and
  * consume the AkkacookbookStream service.
  */
trait AkkacookbookStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor = {
    import Service._

    named("akkacookbook-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}

