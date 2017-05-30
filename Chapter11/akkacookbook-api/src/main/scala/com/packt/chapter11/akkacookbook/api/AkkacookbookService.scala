package com.packt.chapter11.akkacookbook.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

/**
  * The AkkaCookbook service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the AkkacookbookService.
  */
//trait AkkacookbookService extends Service {
//  def hello(id: String): ServiceCall[NotUsed, String]
//  def useGreeting(id: String): ServiceCall[GreetingMessage, Done]
//  def healthCheck() : ServiceCall[NotUsed, Done]
//
//  override final def descriptor = {
//    import Service._
//    named("akkacookbook").withCalls(
//      pathCall("/api/hello/:id", hello _),
//      pathCall("/api/hello/:id", useGreeting _),
//      pathCall("/api/healthcheck", healthCheck)
//    ).withAutoAcl(true)
//  }
//}

trait AkkacookbookService extends Service {
  def toUppercase: ServiceCall[String, String]
  def toLowercase: ServiceCall[String, String]
  def isEmpty(str: String): ServiceCall[NotUsed, Boolean]
  def areEqual(str1: String, str2: String) : ServiceCall[NotUsed, Boolean]

  override final def descriptor = {
    import Service._
    named("stringutils").withCalls(
      call(toUppercase),
      namedCall("toLowercase", toLowercase),
      pathCall("/isEmpty/:str", isEmpty _),
      restCall(Method.GET, "/areEqual/:one/another/:other", areEqual _)
    ).withAutoAcl(true)
  }
}

/**
  * The greeting message class.
  */
case class GreetingMessage(message: String)

object GreetingMessage {
  /**
    * Format for converting greeting messages to and from JSON.
    *
    * This will be picked up by a Lagom implicit conversion from Play's JSON format to Lagom's message serializer.
    */
  implicit val format: Format[GreetingMessage] = Json.format[GreetingMessage]
}
